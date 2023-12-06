package net.bobnar.marketplace.loaderAgent.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


//@SecurityScheme(
//        securitySchemeName = "openid-connect",
//        type = SecuritySchemeType.OPENIDCONNECT,
//        openIdConnectUrl = "https://id.marketplace.bobnar.net/.well-known/openid-configuration"
//)
@OpenAPIDefinition(
        info = @Info(
                title = "Marketplace Loader Agent API",
                version = "v1",
                description = "Marketplace Loader Agent API. Exposes access to data importers and processors.",
                contact = @Contact(),
                license = @License(name = "MIT License", url = "https://opensource.org/license/mit/"),
                termsOfService = "https://example.com/terms"
        ),
//        security = {
//                @SecurityRequirement(name = "openid-connect", scopes = {"a"})
//        },
        servers = {
                @Server(
                        description = "Local development",
                        url = "http://localhost:8003/"
                ),
                @Server(
                        description = "Local kubernetes cluster deployment",
                        url = "http://loader-agent.marketplace.local:8888/"
                ),
                @Server(
                        description = "Production deployment",
                        url = "https://loader-agent.marketplace.bobnar.net/"
                ),
                @Server(
                        description = "Deployment on different url (https)",
                        url = "https://{url}/",
                        variables = {
                                @ServerVariable(
                                        name = "url",
                                        description = "Url that is used to connect to server",
                                        defaultValue = "loader-agent.marketplace.bobnar.net"
                                )
                        }
                ),
                @Server(
                        description = "Deployment on different url (http)",
                        url = "https://{url}/",
                        variables = {
                                @ServerVariable(
                                        name = "url",
                                        description = "Url that is used to connect to server",
                                        defaultValue = "localhost:8002"
                                )
                        }
                )
        }
)
//@DeclareRoles({"user", "admin"})
@ApplicationPath("v1")
public class LoaderAgentApplication extends Application {
}
