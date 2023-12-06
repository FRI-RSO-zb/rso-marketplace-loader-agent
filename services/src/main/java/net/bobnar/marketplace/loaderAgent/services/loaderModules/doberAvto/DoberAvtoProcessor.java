package net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto;

import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessorBase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;

public class DoberAvtoProcessor extends ProcessorBase<Object, DoberAvtoListItem> {
    @Override
    public ProcessItemResult<Object> processItem(String data) {
        return null;
    }

    @Override
    public ProcessListResult<DoberAvtoListItem> processItemList(String data) {
        ProcessListResult<DoberAvtoListItem> result = new ProcessListResult<>();
        result.success();

        JSONObject input = new JSONObject(data);
        JSONArray items = input.getJSONArray("results");

        for (Object item : items) {
            ProcessItemResult<DoberAvtoListItem> itemResult = this.processListItem((JSONObject) item);

            if (itemResult.isSuccess()) {
                result.processedItems.add(itemResult.item);
            } else {
                result.failedItems.add(new AbstractMap.SimpleEntry<>(((JSONObject) item).toString(), itemResult.item));
                result.addError(itemResult.errors);
            }
        }

        return result;
    }

    @Override
    public ProcessItemResult<DoberAvtoListItem> processListItem(String data) {
        var item = new JSONObject(data);

        return this.processListItem(item);
    }

    private ProcessItemResult<DoberAvtoListItem> processListItem(JSONObject item) {
        ProcessItemResult<DoberAvtoListItem> result = new ProcessItemResult<>();
        result.success();

        result.item = new DoberAvtoListItem();
        result.item.id = item.getString("postId");
        result.item.title = item.getString("modelName");
        result.item.manufacturer = item.getString("manufacturer");
        result.item.photoPath = item.getString("imageUrl");
        result.item.firstRegistrationDate = item.has("registrationDate") ? item.getString("registrationDate") : null;
        result.item.age = "USED".equals(item.getString("historySource")) ? "Rabljeno" : item.getString("historySource");
        result.item.drivenDistanceKm = item.has("odometer") ? item.getInt("odometer") : -1;
        if ("A".equals(item.getString("transmission"))) {
            result.item.transmissionType = "Avtomatski menjalnik";
        } else if ("M".equals(item.getString("transmission"))) {
            result.item.transmissionType = "Rocni menjalnik";
        } else {
            result.addError("Unsupported transmission: " + item.getString("transmission"));
        }
        result.item.enginePowerKW = item.getInt("enginePower");
        result.item.engineDisplacementCcm = item.has("engineDisplacement") ? item.getInt("engineDisplacement") : -1;
        result.item.totalOwners = item.has("owners") ? item.getInt("owners") : -1;

        switch (item.getString("fuelType")) {
            case "PETROL":
                result.item.engineType = "Bencin";
                break;
            case "DIESEL":
                result.item.engineType = "Dizel";
                break;
            case "HYBRID":
                result.item.engineType = "Hibridni pogon";
                break;
            case "ELECTRIC":
                result.item.engineType = "Elektricni pogon";
                break;
            default:
                result.addError("Unsupported fuelType: " + item.getString("fuelType"));
        }

        result.item.priceEur = item.getInt("price");

        return result;
    }


    private String getStringOrSetError(JSONObject value, String propertyName, ProcessItemResult<DoberAvtoListItem> result) {
        if (value.has(propertyName)) {
            return value.getString(propertyName);
        }

        result.addError("Property '" + propertyName + "' not found");

        return null;
    }

    private int getIntOrSetError(JSONObject value, String propertyName, ProcessItemResult<DoberAvtoListItem> result) {
        if (value.has(propertyName)) {
            return value.getInt(propertyName);
        }

        result.addError("Property '" + propertyName + "' not found");

        return -1;
    }
}
