package com.example.awais.gtl.Pojos;

/**
 * Created by awais on 11/22/2017.
 */

public class Client {
    int client_id ;
    String client_name;
    String company_name;
    String current_bal;

    public Client() {
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCurrent_bal() {
        return current_bal;
    }

    public void setCurrent_bal(String current_bal) {
        this.current_bal = current_bal;
    }
}
