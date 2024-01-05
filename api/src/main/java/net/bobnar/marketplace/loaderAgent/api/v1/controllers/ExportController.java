package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.export.ItemToExport;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Log
@Path("export")
@Tag(name="Export (DRAFT)", description = "Endpoints that allow exporting the processed data into storage ")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods =  "GET, POST, HEAD, OPTIONS, PUT, DELETE", allowOrigin = "*")
public class ExportController {

//    @Inject
//    @Metric(name="processing_meter")
//    private Meter processingMeter;



    @POST
    @Path("catalog/item")
    @Operation(
            summary = "Export the processed item to the catalog service (DRAFT)",
            description = "Tries to export the provided processed data to the catalog service."
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
            @RequestBody(description = "Ad item", required = true, content = @Content(schema = @Schema(implementation = ItemToExport.class))) ItemToExport item

//            @RequestBody(description = "Created user object", required = true,
//                    content = @Content(schema = @Schema(/*implementation = ItemToExport.class, */type = SchemaType.OBJECT))) ItemToExport user
//            @RequestBody(description = "The item to be exported to catalog", content = @Content(schema = @Schema(implementation = ItemToExport.class)), required = true) ItemToExport item
    ) {
        return Response.serverError().build();
    }

    @POST
    @Path("catalog/items")
    @Operation(
            summary = "Export the processed items to the catalog service (DRAFT)",
            description = "Tries to export the provided processed data that contains multiple items to the catalog service.",
            deprecated = true
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
//            @RequestBody(description = "The items to be exported to catalog", content = @Content(schema = @Schema(implementation = ItemsToExport.class)), required = true) ItemsToExport items
    ) {
        return Response.serverError().build();
    }

    @Schema
    public record ItemsToExport(
            List<ItemToExport> items
    ) {}
}
