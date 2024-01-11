package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.v1.info.VersionInfo;
import net.bobnar.marketplace.loaderAgent.api.v1.dtos.DebugConfig;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@Log
@Path("debug")
@RequestScoped
@Tag(name = "Debug", description = "Debugging interface")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(name="debug", allowOrigin = "*", supportedMethods = "GET, HEAD, POST, OPTIONS, DELETE")
public class DebugController {
    private Logger log = Logger.getLogger(DebugConfig.class.getName());

    @Inject
    private ServiceConfig serviceConfig;

    @POST
    @Path("break")
    @Operation(
            summary = "Set the state of this service instance as broken",
            description = "Set the state of this service instance as broken. Used to debug if the cluster will heal itself.",
            hidden = false // true
    )
    public Response debugBreakInstance() {
        serviceConfig.disable();

        return Response.ok()
                .build();
    }

    @GET
    @Path("config")
    @Operation(
            summary = "Get the configuration options",
            description = "Gets the configuration options.",
            hidden = false // true
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = DebugConfig.class))
            )
    })
    public Response getConfig() {
        return Response.ok(getCurrentConfig())
                .build();
    }

    @PUT
    @Path("config")
    @Operation(
            summary = "Set the configuration options",
            description = "Sets the configuration options.",
            hidden = false // true
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = DebugConfig.class))
            )
    })
    public Response setConfig(
            @RequestBody(description = "Configuration", required = true, content = @Content(schema = @Schema(implementation = DebugConfig.class)))
            DebugConfig config) {
        serviceConfig.setEnabled(config.isEnabled());
        serviceConfig.setUseInternalResources(config.isUseInternalResources());
        serviceConfig.setIntentionalyBrokenCircuit(config.isIntentionalyBrokenCircuit());

        return Response.ok(getCurrentConfig())
                .build();
    }

    private DebugConfig getCurrentConfig() {
        DebugConfig result = new DebugConfig();
        result.setEnabled(serviceConfig.isEnabled());
        result.setUseInternalResources(serviceConfig.shouldUseInternalResources());
        result.setIntentionalyBrokenCircuit(serviceConfig.isIntentionalyBrokenCircuit());

        return result;
    }
}
