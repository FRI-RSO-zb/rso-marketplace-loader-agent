package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonLoader;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Log
@Path("loaders")
@Tag(name="Loaders", description="Endpoints for starting and reviewing the data loading jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods =  "GET, POST, HEAD, OPTIONS, PUT, DELETE", allowOrigin = "*")
public class LoadersController {
    private static final String AVTONET = "avtonet";
    private static final String BOLHA = "bolha";
    private static final String DOBERAVTO = "doberavto";
    private static final String SALOMON = "salomon";
    private static final String OGLASISI = "oglasisi";

    private static final String LATEST_PAGE = "latest";

    @Inject
    @Metric(name="loading_meter")
    private Meter loadingMeter;

//    @GET
//    @Path("jobs")
//    public Response getLoaderJobs() {
//        return Response.ok("0").build();
//    }

    @GET
    @Operation(
            summary = "Get the list of loaders",
            description = "Returns the list of supported source loaders"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of loaders",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = String.class))
            )
    })
    public Response getListOfLoaders() {
        List<String> list = new ArrayList<>();
        list.add(AVTONET);
        list.add(BOLHA);
        list.add(DOBERAVTO);
        list.add(SALOMON);
        list.add(OGLASISI);

        return Response.ok(list)
                .build();
    }

    @GET
    @Path("{loader}/pages")
    @Operation(
            summary = "Get the list of pages in loader",
            description = "Returns the list of supported pages by the loader"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of supported pages",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = String.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Loader not found."
            ),
    })
    public Response getListOfLoaderPages(
            @PathParam("loader") @Parameter(description = "The name of loader", example = AVTONET, required = true) String loader
    ) {
        if (loader.equals(AVTONET) || loader.equals(BOLHA) || loader.equals(DOBERAVTO) || loader.equals(SALOMON) || loader.equals(OGLASISI)) {
            List<String> list = new ArrayList<>();
            list.add(LATEST_PAGE);

            return Response.ok(list).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{loader}/{page}/load")
    @Operation(
            summary = "Load the page",
            description = "Start the loading job for specified page using specified loader."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Page loaded",
                    content = @Content(schema = @Schema(implementation = LoadingResult.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Page not found in loader."
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Page loading failed."
            )
    })
    @Timed(name="loaders_load_timer")
    @Metered(name="loaders_load_meter")
    public Response loadPage(
            @PathParam("loader") @Parameter(description = "The name of loader to use", example = AVTONET, required = true) String loader,
            @PathParam("page") @Parameter(description = "The name of page to load", example = LATEST_PAGE, required = true) String page
    ) throws IOException {
        this.loadingMeter.mark();

        LoadingResult result = switch (loader) {
            case AVTONET -> switch (page) {
                case LATEST_PAGE -> new AvtoNetLoader().loadAvtonetTop100List();
                default -> null;
            };
            case BOLHA -> switch (page) {
                case LATEST_PAGE -> new BolhaLoader().loadLatestCarAds();
                default -> null;
            };
            case DOBERAVTO -> switch (page) {
                case LATEST_PAGE -> new DoberAvtoLoader().loadLatestCarAds();
                default -> null;
            };
            case SALOMON -> switch (page) {
                case LATEST_PAGE -> new SalomonLoader().loadLatestCarAds();
                default -> null;
            };
            case OGLASISI -> switch (page) {
                case LATEST_PAGE -> new OglasiSiLoader().loadLatestCarAds();
                default -> null;
            };
            default -> null;
        };

        return result != null ?
                Response.ok(result).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }
}
