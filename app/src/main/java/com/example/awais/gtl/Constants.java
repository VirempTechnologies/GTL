package com.example.awais.gtl;

import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.Pojos.City;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by awais on 15/05/2017.
 */
public class Constants {
    public static String  TAG = "CheckLog";
    public static int  bagItemHeight = 0;
    public static String IPAddress= "85.25.217.41:8080";
//    public static String IPAddress= "192.168.100.6:8000";
    public static String loginURL ="http://"+IPAddress+"/api/login";
    public static String getBagUrl ="http://"+IPAddress+"/api/my_bag";
    public static String getClientsUrl ="http://"+IPAddress+"/api/my_clients";
    public static String getCheckOutUrl ="http://"+IPAddress+"/api/store_sale";
    public static String getCitiesUrl ="http://"+IPAddress+"/api/cities_list";
    public static String getAddClientUrl="http://"+IPAddress+"/api/store_client";
    public static String getAllSalesUrl="http://"+IPAddress+"/api/my_sale";
    public static String getSaleDetailUrl="http://"+IPAddress+"/api/sale";
    public static String getDateSaleUrl="http://"+IPAddress+"/api/date_invoice";
    public static String getSaleHistoryUrl="http://"+IPAddress+"/api/client_sale_history";
    public static String getClientLedgerUrl="http://"+IPAddress+"/api/user_ledger";
    public static HashMap<String,CartItem> cartItemsMap= new HashMap<String, CartItem>();
    public  static ArrayList<BagProduct> bagProductArrayList = new ArrayList<>();
    public  static ArrayList<City> allCities= new ArrayList<>();
    public  static HashMap<String,Integer> allCitiesMap= new HashMap<>();

    public static boolean gotoClient=false;
}
