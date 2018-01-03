package com.example.awais.gtl.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Activities.BagActivity;
import com.example.awais.gtl.Adapters.AllReciptsAdapter;
import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.ReceiptAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.Receipt;
import com.example.awais.gtl.Pojos.Sale;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.riyagayasen.easyaccordion.AccordionView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by awais on 11/20/2017.
 */

public class AllReceipts extends Fragment {
    AllReciptsAdapter adapter;
    ArrayList<Sale> salesArrayList;
    RecyclerView all_receipts_recycler_view;
    View rootView;
    private RecyclerViewReadyCallback recyclerViewReadyCallback;
    private boolean fragmentResume=false;
    private boolean fragmentVisible=false;
    private boolean fragmentOnCreated=false;
    boolean serviceCall=false;
    JSONObject params = new JSONObject();
    public interface RecyclerViewReadyCallback {
        void onLayoutReady();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_receipts, container, false);
        try {
            SharedPreferences setting = getActivity().getSharedPreferences("GTL-Settings", 0);
            ;
            final String headerss = setting.getString("headers", null);
            JSONObject headers = new JSONObject(headerss);
            params = new JSONObject();
            params.put("headers", headers);
            Log.d("checklog", "sending request : " + params.toString());
            // invoke web service here
            //respObject = webServiceHelper.sendPostRequest(params, BagActivity.this, Constants.getBagUrl);
            ///
               getAllSales(params, getActivity());

            //getAllSales(params, getActivity());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e(Constants.TAG,"exception: "+ ex.getMessage());
        }
        return rootView;

    }
    public void getAllSales(JSONObject params, final Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait... getting your sales");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        prgDialog.show();
        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context , Constants.getAllSalesUrl, entity, "application/json",new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    try {


                        super.onSuccess(statusCode, headers, resp);
                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "success data say congo : " + resp.toString());

                        if(statusCode==200) {
                                salesArrayList = new ArrayList<>();
                                Sale sale = new Sale();
                                resp=resp.getJSONObject("sale");
                                JSONArray datesArray = resp.names();
                                //Log.d(Constants.TAG,jsonArray.toString());
                                for (int i = 0; i < datesArray.length(); i++) {
                                    JSONArray sales = resp.getJSONArray(datesArray.getString(i));
                                    sale.setSale_date(datesArray.getString(i));
                                    ArrayList<Receipt> receipts = new ArrayList<>();
                                    Receipt receipt = new Receipt();
                                    for (int j = 0; j < sales.length(); j++) {
                                        JSONObject saleObejct = sales.getJSONObject(j);
                                        receipt.setInvoice_id(saleObejct.getInt("invoice_id"));
                                        receipt.setClient_name(saleObejct.getString("client_name"));
                                        receipt.setCompany_name(saleObejct.getString("company_name"));
                                        receipt.setQuantity(saleObejct.getString("quantity"));
                                        receipt.setTotal_amount(saleObejct.getString("total_amount"));
                                        receipt.setInvoice_date(datesArray.getString(i));
                                        receipts.add(receipt);
                                        receipt = new Receipt();
                                    }
                                    sale.setSale_receipts(receipts);
                                    salesArrayList.add(sale);
                                    sale = new Sale();
                                }
                                //setting the recycler view
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
                                all_receipts_recycler_view = (RecyclerView) rootView.findViewById(R.id.all_receipts_recycler_view);
                                adapter = new AllReciptsAdapter(context, salesArrayList);
                                all_receipts_recycler_view.setLayoutManager(mLayoutManager);
                                all_receipts_recycler_view.setAdapter(adapter);
                                all_receipts_recycler_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        if (recyclerViewReadyCallback != null) {
                                            recyclerViewReadyCallback.onLayoutReady();
                                        }
                                        recyclerViewReadyCallback = null;
                                    }
                                });
                                recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
                                    @Override
                                    public void onLayoutReady() {
                                        //
                                        //here comes your code that will be executed after all items has are laid down
                                        //
                                //        Toast.makeText(getActivity(), "layout competed", Toast.LENGTH_SHORT).show();
                                        prgDialog.dismiss();
//                                    int resId = R.anim.layout_animation_from_left;
//                                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
//                                    all_receipts_recycler_view.setLayoutAnimation(animation);
////
                                    }
                                };
//

                                //end setting


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
                        Log.d(Constants.TAG,"exception "+ex.getMessage());
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
                    builder.setMessage("Service Failure..! ");
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
                    builder.setMessage("Service Failure..!");
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
