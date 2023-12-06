package net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet;

import java.util.*;

public class AvtoNetListItem {
    public String title;
    public int id;
    public String shortTitle;

    public String photoPath;

    public int firstRegistrationYear = -1;
    public int drivenDistanceKm = -1;
    public String engineType;
    public String transmissionType;
    public String engineParameters;
    public String age;
    public Map<String, String> otherParameters = new HashMap<String, String>();

    public String sellerNotes;

    public boolean isDealer;
    public int dealerId = -1;

    public String regularPrice;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getFirstRegistrationYear() {
        return firstRegistrationYear;
    }

    public void setFirstRegistrationYear(int firstRegistrationYear) {
        this.firstRegistrationYear = firstRegistrationYear;
    }

    public int getDrivenDistanceKm() {
        return drivenDistanceKm;
    }

    public void setDrivenDistanceKm(int drivenDistanceKm) {
        this.drivenDistanceKm = drivenDistanceKm;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getEngineParameters() {
        return engineParameters;
    }

    public void setEngineParameters(String engineParameters) {
        this.engineParameters = engineParameters;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Map<String, String> getOtherParameters() {
        return otherParameters;
    }

    public void setOtherParameters(Map<String, String> otherParameters) {
        this.otherParameters = otherParameters;
    }

    public String getSellerNotes() {
        return sellerNotes;
    }

    public void setSellerNotes(String sellerNotes) {
        this.sellerNotes = sellerNotes;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }
}
