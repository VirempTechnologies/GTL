package com.example.awais.gtl.Pojos;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by awais on 11/28/2017.
 */

public class CheckOutItem {
    String productName;
    String ProductModel;

    long collectivePrice;
    long salePrice;
    ArrayList<String> IMEINos=new ArrayList<>();



    public CheckOutItem() {
    }

    public String getProductModel() {
        return ProductModel;
    }

    public void setProductModel(String productModel) {
        ProductModel = productModel;
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
