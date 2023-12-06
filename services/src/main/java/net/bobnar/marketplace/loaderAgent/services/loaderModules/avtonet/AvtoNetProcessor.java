package net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet;

import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessorBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.InvalidParameterException;
import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvtoNetProcessor extends ProcessorBase<Object, AvtoNetListItem> {
    @Override
    public ProcessItemResult<Object> processItem(String data) {
        ProcessItemResult<Object> result =  new ProcessItemResult<>();
        result.fail("Operation not implemented");

        return result;
    }

    @Override
    public ProcessListResult<AvtoNetListItem> processItemList(String data) {
        ProcessListResult<AvtoNetListItem> result = new ProcessListResult<>();

        Document page = Jsoup.parse(data);

        StringBuilder exceptionsBuilder = new StringBuilder();

        Elements resultRows = page.getElementsByClass("GO-Results-Row");
        for (Element resultRow : resultRows) {
            try {
                ProcessItemResult<AvtoNetListItem> itemResult = this.processListItem(resultRow.html());

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
    public ProcessItemResult<AvtoNetListItem> processListItem(String data) {
        ProcessItemResult<AvtoNetListItem> result = new ProcessItemResult<>();
        result.success();

        result.item = new AvtoNetListItem();

        Element itemRow = Jsoup.parse(data).root();

        String adLink = itemRow.getElementsByTag("a").first().attr("href");
        Matcher matcher = Pattern.compile("id=([0-9]+)&display=(.*)", Pattern.CASE_INSENSITIVE).matcher(adLink);
        matcher.find();
        result.item.id = Integer.parseInt(matcher.group(1));
        result.item.shortTitle = matcher.group(2).replace("%20", " ");

        result.item.title = itemRow.getElementsByClass("GO-Results-Naziv").first().getElementsByTag("span").first().text();

        result.item.photoPath = itemRow.getElementsByClass("GO-Results-Photo").first().getElementsByTag("img").first().attr("SRC");

        Elements dataRows = itemRow.getElementsByClass("GO-Results-Data-Top").first().getElementsByTag("tr");
        for (Element dataRow : dataRows) {
            Elements items = dataRow.getElementsByTag("td");
            String property = items.first().text();
            String value = "";
            if (items.size() > 1) {
                value = items.get(1).text();
            }

            if (property.equals("1.registracija")) {
                result.item.firstRegistrationYear = Integer.parseInt(value);
            } else if (property.equals("Prevoženih")) {
                result.item.drivenDistanceKm = Integer.parseInt(value.replace(" km", ""));
            } else if (property.equals("Gorivo")) {
                result.item.engineType = value;
            } else if (property.equals("Menjalnik")) {
                result.item.transmissionType = value;
            } else if (property.equals("Motor")) {
                result.item.engineParameters = value;
            } else if (property.equals("Starost")) {
                result.item.age = value;
            } else if (property.equals("Baterija") || property.endsWith("baterija")) {
                result.item.otherParameters.put(property, value);
            } else {
                throw new InvalidParameterException("Unknown parameter in ad listing: " + property + ", value: " + value);
            }
        }

        Element sellerNotesEl = itemRow.getElementsByClass("GO-bg-graylight").first();
        if (sellerNotesEl != null) {
            result.item.sellerNotes = sellerNotesEl.text();
        }


        Element priceEl = itemRow.getElementsByClass("GO-Results-PriceLogo").first();
        Element sellerEl = priceEl.getElementsByClass("GO-Results-Logo").first();
        if (sellerEl != null) {
            result.item.isDealer = true;
            Element sellerLinkEl = sellerEl.getElementsByTag("a").first();
            if (sellerLinkEl != null) {
                String sellerLink = sellerLinkEl.attr("HREF");
                Matcher matcher1 = Pattern.compile("broker=([0-9]+)&", Pattern.CASE_INSENSITIVE).matcher(sellerLink);
                matcher1.find();
                result.item.dealerId = Integer.parseInt(matcher1.group(1));
            } else {
                // Case of blank image as seller graphics - cannot determine dealer id.
            }
        }

        boolean priceProcessed = false;
        Element regularPriceEl = priceEl.getElementsByClass("Go-Results-Price-TXT-Regular").first();
        if (regularPriceEl != null) {
            result.item.regularPrice = regularPriceEl.text();
            priceProcessed = true;
        }
        Element actionPriceTextEl = priceEl.getElementsByClass("Go-Results-Price-Akcija-TXT").first();
        Element actionOldPriceEl = priceEl.getElementsByClass("Go-Results-Price-TXT-StaraCena").first();
        Element actionNewPriceEl = priceEl.getElementsByClass("Go-Results-Price-TXT-AkcijaCena").first();
        if (actionNewPriceEl != null) {
            //result.actionPrice = actionNewPriceEl.text();
            priceProcessed = true;
        }

        if (!priceProcessed) {
            result.fail("Price element does not exist");
        }

//        if (result.errors != null && result.errors.isEmpty()) {
//            result.success();
//        }

        return result;
    }
}
