package com.example.awais.gtl.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by awais on 16/05/2017.
 */
public class FindProductActivity extends AppCompatActivity {

    EditText searchEditText;
    FancyButton searchBtn;
    SharedPreferences setting;
    RelativeLayout product_rlv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_product_activity);
        getSupportActionBar().setTitle("Find Product");
        setting = getSharedPreferences("GTL-Settings",0);
        searchEditText = findViewById(R.id.search_product_edittext);
        searchBtn  = findViewById(R.id.search_icon);
        product_rlv= findViewById(R.id.search_product_details_rlv);
        product_rlv.setVisibility(View.GONE);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String imeiNo = searchEditText.getText().toString().trim();
                    if(!imeiNo.isEmpty()) {
                        final String headerss = setting.getString("headers", null);
                        JSONObject headers = new JSONObject(headerss);
                        JSONObject params = new JSONObject();
                        params.put("headers", headers);
                        params.put("imei", imeiNo);
                        Log.d("checklog", "sending request : " + params.toString());
                        if(product_rlv.getVisibility()==View.VISIBLE)
                        {
                            product_rlv.setVisibility(View.GONE);
                        }
                        invokeWS(params,FindProductActivity.this);
                    }
                    else {

                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    Log.e(Constants.TAG, "exception: " + ex.getMessage());
                }
            }
        });

    }
    public void invokeWS(JSONObject params, Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...Searching Product");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            StringEntity entity = new StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context , Constants.getFindProductUrl, entity, "application/json",new JsonHttpResponseHandler() {

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
                            prgDialog.dismiss();
                            if(resp.optString("status_code").equals("200"))
                            {
                                product_rlv.setVisibility(View.VISIBLE);
                                JSONObject data = resp.optJSONObject("data");
                                ((TextView) findViewById(R.id.product_name)).setText(data.getString("product"));
                                ((TextView) findViewById(R.id.product_imei_no)).setText("IMEI: "+data.getString("imei"));
                                ((TextView) findViewById(R.id.product_sate)).setText("state: "+data.getString("state").toUpperCase());

                            }
                            else if(resp.optString("status_code").equals("424"))
                            {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(FindProductActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    new AlertDialog.Builder(FindProductActivity.this, R.style.AppCompatAlertDialogStyle);
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
                            new AlertDialog.Builder(FindProductActivity.this, R.style.AppCompatAlertDialogStyle);
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
