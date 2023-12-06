package net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto;

import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;

import java.io.IOException;

public class DoberAvtoLoader extends LoaderBase<Object> {
    public DoberAvtoLoader() {
        super("https://www.doberavto.si/");
    }

    public Object loadLatestCarAds() throws IOException {
        String result = this.loadJsonFromUrl("internal-api/v1/marketplace/search?&&&&&&&&&&&&&&results=62&from=0&sort=published.down&includeSold=true",
                "https://www.doberavto.si/iskanje?sort=published.down&page=0");

        System.out.println(result);

        return result;
    }

}
