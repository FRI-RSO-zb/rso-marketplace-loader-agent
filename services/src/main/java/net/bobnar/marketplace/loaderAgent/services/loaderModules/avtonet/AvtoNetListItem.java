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
}
