package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Adapters.MyStockAdapter;
import com.example.awais.gtl.Adapters.TransactionAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.BagProduct;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.Pojos.Transaction;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by awais on 12/8/2017.
 */

public class ClientLedgerActivity extends AppCompatActivity {

    TransactionAdapter adapter;
     ArrayList<Transaction> transactionArrayList= new ArrayList<>();
    RecyclerView all_transaction_recycler_view;

    ProgressDialog prgDialog;
    JSONObject respObject;
    SharedPreferences settings;
    JSONObject params = new JSONObject();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_ledger_activity);
        final Client client = (Client) getIntent().getSerializableExtra("client");
        try {
            SharedPreferences setting = getSharedPreferences("GTL-Settings", 0);
            ;
            final String headerss = setting.getString("headers", null);
            JSONObject headers = new JSONObject(headerss);
            params = new JSONObject();
            params.put("headers", headers);
            params.put("client_id", client.getClient_id());
            Log.d("checklog", "sending request : " + params.toString());
            // invoke web service here
            //respObject = webServiceHelper.sendPostRequest(params, BagActivity.this, Constants.getBagUrl);
            ///
            getLedgerReport(params,this);

            //getAllSales(params, getActivity());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e(Constants.TAG,"exception: "+ ex.getMessage());
        }
    }
    public void getLedgerReport(JSONObject params, Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait... loading your Ledger");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getClientLedgerUrl, entity, "application/json",new JsonHttpResponseHandler() {

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
                                JSONObject data = resp.getJSONObject("data");

                                JSONArray transactionsArray = data.getJSONArray("Transactions");
                                Transaction transaction;
                                transactionArrayList.clear();
                                if(transactionsArray.length()!=0) {
                                    for (int i = 0; i < transactionsArray.length(); i++) {
                                        JSONObject transactionObj = transactionsArray.getJSONObject(i);
                                        transaction= new Transaction();
                                        transaction.setAccountTitle(transactionObj.getString("account_title"));
                                        transaction.setAccountType(transactionObj.getString("account_type"));
                                        String olddate = transactionObj.getString("date").replace('|',' ');
                                        String[] date = olddate.split(" ");
                                        transaction.setTransactionDate(date[2]+" "+date[1]+" "+ date[3]);
                                        transaction.setDebitBalance(transactionObj.getLong("Dr"));
                                        transaction.setCreditBalance(transactionObj.getLong("Cr"));
                                        transactionArrayList.add(transaction);
                                    }
                                    //setting the summery
                                    JSONObject summaryObject = data.getJSONObject("summary");
                                    ((TextView) findViewById(R.id.cash_debit_text)).setText(summaryObject.getString("cashDr")+" €");
                                    ((TextView) findViewById(R.id.cash_credit_text)).setText(summaryObject.getString("cashCr")+" €");
                                    ((TextView) findViewById(R.id.sale_debit_text)).setText(summaryObject.getString("salesDr")+" €");
                                    ((TextView) findViewById(R.id.sale_credit_text)).setText(summaryObject.getString("salesCr")+" €");
                                    ((TextView) findViewById(R.id.ar_debit_text)).setText(summaryObject.getString("accountsReceivableDr")+" €");
                                    ((TextView) findViewById(R.id.ar_credit_text)).setText(summaryObject.getString("accountsReceivableCr")+" €");
                                    ((TextView) findViewById(R.id.ap_debit_text)).setText(summaryObject.getString("accountsPayableDr")+" €");
                                    ((TextView) findViewById(R.id.ap_credit_text)).setText(summaryObject.getString("accountsPayableCr")+" €");
                                    //set credit debit sum
                                    ((TextView) findViewById(R.id.debit_sum)).setText(data.getString("debit_total")+" €");
                                    ((TextView) findViewById(R.id.credit_sum)).setText(data.getString("credit_total")+" €");



                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ClientLedgerActivity.this, 1);
                                    all_transaction_recycler_view = (RecyclerView) findViewById(R.id.all_transaction_recycler_view);
                                    adapter = new TransactionAdapter(ClientLedgerActivity.this, transactionArrayList);
                                    all_transaction_recycler_view.setLayoutManager(mLayoutManager);
                                    all_transaction_recycler_view.setAdapter(adapter);
                                    int resId = R.anim.layout_animation_from_left;
                                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ClientLedgerActivity.this, resId);
                                    all_transaction_recycler_view.setLayoutAnimation(animation);



                                }
                                else
                                {
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(ClientLedgerActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                        new AlertDialog.Builder(ClientLedgerActivity.this, R.style.AppCompatAlertDialogStyle);
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
                        }
                        if(statusCode==500)
                        {
                            prgDialog.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(ClientLedgerActivity.this, R.style.AppCompatAlertDialogStyle);
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
                            new AlertDialog.Builder(ClientLedgerActivity.this, R.style.AppCompatAlertDialogStyle);
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
