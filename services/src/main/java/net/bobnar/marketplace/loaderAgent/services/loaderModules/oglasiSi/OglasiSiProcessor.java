package net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi;

import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessorBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.AbstractMap;

public class OglasiSiProcessor extends ProcessorBase<Object, OglasiSiListItem> {

    @Override
    public ProcessItemResult<Object> processItem(String data) {
        return null;
    }

    @Override
    public ProcessListResult<OglasiSiListItem> processItemList(String data) {
        ProcessListResult<OglasiSiListItem> result = new ProcessListResult<>();
        result.success();

        Document page = Jsoup.parse(data);

        Elements resultRows = page.getElementById("result").getElementsByClass("post-results");
        for (Element resultRow : resultRows) {
            try {
                ProcessItemResult<OglasiSiListItem> itemResult = this.processListItem(resultRow.html());

                if (itemResult.isFailed()) {
                    result.failedItems.add(new AbstractMap.SimpleEntry<>(resultRow.html(), itemResult.item));
                    result.addError(result.errors + "\n" + resultRow.html() + "\n\n");
                } else {
                    result.processedItems.add(itemResult.item);
                }
            } catch (Exception e) {
                result.addError("\n" + resultRow.html() + "\n"/* + ExceptionUtils.readStackTrace(e) + "\n\n"*/);
                result.failedItems.add(new AbstractMap.SimpleEntry<>(resultRow.html(), null));
            }
        }

        return result;
    }

    @Override
    public ProcessItemResult<OglasiSiListItem> processListItem(String data) {
        ProcessItemResult<OglasiSiListItem> result = new ProcessItemResult<>();
        result.success();

        Document row = Jsoup.parse(data);

        result.item = new OglasiSiListItem();

        result.item.url = row.getElementsByTag("a").first().attr("href");
        result.item.id = result.item.url.substring(result.item.url.lastIndexOf('/') + 1);
        result.item.photoUrl = row.getElementsByTag("img").first().attr("src");
        result.item.title = row.getElementsByTag("h3").first().text();
        result.item.price = row.getElementsByClass("price").first().text();
        if (!row.getElementsByClass("props").first().getElementsByClass("fa-info-circle").isEmpty())
            result.item.sellerNotes = row.getElementsByClass("props").first().getElementsByClass("fa-info-circle").first().parent().text();
        if (!row.getElementsByClass("props").first().getElementsByClass("fa-map-marker-alt").isEmpty())
            result.item.location = row.getElementsByClass("props").first().getElementsByClass("fa-map-marker-alt").first().parent().text();
        result.item.lastUpdated = row.getElementsByClass("lower").first().getElementsByTag("p").first().text();

        return result;
    }
}
