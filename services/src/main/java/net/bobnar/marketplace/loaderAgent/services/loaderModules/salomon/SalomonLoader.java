package net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SalomonLoader extends LoaderBase<Object> {
    public SalomonLoader() {
        super("http://oglasi.svet24.si/");
    }

    public LoadingResult loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("oglasi/motorna-vozila/avtomobili");

        System.out.println(result.html());

        return new LoadingResult(true, result.html());
    }
}
