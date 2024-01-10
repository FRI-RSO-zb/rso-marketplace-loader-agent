package net.bobnar.marketplace.loaderAgent.api.v1.healthChecks;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Readiness
@ApplicationScoped
public class NetworkAvailableHealthCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        System.out.println("Running network availability health check.");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.doberavto.si"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return HealthCheckResponse.down(NetworkAvailableHealthCheck.class.getSimpleName());
            }
        } catch (IOException e) {
            return HealthCheckResponse.down(NetworkAvailableHealthCheck.class.getSimpleName());
        } catch (InterruptedException e) {
            return HealthCheckResponse.down(NetworkAvailableHealthCheck.class.getSimpleName());
        }

        return HealthCheckResponse.down(NetworkAvailableHealthCheck.class.getSimpleName());
    }
}
