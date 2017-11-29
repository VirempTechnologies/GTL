package com.example.awais.gtl.Pojos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by awais on 11/28/2017.
 */

public class SoldItem {
    int product_id;
    String product_name;
    long sale_price;
    String imei;

    public SoldItem() {
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public long getSale_price() {
        return sale_price;
    }

    public void setSale_price(long sale_price) {
        this.sale_price = sale_price;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
