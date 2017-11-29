package com.example.awais.gtl.Pojos;

import java.util.ArrayList;

/**
 * Created by awais on 11/20/2017.
 */

public class Sale {
    String sale_date;
    ArrayList<Receipt> sale_receipts;

    public Sale() {
    }

    public String getSale_date() {
        return sale_date;
    }

    public void setSale_date(String sale_date) {
        this.sale_date = sale_date;
    }

    public ArrayList<Receipt> getSale_receipts() {
        return sale_receipts;
    }

    public void setSale_receipts(ArrayList<Receipt> sale_receipts) {
        this.sale_receipts = sale_receipts;
    }
}

