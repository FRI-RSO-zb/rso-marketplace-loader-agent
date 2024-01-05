package net.bobnar.marketplace.loaderAgent.services.processor;

import net.bobnar.marketplace.common.dtos.loaderAgent.v1.processors.ProcessingResult;
import org.json.JSONArray;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class ProcessListResult<TListItem> extends ProcessResult {
    public List<TListItem> processedItems = new ArrayList<>();
    public List<AbstractMap.SimpleEntry<String, TListItem>> failedItems = new ArrayList<>();

    public ProcessingResult toDto() {
        return new ProcessingResult(
                this.isSuccess(),
                errors + (!failedItems.isEmpty() ? "\n\nFailed items: " + new JSONArray(failedItems) : ""),
                processedItems
        );
    }
}
