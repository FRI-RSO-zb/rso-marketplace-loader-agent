package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import net.bobnar.marketplace.common.controllers.InfoControllerBase;
import net.bobnar.marketplace.common.dtos.v1.info.Info;
import net.bobnar.marketplace.common.dtos.v1.info.VersionInfo;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;


@Path("info")
@ApplicationScoped
@Tag(name = "Info", description = "Deployment instance information")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfoController /*extends InfoControllerBase*/ {


//    @Override
//    protected Properties getProperties() {
//        return this.loadResourceProperties(this.getClass(), "/META-INF/service.properties");
//    }
}
