package net.bobnar.marketplace.loaderAgent.services.catalog;

import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.loaderAgent.services.config.ServiceConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.logging.Logger;


@RequestScoped
public class BrandsService extends MarketplaceHttpService {
    private Logger log = Logger.getLogger(BrandsService.class.getName());

    public CarBrand[] getCompleteBrandsList() {
        return getFromUrl("/brands?limit=200", CarBrand[].class);
    }

    public CarBrand getBrand(Integer id) {
        return getFromUrl("/brands/" + id, CarBrand.class);
    }

    @Inject
    private ServiceConfig serviceConfig;

    @Override
    protected String getServiceRootUrl() {
        return serviceConfig.getCatalogServiceUrl();
    }
}
