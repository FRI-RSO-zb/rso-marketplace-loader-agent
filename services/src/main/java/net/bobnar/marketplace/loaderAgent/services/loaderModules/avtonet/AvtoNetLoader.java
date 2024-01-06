package net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;
import org.jsoup.nodes.Document;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;

@RequestScoped
public class AvtoNetLoader extends LoaderBase<Object> {
    public AvtoNetLoader(boolean useInternalResources) {
        super("https://www.avto.net/", useInternalResources);
    }

    public LoadingResult loadAvtonetTop100List() throws IOException {
        Document result = this.loadDocumentFromUrl("Ads/results_100.asp?oglasrubrika=1&prodajalec=2", "samples/avtonet-top100-list_2.html");

        return new LoadingResult(true, result.html());
    }
}
