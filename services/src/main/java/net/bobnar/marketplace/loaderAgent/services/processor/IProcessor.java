package net.bobnar.marketplace.loaderAgent.services.processor;

public interface IProcessor<TItem, TListItem> {
    ProcessItemResult<TItem> processItem(String data);
    ProcessListResult<TListItem> processItemList(String data);
    ProcessItemResult<TListItem> processListItem(String data);
}
