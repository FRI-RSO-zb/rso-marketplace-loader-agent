package net.bobnar.marketplace.loaderAgent.services.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public abstract class MarketplaceHttpService {
    protected <TResponse> TResponse getFromUrl(String path, Class<TResponse> responseType) {
        HttpClient client = null;
        try {
            client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(getServiceRootUrl() + path))
                    .timeout(Duration.of(2, ChronoUnit.SECONDS))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() > 299) {
                throw new RuntimeException("Status not in 2xx range: " + response.statusCode() + ". " + response.body());
            }

            ObjectMapper mapper = getObjectMapper();
            TResponse responseValue = mapper.readValue(response.body(), responseType);

            return responseValue;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            if (client != null) {
//                client.close();
//            }
        }
    }

    protected <TRequest, TResponse> TResponse postToUrl(String path, TRequest data, Class<TResponse> responseType) {
        HttpClient client = null;
        try {
            client = HttpClient.newHttpClient();
            ObjectMapper mapper = getObjectMapper();
            String jsonData = mapper.writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(getServiceRootUrl() + path))
                    .timeout(Duration.of(2, ChronoUnit.SECONDS))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() > 299) {
                throw new RuntimeException("Status not in 2xx range: " + response.statusCode() + ". " + response.body());
            }

            TResponse responseValue = mapper.readValue(response.body(), responseType);

            return responseValue;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            if (client != null) {
//                client.close();
//            }
        }
    }

    protected <TRequest, TResponse> TResponse putToUrl(String path, TRequest data, Class<TResponse> responseType) {
        HttpClient client = null;
        try {
            client = HttpClient.newHttpClient();
            ObjectMapper mapper = getObjectMapper();
            String jsonData = mapper.writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(getServiceRootUrl() + path))
                    .timeout(Duration.of(2, ChronoUnit.SECONDS))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonData))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() > 299) {
                throw new RuntimeException("Status not in 2xx range: " + response.statusCode() + ". " + response.body());
            }

            TResponse responseValue = mapper.readValue(response.body(), responseType);

            return responseValue;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            if (client != null) {
//                client.close();
//            }
        }
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    protected abstract String getServiceRootUrl();
}
