package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.common.controllers.ControllerBase;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.pipelines.ItemToExport;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.pipelines.ItemsToExport;
import net.bobnar.marketplace.loaderAgent.services.LoaderHelper;
import net.bobnar.marketplace.loaderAgent.services.ProcessorHelper;
import net.bobnar.marketplace.loaderAgent.services.catalog.AdsService;
import net.bobnar.marketplace.loaderAgent.services.catalog.BrandsService;
import net.bobnar.marketplace.loaderAgent.services.catalog.CatalogCarModelsServiceClient;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.IProcessedAdBriefData;
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
import org.parboiled.common.Tuple3;

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
import java.util.stream.Collectors;


@Log
@Path("pipelines")
@Tag(name="Pipelines (DRAFT)", description = "Endpoints that combine the service operations and allow exporting of data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods =  "GET, POST, HEAD, OPTIONS, PUT, DELETE", allowOrigin = "*")
@RequestScoped
public class PipelinesController extends ControllerBase {


//    @Inject
//    @Metric(name="processing_meter")
//    private Meter processingMeter;

    @Inject
    private ServiceConfig serviceConfig;

    @Inject
    private CatalogCarModelsServiceClient carModelsClient;

    @Inject
    private BrandsService brandsService;

    @Inject
    private AdsService adsService;

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
        HashMap<String, Object> result = new HashMap<>();

        result.put("source", site);
        result.put("shouldUseInternalResource", serviceConfig.shouldUseInternalResources());

        LoadingResult loadingResult = LoaderHelper.loadLatestListFromSource(site, serviceConfig.shouldUseInternalResources());
        if (loadingResult == null) {
            return respondNotFound();
        }

        ProcessListResult<IProcessedAdBriefData> processingResult = ProcessorHelper.processFromSource(site, loadingResult.content());
        if (processingResult == null) {
            return respondNotFound();
        }
        result.put("processingResult", processingResult.toDto());

        List<IProcessedAdBriefData> processedItems = processingResult.processedItems;


        List<String> allAdIds = processedItems.stream().map(IProcessedAdBriefData::getId).toList();
        Ad[] existingAds = adsService.getAdsWithSourceIds(site, allAdIds);
        result.put("existingAds", existingAds);


        List<IProcessedAdBriefData> missingAds = processedItems.stream()
                .filter(x-> Arrays.stream(existingAds)
                        .noneMatch(y-> Objects.equals(y.getSourceId(), x.getId())))
                .toList();
        result.put("missingAds", missingAds);


        List<CarBrand> resolvedBrands = new ArrayList<>();
        List<String> resolvedBrandsAdIds = new ArrayList<>();
        List<String> notResolvedBrands = new ArrayList<>();
        List<String> notResolvedBrandsAdIds = new ArrayList<>();
        for (IProcessedAdBriefData item : missingAds) {
            if (item.getBrand() != null && !item.getBrand().isEmpty()) {
                Optional<CarBrand> resolvedBrand = resolvedBrands.stream()
                        .filter(x-> Objects.equals(x.getPrimaryIdentifier(), item.getBrand()))
                        .findFirst();
                if (resolvedBrand.isPresent()) {
                    item.setBrand(resolvedBrand.get().getPrimaryIdentifier());
                    continue;
                }
            }

            CarBrand resolved = carModelsClient.resolveBrand(item.getBrand(), item.getTitle());
            if (resolved == null) {
                notResolvedBrands.add(item.getBrand() + " - " + item.getTitle());
                notResolvedBrandsAdIds.add(item.getId());
                continue;
            }

            item.setBrand(resolved.getPrimaryIdentifier());
            resolvedBrandsAdIds.add(item.getId());

            if (resolvedBrands.stream().noneMatch(x-> Objects.equals(x.getId(), resolved.getId()))) {
                resolvedBrands.add(resolved);
            }
        }

        result.put("resolvedBrands", resolvedBrands);
        result.put("resolvedBrandsAdIds", resolvedBrandsAdIds);
        result.put("notResolvedBrands", notResolvedBrands);
        result.put("notResolvedBrandsAdIds", notResolvedBrandsAdIds);


        List<CarModel> resolvedModels = new ArrayList<>();
        List<String> resolvedModelAdIds = new ArrayList<>();
        List<String> notResolvedModels = new ArrayList<>();
        List<String> notResolvedModelsAdIds = new ArrayList<>();
        for (IProcessedAdBriefData item : missingAds.stream().filter(x->resolvedBrandsAdIds.contains(x.getId())).toList()) {
            if (item.getModel() != null && !item.getModel().isEmpty()) {
                Optional<CarModel> resolvedModel = resolvedModels.stream().filter(x-> Objects.equals(x.getPrimaryIdentifier(), item.getModel())).findFirst();
                if (resolvedModel.isPresent()) {
                    item.setModel(resolvedModel.get().getPrimaryIdentifier());
                    continue;
                }
            }

            CarModel resolved = carModelsClient.resolveModel(item.getBrand(), item.getModel(), item.getTitle());
            if (resolved == null) {
                notResolvedModels.add(item.getBrand() + " - " + item.getModel() + " - " + item.getTitle());
                notResolvedModelsAdIds.add(item.getId());
                continue;
            }

            item.setModel(resolved.getPrimaryIdentifier());
            resolvedModelAdIds.add(item.getId());

            if (resolvedModels.stream().noneMatch(x-> Objects.equals(x.getId(), resolved.getId()))) {
                resolvedModels.add(resolved);
            }
        }

        result.put("resolvedModels", resolvedModels);
        result.put("resolvedModelAdIds", resolvedModelAdIds);
        result.put("notResolvedModels", notResolvedModels);
        result.put("notResolvedModelsAdIds", notResolvedModelsAdIds);



        List<Ad> adsToCreate = new ArrayList<>();
        for (String adId : resolvedModelAdIds) {
            IProcessedAdBriefData ad = missingAds.stream().filter(x-> x.getId().equals(adId)).findFirst().get();
            CarModel model = resolvedModels.stream().filter(x-> x.getPrimaryIdentifier().equals(ad.getModel())).findFirst().get();

            Ad newAd = new Ad();
            newAd.setTitle(ad.getTitle());
            newAd.setSource(site);
            newAd.setSourceId(ad.getId());
            newAd.setModelId(model.getId());
            newAd.setPhotoUri(ad.getPhotoUrl());
            newAd.setOtherData(ad.getOtherData());

            adsToCreate.add(newAd);
        }
        result.put("adsToCreate", adsToCreate);


        if (!adsToCreate.isEmpty()) {
            Ad[] createdAds = adsService.createAds(adsToCreate.toArray(new Ad[0]));
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
