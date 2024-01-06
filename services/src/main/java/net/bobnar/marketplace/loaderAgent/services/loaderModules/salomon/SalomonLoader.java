package net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SalomonLoader extends LoaderBase<Object> {
    public SalomonLoader(boolean useInternalResources) {
        super("http://oglasi.svet24.si/", useInternalResources);
    }

    public LoadingResult loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("oglasi/motorna-vozila/avtomobili", "samples/salomon-avtomobili_2.html");

        return new LoadingResult(true, result.html());
    }
}
