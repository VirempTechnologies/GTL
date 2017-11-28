package com.example.awais.gtl.Pojos;

import java.util.ArrayList;

/**
 * Created by awais on 11/28/2017.
 */

public class CheckOutItem {
    String productName;
    long collectivePrice;
    long salePrice;
    ArrayList<String> IMEINos=new ArrayList<>();

    public CheckOutItem() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getCollectivePrice() {
        return collectivePrice;
    }

    public void setCollectivePrice(long collectivePrice) {
        this.collectivePrice = collectivePrice;
    }

    public long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(long salePrice) {
        this.salePrice = salePrice;
    }

    public ArrayList<String> getIMEINos() {
        return IMEINos;
    }

    public void setIMEINos(ArrayList<String> IMEINos) {
        this.IMEINos = IMEINos;
    }
}
