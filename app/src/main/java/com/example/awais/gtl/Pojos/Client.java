package com.example.awais.gtl.Pojos;

import java.io.Serializable;

/**
 * Created by awais on 11/22/2017.
 */


public class Client implements Serializable {
    int client_id ;
    String client_name;
    String first_name;
    String last_name;
    String company_name;
    String current_bal;
    int parent_id;
    String mobile_no;
    String land_line_no;
    String address;
    int city_id;
    String postal_code;
    String status;
    String email;
    String username;
    String password;
    String password_confirmation;
    String tax_no;
    String hrb_no;
    String start_bal;
    String comment;
    String user_type;





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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getLand_line_no() {
        return land_line_no;
    }

    public void setLand_line_no(String land_line_no) {
        this.land_line_no = land_line_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getTax_no() {
        return tax_no;
    }

    public void setTax_no(String tax_no) {
        this.tax_no = tax_no;
    }

    public String getHrb_no() {
        return hrb_no;
    }

    public void setHrb_no(String hrb_no) {
        this.hrb_no = hrb_no;
    }

    public String getStart_bal() {
        return start_bal;
    }

    public void setStart_bal(String start_bal) {
        this.start_bal = start_bal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
