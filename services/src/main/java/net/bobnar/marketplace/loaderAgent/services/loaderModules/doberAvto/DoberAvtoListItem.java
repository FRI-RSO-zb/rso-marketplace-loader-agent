package net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto;

import net.bobnar.marketplace.loaderAgent.services.processor.IProcessedAdBriefData;
import org.json.JSONObject;

import java.util.HashMap;

public class DoberAvtoListItem implements IProcessedAdBriefData {
    private String id;
    private String title;
    private String brand;
    private String model;
    private String photoUrl;


    private String age;

    private int drivenDistanceKm;
    private String firstRegistrationDate;
    private String transmissionType;
    private int enginePowerKW;
    private int engineDisplacementCcm;
    private int totalOwners;
    private String engineType;


    private int priceEur;

    private boolean isDealer;
    private String dealerId;
    private String dealerInfo;

    @Override
    public String getOtherData() {
        JSONObject otherData = new JSONObject();

        otherData.put("age", getAge());
        otherData.put("drivenDistanceKm", getDrivenDistanceKm());
        otherData.put("firstRegistrationDate", getFirstRegistrationDate());
        otherData.put("transmissionType", getTransmissionType());
        otherData.put("enginePowerKW", getEnginePowerKW());
        otherData.put("engineDisplacementCcm", getEngineDisplacementCcm());
        otherData.put("totalOwners", getTotalOwners());
        otherData.put("engineType", getEngineType());
        otherData.put("priceEur", getPriceEur());
        otherData.put("isDealer", isDealer);
        otherData.put("dealerId", getDealerId());
        otherData.put("dealerInfo", getDealerInfo());

        return otherData.toString(2);
    }

    @Override
    public IProcessedAdBriefData toInterface() {
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public String getFirstRegistrationDate() {
        return firstRegistrationDate;
    }

    public void setFirstRegistrationDate(String firstRegistrationDate) {
        this.firstRegistrationDate = firstRegistrationDate;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public int getEnginePowerKW() {
        return enginePowerKW;
    }

    public void setEnginePowerKW(int enginePowerKW) {
        this.enginePowerKW = enginePowerKW;
    }

    public int getEngineDisplacementCcm() {
        return engineDisplacementCcm;
    }

    public void setEngineDisplacementCcm(int engineDisplacementCcm) {
        this.engineDisplacementCcm = engineDisplacementCcm;
    }

    public int getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(int totalOwners) {
        this.totalOwners = totalOwners;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public int getPriceEur() {
        return priceEur;
    }

    public void setPriceEur(int priceEur) {
        this.priceEur = priceEur;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerInfo() {
        return dealerInfo;
    }

    public void setDealerInfo(String dealerInfo) {
        this.dealerInfo = dealerInfo;
    }
}
