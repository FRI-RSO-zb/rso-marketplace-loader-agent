package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.pipelines.ItemToExport;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.pipelines.ItemsToExport;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.processors.ProcessingResult;
import net.bobnar.marketplace.loaderAgent.services.catalog.CatalogCarModelsServiceClient;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.parboiled.common.Tuple2;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Log
@Path("pipelines")
@Tag(name="Pipelines (DRAFT)", description = "Endpoints that combine the service operations and allow exporting of data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods =  "GET, POST, HEAD, OPTIONS, PUT, DELETE", allowOrigin = "*")
@RequestScoped
public class PipelinesController {


//    @Inject
//    @Metric(name="processing_meter")
//    private Meter processingMeter;

    @Inject
    private ServiceConfig serviceConfig;

    @Inject
    private CatalogCarModelsServiceClient carModelsClient;

    @POST
    @Path("/export/site/{site}/to/catalog")
    @Operation(
            summary = "Loads the site and exports it to the catalog",
            description = "Starts the loading of site and processes the result, then exports it to catalog."
    )
//    @APIResponses({
//            @APIResponse(
//                    responseCode = "200",
//                    description = "Input processed",
//                    content = @Content(schema = @Schema(implementation = ProcessingResult.class))
//            ),
//            @APIResponse(
//                    responseCode = "404",
//                    description = "Input type not supported by processor."
//            ),
//            @APIResponse(
//                    responseCode = "500",
//                    description = "Input processing failed."
//            )
//    })
//    @Timed(name="processors_process_timer")
//    @Metered(name="processors_process_meter")
    public Response process(
            @PathParam("site") @Parameter(description = "The site name", example = "doberavto", required = true) String site
    ) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        if ("doberavto".equals(site)) {
            DoberAvtoLoader loader = new DoberAvtoLoader(serviceConfig.shouldUseInternalResources());
            LoadingResult latestList;
            try {
                latestList = loader.loadLatestCarAds();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DoberAvtoProcessor processor = new DoberAvtoProcessor();
            ProcessListResult<DoberAvtoListItem> processedList = processor.processItemList(latestList.content());
            result.put("processedList", processedList);

            var dataForResolving = new HashMap<String, Object>();
            var identifiers = processedList.processedItems.stream().map(v -> v.getManufacturer().toLowerCase().replace(' ', '-') + " " + v.getTitle().split(" ")[0].toLowerCase()).collect(Collectors.toList());
            dataForResolving.put("identifiers",  identifiers);

            var brandAndModelIds = carModelsClient.findMultipleModelsByIdentifiers(
                    processedList.processedItems.stream().map(v -> new Tuple2<>(
                            v.getManufacturer().toLowerCase().replace(' ', '-'),
                            v.getTitle().split(" ")[0].toLowerCase()
                    )).toList()
            );

            result.put("resolvedModels", brandAndModelIds);
            result.put("dataforresolving", dataForResolving);

            List<String> unresolvedBrands = new ArrayList<>();
            List<String> unresolvedModels = new ArrayList<>();
            List<Integer> resolvedIndexes = new ArrayList<>();
            for (int i = 0; i < brandAndModelIds.size(); i++) {
                if (brandAndModelIds.get(i).a == null || brandAndModelIds.get(i).a == 0) {
                    unresolvedBrands.add(identifiers.get(i));
                } else if (brandAndModelIds.get(i).b == null || brandAndModelIds.get(i).b == 0) {
                    unresolvedModels.add(identifiers.get(i));
                } else {
                    resolvedIndexes.add(i);
                }
            }

            result.put("unresolvedBrands", unresolvedBrands);
            result.put("unresolvedModels", unresolvedModels);
            result.put("resolvedIndexes", resolvedIndexes);


            List<String> resolvedSourceIds=new ArrayList<>();
            for (var i : resolvedIndexes) {
                resolvedSourceIds.add(processedList.processedItems.get(i).getId());
            }

            Ad[] existingAds = new Ad[0];
            try {
                HttpClient client = HttpClient.newHttpClient();
                String url = "http://localhost:8801/v1/ads?sources=doberavto&" + String.join("&", resolvedSourceIds.stream().map(x -> "sourceIds=" + x).toList());
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .timeout(Duration.of(2, ChronoUnit.SECONDS))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new RuntimeException("Status not 200: " + response.statusCode() + ". " + response.body());
                }

                ObjectMapper mapper = new ObjectMapper();
                existingAds = mapper.readValue(response.body(), Ad[].class);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            result.put("existingAds", existingAds);

            List<Ad> newAds = new ArrayList<>();
            for (var i : resolvedIndexes) {
                var brandId = brandAndModelIds.get(i).a;
                var modelId = brandAndModelIds.get(i).b;
                var item = processedList.processedItems.get(i);
                var source = "doberavto";
                var sourceId = item.getId();

                if (Arrays.stream(existingAds).toList().stream().anyMatch(v -> Objects.equals(v.getSourceId(), sourceId))) {
                    continue;
                }

                var newAd = new Ad();
                newAd.setTitle(item.getTitle());
                newAd.setSource(source);
                newAd.setSourceId(sourceId);
                newAd.setModelId(modelId);
                newAd.setPhotoUri(item.getPhotoPath());

                newAds.add(newAd);
            }
            result.put("newAds", newAds);



            Ad[] createdAds = new Ad[0];
            try {
                HttpClient client = HttpClient.newHttpClient();
                ObjectMapper mapper = new ObjectMapper();
                String data = mapper.writeValueAsString(newAds);
                String url = "http://localhost:8801/v1/ads";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .timeout(Duration.of(2, ChronoUnit.SECONDS))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(data))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 201) {
                    throw new RuntimeException("Status not 200: " + response.statusCode() + ". " + response.body());
                }

//                ObjectMapper mapper = new ObjectMapper();
                createdAds = mapper.readValue(response.body(), Ad[].class);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            result.put("createdAds", createdAds);

        }

        return result != null ?
                Response.ok(result).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }


    @POST
    @Path("export/item/to/catalog")
    @Operation(
            summary = "Export the processed item to the catalog service (DRAFT)",
            description = "Tries to export the provided processed data to the catalog service.",
            hidden = true
    )
    @APIResponses({
//            @APIResponse(
//                    responseCode = "200",
//                    description = "Input processed",
//                    content = @Content(schema = @Schema(implementation = ProcessingResult.class))
//            ),
//            @APIResponse(
//                    responseCode = "404",
//                    description = "Input type not supported by processor."
//            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Request failed."
            )
    })
    @Timed(name="export_catalog_item_timer")
    @Metered(name="export_catalog_item_meter")
    public Response exportSingleToCatalog(
            @RequestBody(description = "The item to be exported to catalog", required = true, content = @Content(schema = @Schema(implementation = ItemToExport.class))) ItemToExport item
    ) {
        return Response.serverError().build();
    }

    @POST
    @Path("export/items/to/catalog")
    @Operation(
            summary = "Export the processed items to the catalog service (DRAFT)",
            description = "Tries to export the provided processed data that contains multiple items to the catalog service.",
            deprecated = true,
            hidden = true
    )
    @APIResponses({
//            @APIResponse(
//                    responseCode = "200",
//                    description = "Input processed",
//                    content = @Content(schema = @Schema(implementation = ProcessingResult.class))
//            ),
//            @APIResponse(
//                    responseCode = "404",
//                    description = "Input type not supported by processor."
//            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Request failed."
            )
    })
    @Timed(name="export_catalog_items_timer")
    @Metered(name="export_catalog_items_meter")
    public Response ExportMultipleToCatalog(
            @RequestBody(description = "The items to be exported to catalog", content = @Content(schema = @Schema(implementation = ItemsToExport.class)), required = true) ItemsToExport items
    ) {
        return Response.serverError().build();
    }

}
