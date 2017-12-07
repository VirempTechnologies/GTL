package com.example.awais.gtl.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Activities.BagActivity;
import com.example.awais.gtl.Activities.ReceiptsActivity;
import com.example.awais.gtl.Adapters.AllReciptsAdapter;
import com.example.awais.gtl.Adapters.BagAdapter;
import com.example.awais.gtl.Adapters.ReceiptAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.MediaConversion;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.Receipt;
import com.example.awais.gtl.Pojos.Sale;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.riyagayasen.easyaccordion.AccordionView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by awais on 11/20/2017.
 */

public class TodayReceipts extends Fragment {
    String todayDate;
    ReceiptAdapter adapter;
    AccordionView accordionView;
    View rootView;
    RecyclerView today_receipt_recycler_view;
    ArrayList<Receipt> allReceiptsArrayList;
    private boolean fragmentResume=false;
    private boolean fragmentVisible=false;
    private boolean fragmentOnCreated=false;
    boolean serviceCall=false;
    JSONObject params = new JSONObject();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.today_receipts, container, false);
        try {
            accordionView = (AccordionView) rootView.findViewById(R.id.sale_accordion);
            today_receipt_recycler_view = (RecyclerView) accordionView.findViewById(R.id.today_receipt_recycler_view);

            SharedPreferences setting = getActivity().getSharedPreferences("GTL-Settings", 0);
            final String headerss = setting.getString("headers", null);
            JSONObject headers = new JSONObject(headerss);
            JSONObject params = new JSONObject();
            params.put("headers", headers);
            Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.DATE, 0);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            todayDate = format1.format(cal.getTime());
            params.put("date1",todayDate);
            params.put("date2",todayDate);

            Log.d("checklog", "sending request : " + params.toString());
            Log.d(Constants.TAG, "date:" + todayDate);
           // Log.d("checklog", "sending request : " + params.toString());
            // invoke web service here
            //respObject = webServiceHelper.sendPostRequest(params, BagActivity.this, Constants.getBagUrl);
            ///
              getAllSales(params, getActivity());

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
        prgDialog.setMessage("Please wait... getting today sales");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        prgDialog.show();
        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context , Constants.getDateSaleUrl, entity, "application/json",new JsonHttpResponseHandler() {

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
                            if(resp.getString("status_code").equals("200")) {

                                accordionView.setHeadingString(todayDate);
                                ((TextView)accordionView.findViewById(R.id.heading)).setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                                ((TextView)accordionView.findViewById(R.id.heading)).setTextSize(15);
                                ((ImageView)accordionView.findViewById(R.id.dropup_image)).getDrawable().setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));

                                prgDialog.dismiss();
                                resp=resp.getJSONObject("data");
                                JSONArray sales = resp.getJSONArray(todayDate);
                                allReceiptsArrayList = new ArrayList<>();
                                Receipt receipt = new Receipt();
                                for(int i=0;i<sales.length();i++)
                                {
                                    JSONObject saleObejct = sales.getJSONObject(i);
                                    receipt.setInvoice_id(saleObejct.getInt("invoice_id"));
                                    receipt.setClient_name(saleObejct.getString("client_name"));
                                    receipt.setCompany_name(saleObejct.getString("company_name"));
                                    receipt.setQuantity(saleObejct.getString("quantity"));
                                    receipt.setTotal_amount(saleObejct.getString("total_amount"));
                                    receipt.setInvoice_date(todayDate);
                                    allReceiptsArrayList.add(receipt);
                                    receipt=new Receipt();
                                }





                                adapter = new ReceiptAdapter(getActivity(),allReceiptsArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                                today_receipt_recycler_view.setLayoutManager(mLayoutManager);
                                today_receipt_recycler_view.setAdapter(adapter);
                                prgDialog.dismiss();
                            }
                            else if(resp.getString("status_code").equals("177"))
                            {
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
                    builder.setMessage("server not found..! ");
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
                    builder.setMessage("server not found..! ");
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
