package net.bobnar.marketplace.loaderAgent.services;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders.LoadingResult;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiLoader;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonLoader;

import java.io.IOException;

public class LoaderHelper {
    public static LoadingResult loadLatestListFromSource(String source, boolean shouldUseInternalResources) {
        try {
            if ("doberavto".equals(source)) {
                return new DoberAvtoLoader(shouldUseInternalResources).loadLatestCarAds();
            } else if ("avtonet".equals(source)) {
                return new AvtoNetLoader(shouldUseInternalResources).loadAvtonetTop100List();
            } else if ("bolha".equals(source)) {
                return new BolhaLoader(shouldUseInternalResources).loadLatestCarAds();
            } else if ("oglasisi".equals(source)) {
                return new OglasiSiLoader(shouldUseInternalResources).loadLatestCarAds();
            } else if ("salomon".equals(source)) {
                return new SalomonLoader(shouldUseInternalResources).loadLatestCarAds();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
