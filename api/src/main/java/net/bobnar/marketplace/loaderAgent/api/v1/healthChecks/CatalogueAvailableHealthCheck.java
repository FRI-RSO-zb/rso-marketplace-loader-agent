//package net.bobnar.marketplace.loaderAgent.healthChecks;
//
//import org.eclipse.microprofile.health.HealthCheck;
//import org.eclipse.microprofile.health.HealthCheckResponse;
//import org.eclipse.microprofile.health.Readiness;
//import org.json.JSONObject;
//
//import javax.faces.bean.ApplicationScoped;
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//@Readiness
//@ApplicationScoped
//public class CatalogueAvailableHealthCheck implements HealthCheck {
//    @Override
//    public HealthCheckResponse call() {
//        System.out.println("Running catalogue health check.");
//
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8082/health"))
//                .build();
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() != 200) {
//                return HealthCheckResponse.down(CatalogueAvailableHealthCheck.class.getSimpleName());
//            }
//
//            JSONObject o = new JSONObject(response.body());
//            if (o.get("status") == "UP") {
//                return HealthCheckResponse.up(CatalogueAvailableHealthCheck.class.getSimpleName());
//            }
//        } catch (IOException e) {
//            return HealthCheckResponse.down(CatalogueAvailableHealthCheck.class.getSimpleName());
//        } catch (InterruptedException e) {
//            return HealthCheckResponse.down(CatalogueAvailableHealthCheck.class.getSimpleName());
//        }
//
//        return HealthCheckResponse.down(CatalogueAvailableHealthCheck.class.getSimpleName());
//    }
//}
