package net.bobnar.marketplace.loaderAgent.services.loader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public abstract class LoaderBase<T> implements ILoader<T> {
    private final String baseUrl;
    protected boolean useInternalResources = true;

    public LoaderBase(String baseUrl, boolean useInternalResources) {
        this.baseUrl = baseUrl;
        this.useInternalResources = useInternalResources;
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
    public Document loadDocumentFromUrl(String path, String internalName) throws IOException {
        if (this.useInternalResources) {
            return Jsoup.parse(this.loadFromInternalResource(internalName));
        }

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

    public String loadJsonFromUrl(String path, String referer, String internalName) throws IOException {
        if (this.useInternalResources) {
            return this.loadFromInternalResource(internalName);
        }

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

    public String loadFromInternalResource(String name) throws IOException {
        ClassLoader classLoader = LoaderBase.class.getClassLoader();
        InputStream fis = classLoader.getResourceAsStream(name);
//        FileInputStream fis = new FileInputStream("src/main/resources/samples/" + name);
        if (fis == null) {
            throw new RuntimeException("Internal resource " + name + " does not exist. ClassLoader: " + classLoader);
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        return data;
    }
}
