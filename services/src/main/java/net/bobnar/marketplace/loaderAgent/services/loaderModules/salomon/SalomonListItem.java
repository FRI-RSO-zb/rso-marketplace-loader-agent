package net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon;

import net.bobnar.marketplace.loaderAgent.services.processor.IProcessedAdBriefData;
import org.json.JSONObject;

public class SalomonListItem implements IProcessedAdBriefData {
    public String url;
    public String id;
    public String brand;
    public String model;
    public String photoUrl;
    public String title;
    public String price;
    public String description;
    public String adType; /// Selling or buying

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getOtherData() {
        JSONObject otherData = new JSONObject();

        otherData.put("url", getUrl());
        otherData.put("price", getPrice());
        otherData.put("description", getDescription());
        otherData.put("adType", getAdType());

        return otherData.toString(2);
    }

    @Override
    public IProcessedAdBriefData toInterface() {
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getModel() {
        return model;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }
}
