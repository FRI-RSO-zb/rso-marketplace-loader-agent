package net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class BolhaLoader extends LoaderBase<Object> {
    public BolhaLoader() {
        super("https://www.bolha.com/");
    }

    public LoadingResult loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("avto-oglasi");

        System.out.println(result.html());

        return new LoadingResult(true, result.html());
    }
}
