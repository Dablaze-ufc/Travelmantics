package com.dablaze.travelmanticsblaze;

import java.io.Serializable;

public class TravelDeal implements Serializable {
    private  String fId;
    private String fTittle;
    private String fPrice;
    private String fDescription;
    private String fImageUrl;

    public TravelDeal () {}



    public TravelDeal( String tittle, String price, String description, String imageUrl) {
        this.setid(fId);
        this.setTittle(tittle);
        this.setPrice(price);
        this.setDescription(description);
        this.setImageUrl(imageUrl);
    }

    public String getId() {
        return fId;
    }

    public void setid(String fid) {
        this.fId = fid;
    }

    public String getTittle() {
        return fTittle;
    }

    public void setTittle(String tittle) {
        fTittle = tittle;
    }

    public String getPrice() {
        return fPrice;
    }

    public void setPrice(String price) {
        fPrice = price;
    }

    public String getDescription() {
        return fDescription;
    }

    public void setDescription(String description) {
        fDescription = description;
    }

    public String getImageUrl() {
        return fImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        fImageUrl = imageUrl;
    }
}
