package net.bobnar.marketplace.loaderAgent.services.processor;

public interface IProcessedAdBriefData {
    String getId();
    String getTitle();
    String getBrand();
    void setBrand(String brand);
    String getModel();
    void setModel(String model);
    String getPhotoUrl();
    String getOtherData();

    IProcessedAdBriefData toInterface();
}
