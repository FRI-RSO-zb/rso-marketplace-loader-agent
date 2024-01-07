package net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha;

import net.bobnar.marketplace.loaderAgent.services.processor.IProcessedAdBriefData;
import org.json.JSONObject;

public class BolhaListItem implements IProcessedAdBriefData {
    public boolean isExposed;

    public String title;
    public int id;
    public String url;
    public String brand;
    public String model;

    public String photoPath;

    public String age;
    public int drivenDistanceKm = -1;
    public int manufacturingYear = -1;
    public String location;


    public boolean isDealer;


    public String publishDate;

    public String price;

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

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

    @Override
    public String getPhotoUrl() {
        return photoPath;
    }

    @Override
    public String getOtherData() {
        JSONObject otherData = new JSONObject();

        otherData.put("isExposed", isExposed);
        otherData.put("age", getAge());
        otherData.put("drivenDistanceKm", getDrivenDistanceKm());
        otherData.put("manufacturingYear", getManufacturingYear());
        otherData.put("location", getLocation());
        otherData.put("isDealer", isDealer());
        otherData.put("publishDate", getPublishDate());
        otherData.put("price", getPrice());

        return otherData.toString(2);
    }

    @Override
    public IProcessedAdBriefData toInterface() {
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getDrivenDistanceKm() {
        return drivenDistanceKm;
    }

    public void setDrivenDistanceKm(int drivenDistanceKm) {
        this.drivenDistanceKm = drivenDistanceKm;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
