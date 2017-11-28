package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.CheckOutItemsAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.CheckOutItem;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.Pojos.SoldItem;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by awais on 11/28/2017.
 */

public class CheckOutActivity extends AppCompatActivity
{
    CheckOutItemsAdapter adapter;
    RecyclerView checkout_product_recycler_view;
    ArrayList<CheckOutItem> checkOutItemsArrayList;
    long total=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_activity);

        try {


            final Client client = (Client) getIntent().getSerializableExtra("client");
            ((TextView) findViewById(R.id.client_name)).setText(client.getClient_name());
            ((TextView) findViewById(R.id.client_current_balance)).setText(client.getCurrent_bal());
            ((TextView) findViewById(R.id.client_company_name)).setText(client.getCompany_name());
//            String string = "{\n" +
//                    "    \"imei_list\": [\n" +
//                    "        {\n" +
//                    "            \"product_id\": 2,\n" +
//                    "            \"sale_price\": 170,\n" +
//                    "            \"imei\": \"352619090969784\",\n" +
//                    "            \"product_name\": \"Glaxy S6\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"product_id\": 2,\n" +
//                    "            \"imei\": \"352619090969636\",\n" +
//                    "            \"sale_price\": 170,\n" +
//                    "            \"product_name\": \"Glaxy S6\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"product_id\": 1,\n" +
//                    "            \"imei\": \"352619090592966\",\n" +
//                    "            \"sale_price\": 50,\n" +
//                    "            \"product_name\": \"Nokia\"\n" +
//                    "        }\n" +
//                    "    ],\n" +
//                    "    \"success\": \"Sale created successfully.\",\n" +
//                    "    \"status_code\": \"200\"\n" +
//                    "}";

            JSONObject respObject = new JSONObject(getIntent().getStringExtra("respObject"));
//            JSONObject respObject = new JSONObject(string);
            Log.d(Constants.TAG,"data: "+respObject.toString());
            checkOutItemsArrayList = new ArrayList<>();
            ArrayList<SoldItem> soldItems = new ArrayList<>();
            SoldItem soldItem = new SoldItem();
            JSONArray allProducts = respObject.getJSONArray("imei_list");
            for(int i=0;i<allProducts.length();i++)
            {
                JSONObject object = allProducts.getJSONObject(i);
                soldItem.setProduct_id(object.getInt("product_id"));
                soldItem.setProduct_name(object.getString("product_name"));
                soldItem.setImei(object.getString("imei"));
                soldItem.setSale_price(object.getLong("sale_price"));
                total+=object.getLong("sale_price");
                soldItems.add(soldItem);
                //object= new JSONObject();
                soldItem = new SoldItem();
            }

            int oldProductId =-1;
            CheckOutItem checkOutItem= new CheckOutItem();
            for(SoldItem item :soldItems)
            {
                if(item.getProduct_id()!=oldProductId)
                {
                    checkOutItem.setProductName(item.getProduct_name());
                    checkOutItem.setCollectivePrice(item.getSale_price());
                    checkOutItem.getIMEINos().add(item.getImei());
                    checkOutItem.setSalePrice(item.getSale_price());
                    checkOutItemsArrayList.add(checkOutItem);
                    oldProductId=item.getProduct_id();
                    checkOutItem= new CheckOutItem();
                }
                else
                {
                    for(CheckOutItem checkOutItem1:checkOutItemsArrayList)
                    {
                        if(checkOutItem1.getProductName().equals(item.getProduct_name()))
                        {
                            checkOutItem1.setCollectivePrice(checkOutItem1.getCollectivePrice()+item.getSale_price());
                            checkOutItem1.getIMEINos().add(item.getImei());
                        }
                    }
                }
            }

            //set recycler view here
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CheckOutActivity.this, 1);
            checkout_product_recycler_view = (RecyclerView) findViewById(R.id.checkout_product_recycler_view);
            adapter = new CheckOutItemsAdapter(CheckOutActivity.this, checkOutItemsArrayList);
            checkout_product_recycler_view.setLayoutManager(mLayoutManager);
            checkout_product_recycler_view.setAdapter(adapter);
            int resId = R.anim.layout_animation_from_left;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(CheckOutActivity.this, resId);
            checkout_product_recycler_view.setLayoutAnimation(animation);
            ((TextView) findViewById(R.id.product_grand_total_price)).setText(total+" €");


        }catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d(Constants.TAG,"exception :"+ex.getMessage());
        }

    }

}
