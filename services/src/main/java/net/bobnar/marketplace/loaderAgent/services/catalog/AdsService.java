package net.bobnar.marketplace.loaderAgent.services.catalog;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;


@RequestScoped
public class AdsService extends MarketplaceHttpService {
    private Logger log = Logger.getLogger(AdsService.class.getName());

    public Ad[] getAdsWithIds(List<Integer> ids) {
        return getFromUrl("/ads?" + String.join("&", ids.stream().map(x->"ids="+x).toList()), Ad[].class);
    }

    public Ad[] getAdsWithSourceIds(String source, List<String> sourceIds) {
        return getFromUrl("/ads?sources=" + source + "&" + String.join("&", sourceIds.stream().map(x->"sourceIds="+x).toList()), Ad[].class);
    }

    public Ad[] createAds(Ad[] ads) {
        return postToUrl("/ads", ads, Ad[].class);
    }

    @Inject
    private ServiceConfig serviceConfig;

    @Override
    protected String getServiceRootUrl() {
        return serviceConfig.getCatalogServiceUrl();
    }
}
