package net.bobnar.marketplace.loaderAgent.services.catalog;

import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;

import javax.enterprise.context.RequestScoped;


@RequestScoped
public class BrandsService extends MarketplaceHttpService {
    public CarBrand[] getCompleteBrandsList() {
        return getFromUrl("/brands?limit=200", CarBrand[].class);
    }

    public CarBrand getBrand(Integer id) {
        return getFromUrl("/brands/" + id, CarBrand.class);
    }

    @Override
    protected String getServiceRootUrl() {
        return "http://localhost:8801/v1";
    }
}
