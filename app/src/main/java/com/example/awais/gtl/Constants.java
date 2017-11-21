package com.example.awais.gtl;

import com.example.awais.gtl.Pojos.CartItem;

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
    public static String loginURL ="http://"+IPAddress+"/api/login";
    public static String getBagUrl ="http://"+IPAddress+"/api/my_bag";
    public static HashMap<String,CartItem> cartItemsMap= new HashMap<String, CartItem>();
}
