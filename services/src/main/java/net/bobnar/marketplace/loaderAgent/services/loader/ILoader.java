package net.bobnar.marketplace.loaderAgent.services.loader;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface ILoader<T> {
    String getBaseUrl();

    String getUrl(String path);

    Document loadDocumentFromUrl(String path) throws IOException;
}
