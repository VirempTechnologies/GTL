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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.CartAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.CartItem;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by awais on 11/21/2017.
 */

public class CartActivity extends AppCompatActivity {
    CartAdapter adapter;
    ArrayList<CartItem> cartItemArrayList;
    RecyclerView product_cart_recycler_view;
    double subTotal=0;
    SharedPreferences settings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        cartItemArrayList = new ArrayList<>();
        for (Map.Entry<String,CartItem> cartItem:Constants.cartItemsMap.entrySet())
        {
            cartItemArrayList.add(cartItem.getValue());
            subTotal+=cartItem.getValue().getCollective_price();
        }

        final Client client = (Client) getIntent().getSerializableExtra("client");
        ((TextView) findViewById(R.id.client_name)).setText(client.getClient_name());
        ((TextView) findViewById(R.id.client_current_balance)).setText(client.getCurrent_bal());
        ((TextView) findViewById(R.id.client_company_name)).setText(client.getCompany_name());


        //setting the recycler view
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        product_cart_recycler_view = (RecyclerView) findViewById(R.id.product_cart_recycler_view);
        adapter = new CartAdapter(this, cartItemArrayList);
        product_cart_recycler_view.setLayoutManager(mLayoutManager);
        product_cart_recycler_view.setAdapter(adapter);
        int resId = R.anim.layout_animation_from_left;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        product_cart_recycler_view.setLayoutAnimation(animation);

        FancyButton addAnotherItemBtn = (FancyButton) findViewById(R.id.btn_addAnother);
        addAnotherItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        TextView product_receipt_total_price = (TextView) findViewById(R.id.product_receipt_total_price);
        TextView product_grand_total_price = (TextView) findViewById(R.id.product_grand_total_price);
        product_receipt_total_price.setText(subTotal+" €");
        Log.d(Constants.TAG," setting the cart proce "+(subTotal-Double.parseDouble(client.getCurrent_bal().replace("€","")))+"€");
        product_grand_total_price.setText((subTotal-Double.parseDouble(client.getCurrent_bal().replace("€","")))+"€");

        FancyButton proceedCheckOut = (FancyButton) findViewById(R.id.btn_proceedCheckOut);
        proceedCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItemArrayList.size()==0)
                {

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(CartActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Opps");
                    builder.setIcon(R.drawable.corss);
                    builder.setMessage("cart is Empty... ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
                    builder.show();
                }
                else {
                    try {
                        int quantity = 0;
                        JSONArray productArray = new JSONArray();
                        JSONObject product = new JSONObject();
                        for (CartItem cartItem : cartItemArrayList) {

                            product.put("product_id", cartItem.getProduct_id());
                            product.put("quantity", cartItem.getQuantity());
                            product.put("sale_price", cartItem.getSale_price());
                            quantity+=cartItem.getQuantity();
                            productArray.put(product);
                            product = new JSONObject();
                        }
                        double total_amount =  Double.parseDouble(((TextView) findViewById(R.id.product_grand_total_price)).getText().toString().replace("€",""));
                        Toast.makeText(CartActivity.this, "check out", Toast.LENGTH_SHORT).show();
                        settings = getSharedPreferences("GTL-Settings", 0);
                        final String headerss = settings.getString("headers", null);
                        JSONObject headers = new JSONObject(headerss);
                        JSONObject params = new JSONObject();
                        params.put("headers", headers);
                        params.put("products", productArray);
                        params.put("total_sets", quantity);
                        params.put("total_amount", total_amount);
                        params.put("client_id", client.getClient_id());

                        Log.d("checklog", "sending request : " + params.toString());
                        Log.d(Constants.TAG, "here to call the web service to proceed check out..");
                        invokeWS(params,CartActivity.this,client);
                       } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d("checklog", "exception : " + ex.getMessage());

                    }
                }

            }
        });
    }
    public void invokeWS(JSONObject params, final Context context,final Client salesmanClient){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getCheckOutUrl, entity, "application/json",new JsonHttpResponseHandler() {

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
                                prgDialog.dismiss();
                                startActivity(new Intent(CartActivity.this, CheckOutActivity.class).putExtra("client", salesmanClient).putExtra("respObject",resp.toString()));
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
                    builder.setMessage("Service Failer server not found..! ");
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
