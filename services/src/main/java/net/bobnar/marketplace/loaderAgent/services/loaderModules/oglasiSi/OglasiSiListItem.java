package net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi;

import net.bobnar.marketplace.loaderAgent.services.processor.IProcessedAdBriefData;
import org.json.JSONObject;

public class OglasiSiListItem implements IProcessedAdBriefData {
    public String id;
    public String url;
    private String brand;
    private String model;
    public String photoUrl;

    public String title;
    public String price;
    public String sellerNotes;
    public String location;
    public String lastUpdated;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String getOtherData() {
        JSONObject otherData = new JSONObject();

        otherData.put("url", getUrl());
        otherData.put("price", getPrice());
        otherData.put("sellerNotes", getSellerNotes());
        otherData.put("location", getLocation());
        otherData.put("lastUpdated", getLastUpdated());

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
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
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

    public String getSellerNotes() {
        return sellerNotes;
    }

    public void setSellerNotes(String sellerNotes) {
        this.sellerNotes = sellerNotes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
