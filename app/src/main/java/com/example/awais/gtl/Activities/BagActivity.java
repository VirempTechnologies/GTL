package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.R;
import com.example.awais.gtl.WebServiceHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BagActivity extends AppCompatActivity {
    BagAdapter adapter;
   // ArrayList<BagProduct> bagProductArrayList;
    RecyclerView salesman_bag_recyler_view;
    ProgressDialog prgDialog;
    JSONObject respObject;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bag_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        try {
            settings = getSharedPreferences("GTL-Settings",0);
            final String headerss = settings.getString("headers",null);
            JSONObject headers = new JSONObject(headerss);
            JSONObject params = new JSONObject();
            params.put("headers", headers);
            Log.d("checklog", "sending request : " + params.toString());
            // invoke web service here
            //respObject = webServiceHelper.sendPostRequest(params, BagActivity.this, Constants.getBagUrl);
            ///
                invokeWS(params,BagActivity.this);
            //
            final Client client = (Client) getIntent().getSerializableExtra("client");
            ((TextView) findViewById(R.id.client_name)).setText(client.getClient_name());
            ((TextView) findViewById(R.id.client_current_balance)).setText(client.getCurrent_bal());
            ((TextView) findViewById(R.id.client_company_name)).setText(client.getCompany_name());

            Button button = (Button) findViewById(R.id.btn_viewinvoice);
              button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      startActivity(new Intent(BagActivity.this, CartActivity.class).putExtra("client",client));
                      overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                  }
              });

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(Constants.TAG,"here to refresh activity salesman adapter");
        if(salesman_bag_recyler_view!=null)
        salesman_bag_recyler_view.getAdapter().notifyDataSetChanged();
        //runLayoutAnimation(salesman_bag_recyler_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(Constants.TAG,"here to create owerflow menu");

        getMenuInflater().inflate(R.menu.profile_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void invokeWS(JSONObject params, final Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait... loading your bag");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getBagUrl, entity, "application/json",new JsonHttpResponseHandler() {

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

                                //set the view here
                                Log.d("checklog", "responce : " + resp.toString());
                                BagProduct bagProduct;
                                Constants.bagProductArrayList.clear();
                                JSONArray allProducts = resp.getJSONObject("data").getJSONArray("products");
                                if(allProducts.length()!=0) {
                                    for (int i = 0; i < allProducts.length(); i++) {
                                        JSONObject product = allProducts.getJSONObject(i);
                                        bagProduct = new BagProduct();
                                        bagProduct.setProductName(product.getString("name"));
                                        bagProduct.setSalePrice(product.getInt("sale_price"));
                                        bagProduct.setMyStock(product.getInt("quantity"));
                                        bagProduct.setCompanyStock(00);
                                        bagProduct.setProduct_id(product.getInt("id"));
                                        bagProduct.setInitialQuantity(0);
                                        bagProduct.setBagIndex(i);
                                        bagProduct.setModel(product.getString("model"));
                                        bagProduct.setImage(product.getString("product_image"));
                                        Constants.bagProductArrayList.add(bagProduct);
                                    }
                                    //client data

                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(BagActivity.this, 1);
                                    salesman_bag_recyler_view = (RecyclerView) findViewById(R.id.salesman_bag_recycler_view);
                                    adapter = new BagAdapter(BagActivity.this, Constants.bagProductArrayList);
                                    salesman_bag_recyler_view.setLayoutManager(mLayoutManager);
                                    salesman_bag_recyler_view.setAdapter(adapter);
                                    int resId = R.anim.layout_animation_from_left;
                                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(BagActivity.this, resId);
                                    salesman_bag_recyler_view.setLayoutAnimation(animation);


                                    RelativeLayout invoice_total_rlv = ((RelativeLayout) findViewById(R.id.invoice_total_rlv));
                                    int animId = R.anim.layout_animation_from_bottom;
                                    LayoutAnimationController animation2 = AnimationUtils.loadLayoutAnimation(BagActivity.this, animId);
                                    invoice_total_rlv.setLayoutAnimation(animation2);
                                }
                                else
                                {
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(BagActivity.this, R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Opps");
                                    builder.setIcon(R.drawable.corss);
                                    builder.setMessage("Your bag is Empty");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }
                                //end set
                            }
                            else  if(resp.getString("status_code").equals("404"))
                            {
                                prgDialog.dismiss();

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(BagActivity.this, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Opps");
                                builder.setIcon(R.drawable.corss);
                                builder.setMessage("bag not found ");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                });
                                builder.show();
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
                                    new AlertDialog.Builder(BagActivity.this, R.style.AppCompatAlertDialogStyle);
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
                            new AlertDialog.Builder(BagActivity.this, R.style.AppCompatAlertDialogStyle);
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
