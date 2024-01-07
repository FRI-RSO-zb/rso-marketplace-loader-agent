package net.bobnar.marketplace.loaderAgent.services.catalog;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;

import javax.enterprise.context.RequestScoped;
import java.util.List;


@RequestScoped
public class AdsService extends MarketplaceHttpService {

    public Ad[] getAdsWithIds(List<Integer> ids) {
        return getFromUrl("/ads?" + String.join("&", ids.stream().map(x->"ids="+x).toList()), Ad[].class);
    }

    public Ad[] getAdsWithSourceIds(String source, List<String> sourceIds) {
        return getFromUrl("/ads?sources=" + source + "&" + String.join("&", sourceIds.stream().map(x->"sourceIds="+x).toList()), Ad[].class);
    }

    public Ad[] createAds(Ad[] ads) {
        return postToUrl("/ads", ads, Ad[].class);
    }

    @Override
    protected String getServiceRootUrl() {
        return "http://localhost:8801/v1";
    }
}
