package net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha;

import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessorBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.enterprise.context.RequestScoped;
import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class BolhaProcessor extends ProcessorBase<Object, BolhaListItem> {
    @Override
    public ProcessItemResult<Object> processItem(String data) {
        return null;
    }

    @Override
    public ProcessListResult<BolhaListItem> processItemList(String data) {
        ProcessListResult<BolhaListItem> result = new ProcessListResult<>();
        result.success();

        Document page = Jsoup.parse(data);

        StringBuilder exceptionsBuilder = new StringBuilder();

        Elements resultRows = page.getElementsByClass("content-main").first().getElementsByClass("EntityList-item");
        for (Element resultRow : resultRows) {
            try {
                if (resultRow.hasClass("EntityList-item--FeaturedStore") || resultRow.hasClass("BannerAlignment")) {
                    continue;
                }

                ProcessItemResult<BolhaListItem> itemResult = this.processListItem(resultRow.html());

                if (itemResult.isFailed()) {
                    result.failedItems.add(new AbstractMap.SimpleEntry<>(resultRow.html(), itemResult.item));
                    exceptionsBuilder.append(result.errors).append("\n");
                    exceptionsBuilder.append(resultRow.html()).append("\n\n");
                } else {
                    result.processedItems.add(itemResult.item);
                }
            } catch (Exception e) {
                exceptionsBuilder.append(e).append("\n");
                exceptionsBuilder.append(resultRow.html()).append("\n");
//                exceptionsBuilder.append(ExceptionUtils.readStackTrace(e)).append("\n\n");

                result.failedItems.add(new AbstractMap.SimpleEntry<>(resultRow.html(), null));
            }
        }

        String exceptions = exceptionsBuilder.toString();
        result.errors = exceptions;

        if (exceptions.isEmpty()) {
            result.success();
        } else {
            result.fail();
        }

        return result;
    }

    @Override
    public ProcessItemResult<BolhaListItem> processListItem(String data) {
        ProcessItemResult<BolhaListItem> result = new ProcessItemResult<>();
        result.success();

        Document row = Jsoup.parse(data);

        result.item = new BolhaListItem();

        result.item.isExposed = row.hasClass("EntityList-item--VauVau");

        result.item.id = Integer.parseInt(row.getElementsByClass("entity-title").first().getElementsByTag("a").attr("name"));
        result.item.title = row.getElementsByClass("entity-title").first().text();
        result.item.url = row.attr("data-href");

        result.item.photoPath = row.getElementsByClass("entity-thumbnail").first().getElementsByTag("img").first().attr("src");

        String descriptionText = row.getElementsByClass("entity-description").first().text();

        Matcher matcher = Pattern.compile("(.+), ([0-9]+) km Leto izdelave: ([0-9]+). Lokacija vozila: ([A-zÀ-ž ]+), ([A-zÀ-ž ]+)", Pattern.CASE_INSENSITIVE).matcher(descriptionText);
        matcher.find();
        if (matcher.group(1).equals("Rabljeno vozilo")) {
            result.item.age = "Rabljeno vozilo";
        } else {
            result.fail("Invalid age" + descriptionText);
        }
        result.item.drivenDistanceKm = Integer.parseInt(matcher.group(2));
        result.item.manufacturingYear = Integer.parseInt(matcher.group(3));
        result.item.location = matcher.group(4) + ", " + matcher.group(5);

        result.item.publishDate = row.getElementsByClass("date").first().attr("datetime");

        result.item.price = row.getElementsByClass("entity-prices").first().text();

        return result;
    }
}
