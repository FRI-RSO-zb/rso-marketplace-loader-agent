package net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon;

import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessorBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.AbstractMap;

public class SalomonProcessor extends ProcessorBase<Object, SalomonListItem> {

    @Override
    public ProcessItemResult<Object> processItem(String data) {
        return null;
    }

    @Override
    public ProcessListResult<SalomonListItem> processItemList(String data) {
        ProcessListResult<SalomonListItem> result = new ProcessListResult<>();
        result.success();

        Document page = Jsoup.parse(data);

        Elements resultRows = page.getElementById("advertList").getElementsByTag("article");
        for (Element resultRow : resultRows) {
            try {
                ProcessItemResult<SalomonListItem> itemResult = this.processListItem(resultRow.html());

                if (itemResult.isFailed()) {
                    if ("Ad does not have any link element".equals(itemResult.errors)) {
                        // Discard.
                        continue;
                    }
                    result.failedItems.add(new AbstractMap.SimpleEntry<>(resultRow.html(), itemResult.item));
                    result.addError(result.errors + "\n" + resultRow.html() + "\n\n");
                } else {
                    boolean itemAlreadyParsed = false;
                    for (var r : result.processedItems) {
                        if (r.id.equals(itemResult.item.id)) {
                            itemAlreadyParsed = true;
                            break;
                        }
                    }

                    if (!itemAlreadyParsed) {
                        result.processedItems.add(itemResult.item);
                    }
                }
            } catch (Exception e) {
                result.addError("\n" + resultRow.html() + "\n" /*+ ExceptionUtils.readStackTrace(e) + "\n\n"*/);
                result.failedItems.add(new AbstractMap.SimpleEntry<>(resultRow.html(), null));
            }
        }

        return result;
    }

    @Override
    public ProcessItemResult<SalomonListItem> processListItem(String data) {
        ProcessItemResult<SalomonListItem> result = new ProcessItemResult<>();
        result.success();

        Document row = Jsoup.parse(data);

        result.item = new SalomonListItem();

        if (row.getElementsByTag("a").isEmpty()) {
            result.fail("Ad does not have any link element");
            return result;
        }

        result.item.url = row.getElementsByTag("a").first().attr("href");
        result.item.id = result.item.url.substring(result.item.url.lastIndexOf('/') + 1);
        result.item.photoUrl = row.getElementsByTag("img").first().attr("src");
        result.item.title = row.getElementsByClass("title").first().getElementsByTag("a").text();
        result.item.price = row.getElementsByClass("price").first().text();
        result.item.description = row.getElementsByClass("desc").first().text();
        result.item.adType = row.getElementsByClass("label").first().text();
        if (!"Prodam".equals(result.item.adType) && !"Kupim".equals(result.item.adType)) {
            result.addError("Invalid ad type " + result.item.adType);
        }

        return result;
    }
}
