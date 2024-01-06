package net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class OglasiSiLoader extends LoaderBase<Object> {
    public OglasiSiLoader(boolean useInternalResources) {
        super("https://oglasi.si/", useInternalResources);
    }

    public LoadingResult loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("avtomobili", "samples/oglasi-si-avtomobili_2.html");

        return new LoadingResult(true, result.html());
    }
}
