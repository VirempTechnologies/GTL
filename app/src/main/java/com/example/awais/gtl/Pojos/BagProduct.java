package com.example.awais.gtl.Pojos;

/**
 * Created by awais on 11/15/2017.
 */

public class BagProduct {
    private String productName;
    private int myStock;
    private int companyStock;
    private long salePrice;
    private String image;

    public BagProduct() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getMyStock() {
        return myStock;
    }

    public void setMyStock(int myStock) {
        this.myStock = myStock;
    }

    public int getCompanyStock() {
        return companyStock;
    }

    public void setCompanyStock(int companyStock) {
        this.companyStock = companyStock;
    }

    public long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(long salePrice) {
        this.salePrice = salePrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}