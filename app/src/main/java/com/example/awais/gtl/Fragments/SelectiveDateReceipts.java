package com.example.awais.gtl.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.awais.gtl.Adapters.AllReciptsAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.Receipt;
import com.example.awais.gtl.Pojos.Sale;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by awais on 11/20/2017.
 */

public class SelectiveDateReceipts extends Fragment{
    AllReciptsAdapter adapter;
    ArrayList<Sale> salesArrayList;
    RecyclerView selective_date_receipts_recycler_view;
    View rootView;
    private AllReceipts.RecyclerViewReadyCallback recyclerViewReadyCallback;
    private boolean toDateCheck=false;
    private boolean fromDateCheck=false;
    public interface RecyclerViewReadyCallback {
        void onLayoutReady();
    }
    JSONObject params = new JSONObject();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.selective_date_receipts, container, false);
            try {

                                //select to date dialog
                final FancyButton to_date = (FancyButton) rootView.findViewById(R.id.btn_to_date);
                final FancyButton from_date = (FancyButton) rootView.findViewById(R.id.btn_from_date);
                final FancyButton proceed_btn = (FancyButton) rootView.findViewById(R.id.btn_get_receipt);
                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener to_date_picker = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //updateLabel();
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        to_date.setText(sdf.format(myCalendar.getTime()));
                        toDateCheck=true;
                    }

                };
                final DatePickerDialog.OnDateSetListener from_date_picker = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //updateLabel();
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        from_date.setText(sdf.format(myCalendar.getTime()));
                        fromDateCheck=true;
                    }

                };


                to_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getActivity(), to_date_picker, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                from_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getActivity(), from_date_picker, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                proceed_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(toDateCheck && fromDateCheck)
                        {
                            try {


                                SharedPreferences setting = getActivity().getSharedPreferences("GTL-Settings", 0);
                                selective_date_receipts_recycler_view = (RecyclerView) rootView.findViewById(R.id.selective_date_receipts_recycler_view);
                                final String headerss = setting.getString("headers", null);
                                JSONObject headers = new JSONObject(headerss);
                                params.put("headers", headers);
                                params.put("date1", to_date.getText());
                                params.put("date2", from_date.getText());
                                Log.d("checklog", "sending request : " + params.toString());
                                getSelectiveSale(params, getActivity());
                            } catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.e(Constants.TAG,ex.getMessage());
                            }

                        }
                        else
                        {
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Opps");
                            builder.setIcon(R.drawable.corss);
                            builder.setMessage("Please select both Dates ! ");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                            builder.show();
                        }
                    }
                });




            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Log.e(Constants.TAG,"exception: "+ ex.getMessage());
            }
                return rootView;
    }
    public void getSelectiveSale(JSONObject params, final Context context){
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
                                resp=resp.getJSONObject("data");
                                salesArrayList = new ArrayList<>();
                                Sale sale = new Sale();

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
                                adapter = new AllReciptsAdapter(context, salesArrayList);
                                selective_date_receipts_recycler_view.setLayoutManager(mLayoutManager);
                                selective_date_receipts_recycler_view.setAdapter(adapter);
                                selective_date_receipts_recycler_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        if (recyclerViewReadyCallback != null) {
                                            recyclerViewReadyCallback.onLayoutReady();
                                        }
                                        recyclerViewReadyCallback = null;
                                    }
                                });
                                recyclerViewReadyCallback = new AllReceipts.RecyclerViewReadyCallback() {
                                    @Override
                                    public void onLayoutReady() {
                                        //
                                        //here comes your code that will be executed after all items has are laid down
                                        //
                                  //      Toast.makeText(getActivity(), "layout competed", Toast.LENGTH_SHORT).show();
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
                        }
                        if(statusCode==500)
                        {
                            prgDialog.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Opps");
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
