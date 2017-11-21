package com.example.awais.gtl.Pojos;

/**
 * Created by awais on 11/21/2017.
 */

public class CartItem {
    int product_id;
    String product_name;
    long sale_price;
    int quantity;
    long collective_price;

    public CartItem() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getCollective_price() {
        return collective_price;
    }

    public void setCollective_price(long collective_price) {
        this.collective_price = collective_price;
    }
}
