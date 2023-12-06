package net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto;

public class DoberAvtoListItem {
    public String id;
    public String title;
    public String manufacturer;

    public String photoPath;

    public String age;

    public int drivenDistanceKm;
    public String firstRegistrationDate;
    public String transmissionType;
    public int enginePowerKW;
    public int engineDisplacementCcm;
    public int totalOwners;
    public String engineType;


    public int priceEur;

    public boolean isDealer;
    public String dealerId;
    public String dealerInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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
