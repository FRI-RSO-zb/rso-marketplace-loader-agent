package net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class OglasiSiLoader extends LoaderBase<Object> {
    public OglasiSiLoader() {
        super("https://oglasi.si/");
    }

    public LoadingResult loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("avtomobili");

        System.out.println(result.html());

        return new LoadingResult(true, result.html());
    }
}
