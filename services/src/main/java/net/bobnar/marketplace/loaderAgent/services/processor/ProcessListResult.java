package net.bobnar.marketplace.loaderAgent.services.processor;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class ProcessListResult<TListItem> extends ProcessResult {
    public List<TListItem> processedItems = new ArrayList<>();
    public List<AbstractMap.SimpleEntry<String, TListItem>> failedItems = new ArrayList<>();
}
