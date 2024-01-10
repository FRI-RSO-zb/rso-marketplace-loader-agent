package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("debug")
@RequestScoped
@Tag(name = "Debug", description = "Debugging interface")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(name="debug", allowOrigin = "*", supportedMethods = "GET, HEAD, POST, OPTIONS, DELETE")
public class DebugController {

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
}
