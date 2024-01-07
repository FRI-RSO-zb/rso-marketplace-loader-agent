package net.bobnar.marketplace.loaderAgent.services;

import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.IProcessedAdBriefData;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;

import java.util.AbstractMap;

public class ProcessorHelper {
    public static ProcessListResult<IProcessedAdBriefData> processFromSource(String source, String data) {
        ProcessListResult<? extends IProcessedAdBriefData> result = null;
        if ("doberavto".equals(source)) {
            result = new DoberAvtoProcessor().processItemList(data);
        } else if ("avtonet".equals(source)) {
            result = new AvtoNetProcessor().processItemList(data);
        } else if ("bolha".equals(source)) {
            result = new BolhaProcessor().processItemList(data);
        } else if ("oglasisi".equals(source)) {
            result = new OglasiSiProcessor().processItemList(data);
        } else if ("salomon".equals(source)) {
            result = new SalomonProcessor().processItemList(data);
        }

        if (result != null) {
            return toGenericAdResult(result);
        }

        return null;
    }

    protected static ProcessListResult<IProcessedAdBriefData> toGenericAdResult(ProcessListResult<? extends IProcessedAdBriefData> value) {
        ProcessListResult<IProcessedAdBriefData> result = new ProcessListResult<>();
        result.status =  value.status;
        result.errors = value.errors;
        result.processedItems.addAll(value.processedItems);
        result.failedItems.addAll(value.failedItems.stream().map(x-> new AbstractMap.SimpleEntry<String, IProcessedAdBriefData>(x.getKey(), x.getValue())).toList());

        return result;
    }
}
