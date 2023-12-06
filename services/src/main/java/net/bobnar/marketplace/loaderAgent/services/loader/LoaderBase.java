package net.bobnar.marketplace.loaderAgent.services.loader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class LoaderBase<T> implements ILoader<T> {
    private final String baseUrl;

    public LoaderBase(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getBaseUrl() {
        return this.baseUrl;
    }

    @Override
    public String getUrl(String path) {
        return this.baseUrl  + path;
    }

    @Override
    public Document loadDocumentFromUrl(String path) throws IOException {
        Document result = Jsoup.connect(this.getUrl(path))
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/119.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .get();

        return result;
    }

    public String loadJsonFromUrl(String path, String referer) throws IOException {
        String result = Jsoup.connect(this.getUrl(path))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/119.0")
                .header("Accept", "*/*")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .header("Referer", referer)
                .ignoreContentType(true)
                .execute()
                .body();

        return result;
    }
}
