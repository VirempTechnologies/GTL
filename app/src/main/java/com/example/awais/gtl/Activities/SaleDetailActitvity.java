package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SaleDetailActitvity extends AppCompatActivity
{
    CheckOutItemsAdapter adapter;
    RecyclerView checkout_product_recycler_view;
    ArrayList<CheckOutItem> checkOutItemsArrayList;

    SharedPreferences settings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_activity);

        try {


//            final Client client = (Client) getIntent().getSerializableExtra("client");
            int invoice_id = getIntent().getIntExtra("invoice_id",-1);
            settings = getSharedPreferences("GTL-Settings",0);
            final String headerss = settings.getString("headers",null);
            JSONObject headers = new JSONObject(headerss);
            JSONObject params = new JSONObject();
            params.put("headers", headers);
            params.put("invoice_id",invoice_id);
            Log.d("checklog", "sending request : " + params.toString());
            getSaleDetail(params,SaleDetailActitvity.this);




        }catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d(Constants.TAG,"exception :"+ex.getMessage());
        }

    }
    public void getSaleDetail(JSONObject params, final Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait... getting sale detail");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getSaleDetailUrl, entity, "application/json",new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    prgDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    try {


                        super.onSuccess(statusCode, headers, resp);
                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "success data say congo : " + resp.toString());

                        if(statusCode==200) {
                            if(resp.getString("status_code").equals("200")) {
                                resp= resp.getJSONObject("data");
                                //prgDialog.dismiss();
                                //setting the invoice details
                                ((TextView) findViewById(R.id.client_name)).setText(resp.getString("client_name"));
                                ((TextView) findViewById(R.id.client_current_balance)).setText(resp.getString("company_name"));
                                if(resp.getString("current_balance").equals("null")) {
                                    ((TextView) findViewById(R.id.client_company_name)).setText("0.00 €");
                                }
                                else
                                {
                                    ((TextView) findViewById(R.id.client_company_name)).setText( resp.getString("current_balance")+" €");
                                }


                    //            JSONObject respObject = new JSONObject(string);
                                Log.d(Constants.TAG,"data: "+resp.toString());
                                checkOutItemsArrayList = new ArrayList<>();
                                ArrayList<SoldItem> soldItems = new ArrayList<>();
                                SoldItem soldItem = new SoldItem();
                                JSONArray allProducts = resp.getJSONArray("imei_list");
                                for(int i=0;i<allProducts.length();i++)
                                {
                                    JSONObject object = allProducts.getJSONObject(i);
                                    soldItem.setProduct_id(object.getInt("product_id"));
                                    soldItem.setProduct_name(object.getString("product_name"));
                                    soldItem.setImei(object.getString("imei"));
                                    soldItem.setSale_price(object.optLong("sale_price"));
                          //          total+=object.getLong("sale_price");
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
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SaleDetailActitvity.this, 1);
                                checkout_product_recycler_view = (RecyclerView) findViewById(R.id.checkout_product_recycler_view);
                                adapter = new CheckOutItemsAdapter(SaleDetailActitvity.this, checkOutItemsArrayList);
                                checkout_product_recycler_view.setLayoutManager(mLayoutManager);
                                checkout_product_recycler_view.setAdapter(adapter);
                                int resId = R.anim.layout_animation_from_left;
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(SaleDetailActitvity.this, resId);
                                checkout_product_recycler_view.setLayoutAnimation(animation);
                                ((TextView) findViewById(R.id.product_grand_total_price)).setText(resp.getString("total_amount")+" €");
                                //end setting
                                prgDialog.dismiss();

                            }
                            else if(resp.getString("status_code").equals("199")) {
                                prgDialog.dismiss();

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Opps");
                                builder.setIcon(R.drawable.corss);
                                builder.setMessage(resp.optString("error"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                });
                                builder.show();

                            }
                        }
                        if(statusCode==500)
                        {
                            prgDialog.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("500");
                            builder.setIcon(R.drawable.corss);
                            builder.setMessage("internal Server Error ! ");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                            builder.show();
                        }
                        if(statusCode==404)
                        {
                            prgDialog.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Opps");
                            builder.setIcon(R.drawable.corss);
                            builder.setMessage("server not found ! ");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                            builder.show();
                        }


                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    prgDialog.dismiss();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Opps");
                    builder.setIcon(R.drawable.corss);
                    builder.setMessage("Service Failer ! ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
                    builder.show();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    prgDialog.dismiss();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Opps");
                    builder.setIcon(R.drawable.corss);
                    builder.setMessage("Service Failer ! ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
                    builder.show();

                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }


            });

        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }

}
