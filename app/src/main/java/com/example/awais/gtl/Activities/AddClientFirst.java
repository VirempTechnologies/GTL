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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.City;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.reginald.editspinner.EditSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by awais on 11/23/2017.
 */

public class AddClientFirst extends AppCompatActivity {
    AutoCompleteTextView first_name_text,last_name_text,company_name_text,email_text,phone_no_text,land_line_no_text,address_text
                        ,postal_no_text,user_name_text,password_text,hrb_no_text,txt_no_text,starting_blc_text,comments_text;
    EditSpinner city_spinner,status_spinner;
    ArrayAdapter<String> adapter;
    int city_id=0;
    String status="";
    FancyButton btn_addClient;
    SharedPreferences settings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_first);

//        MaterialBetterSpinner city_spinner ;
        //String[] SPINNER_DATA = {"ANDROID","PHP","BLOGGER","WORDPRESS"};
//        String[] cities = new String[Constants.allCities.size()];
//        for(int i=0;i<Constants.allCities.size();i++)
//        {
//            cities[i]=Constants.allCities.get(i).getCity_name();
//        }
        //Constants.allCitiesMap.
        final String[] cities = Constants.allCitiesMap.keySet().toArray(new String[Constants.allCitiesMap.keySet().size()]);
        //set city_spinner
        city_spinner = (EditSpinner) findViewById(R.id.city_spinner);
         adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                cities);
        city_spinner.setAdapter(adapter);
        city_spinner.setEditable(false);
        // set the dropdown drawable on the right of EditText and its size
        city_spinner.setDropDownDrawable(getResources().getDrawable(R.drawable.ic_arrow_down), 40, 40);
        // set the spacing bewteen Edited area and DropDown click area
        city_spinner.setDropDownDrawableSpacing(10);
        // set DropDown popup window background
        city_spinner.setDropDownBackgroundResource(R.drawable.round_corner_gray);
        // set DropDown animation of the popup window
        city_spinner.setDropDownAnimationStyle(R.style.OverflowMenu);
        city_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city= cities[position];
                city_id= Constants.allCitiesMap.get(city);
                Toast.makeText(AddClientFirst.this, "city_id= "+city_id, Toast.LENGTH_SHORT).show();

            }
        });
        
        //end city spinner
        //set status spinner
        status_spinner = (EditSpinner) findViewById(R.id.status_spinner);
        final String[] statusSpinnerData= {"Active","InActive"};
//        status_spinner = (MaterialBetterSpinner)findViewById(R.id.status_spinner);
        ArrayAdapter<String> status_adapter = new ArrayAdapter<String>(AddClientFirst.this, android.R.layout.simple_dropdown_item_1line, statusSpinnerData);
        status_spinner.setAdapter(status_adapter);
        status_spinner.setEditable(false);
        // set the dropdown drawable on the right of EditText and its size
        status_spinner.setDropDownDrawable(getResources().getDrawable(R.drawable.ic_arrow_down), 40, 40);
        // set the spacing bewteen Edited area and DropDown click area
        status_spinner.setDropDownDrawableSpacing(10);
        // set DropDown popup window background
        status_spinner.setDropDownBackgroundResource(R.drawable.round_corner_gray);
        // set DropDown animation of the popup window
        status_spinner.setDropDownAnimationStyle(R.style.OverflowMenu);
        status_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                status=statusSpinnerData[position];
             //   Toast.makeText(AddClientFirst.this, "status: "+status, Toast.LENGTH_SHORT).show();
            }
        });
        //initializing the forms items
        first_name_text= (AutoCompleteTextView) findViewById(R.id.first_name_text);
        last_name_text= (AutoCompleteTextView) findViewById(R.id.last_name_text);
        company_name_text= (AutoCompleteTextView) findViewById(R.id.company_name_text);
        email_text= (AutoCompleteTextView) findViewById(R.id.email_text);
        phone_no_text= (AutoCompleteTextView) findViewById(R.id.phone_no_text);
        land_line_no_text= (AutoCompleteTextView) findViewById(R.id.land_line_no_text);
        address_text= (AutoCompleteTextView) findViewById(R.id.address_text);
        postal_no_text= (AutoCompleteTextView) findViewById(R.id.postal_no_text);
        user_name_text= (AutoCompleteTextView) findViewById(R.id.user_name_text);
        password_text= (AutoCompleteTextView) findViewById(R.id.password_text);
        hrb_no_text= (AutoCompleteTextView) findViewById(R.id.hrb_no_text);
        txt_no_text= (AutoCompleteTextView) findViewById(R.id.txt_no_text);
        starting_blc_text= (AutoCompleteTextView) findViewById(R.id.starting_blc_text);
        comments_text= (AutoCompleteTextView) findViewById(R.id.comments_text);
        //setting the headers
        try {


            settings = getSharedPreferences("GTL-Settings", 0);
            final String headerss = settings.getString("headers", null);
            final JSONObject headers = new JSONObject(headerss);
            final JSONObject params = new JSONObject();
            params.put("headers", headers);

            //Log.d(Constants.TAG,"first name:"+newClient.getFirst_name());
            btn_addClient = (FancyButton) findViewById(R.id.btn_addClient);
            btn_addClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if(first_name_text.getText().toString().equals("") ||
                                last_name_text.getText().toString().equals("") ||
                                phone_no_text.getText().toString().equals("") ||
                                land_line_no_text.getText().toString().equals("") ||
                                address_text.getText().toString().equals("") ||
                                postal_no_text.getText().toString().equals("") ||
                                email_text.getText().toString().equals("") ||
                                user_name_text.getText().toString().equals("") ||
                                password_text.getText().toString().equals("") ||
                                company_name_text.getText().toString().equals("") ||
                                txt_no_text.getText().toString().equals("") ||
                                hrb_no_text.getText().toString().equals("") ||
                                starting_blc_text.getText().toString().equals("") ||
                                comments_text.getText().toString().equals("") ||
                                city_id==0||
                                status==""
                                ) {
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(AddClientFirst.this, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Opps");
                            builder.setIcon(R.drawable.corss);
                            builder.setMessage("please fill all the fields");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                            builder.show();

                        }
                        else
                        {
                            Client newClient = new Client();
                            newClient.setParent_id(headers.getInt("user_id"));
                            newClient.setUser_type("user");
                            newClient.setFirst_name(first_name_text.getText().toString());
                            newClient.setLast_name(last_name_text.getText().toString());
                            newClient.setMobile_no(phone_no_text.getText().toString());
                            newClient.setLand_line_no(land_line_no_text.getText().toString());
                            newClient.setAddress(address_text.getText().toString());
                            newClient.setCity_id(city_id);
                            newClient.setPostal_code(postal_no_text.getText().toString());
                            newClient.setStatus(status);
                            newClient.setEmail(email_text.getText().toString());
                            newClient.setUsername(user_name_text.getText().toString());
                            newClient.setPassword(password_text.getText().toString());
                            newClient.setPassword_confirmation(password_text.getText().toString());
                            newClient.setCompany_name(company_name_text.getText().toString());
                            newClient.setTax_no(txt_no_text.getText().toString());
                            newClient.setHrb_no((hrb_no_text.getText().toString()));
                            newClient.setStart_bal((starting_blc_text.getText().toString()));
                            newClient.setComment(comments_text.getText().toString());

                            params.put("client", new JSONObject(new Gson().toJson(newClient)));
                            Log.d(Constants.TAG, "data:" + params.toString());
                            saveClient(params,AddClientFirst.this);
                            //Toast.makeText(AddClientFirst.this, "first name" + newClient.getParent_id(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        Log.e(Constants.TAG,"exception:"+ex.getMessage());
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e(Constants.TAG,"exception:"+ex.getMessage());
        }
    }
    public void saveClient(JSONObject params, final Context context){
        final ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait... saving Client");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        prgDialog.show();
        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getAddClientUrl, entity, "application/json",new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONObject resp) {
                    try {


                        super.onSuccess(statusCode, headers, resp);
                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "success data say congo : " + resp.toString());

                        if(statusCode==200) {
                            if(resp.getString("status_code").equals("200")) {

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                                builder.setCancelable(false);
                                builder.setTitle("Congratulations..");
                                builder.setIcon(R.drawable.tick);
                                builder.setMessage("Client Successfully added.. ");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();

                                prgDialog.dismiss();
                            }
                            else  if(resp.getString("status_code").equals("181"))
                            {
                                prgDialog.dismiss();

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Opps");
                                builder.setIcon(R.drawable.corss);
                                builder.setMessage(resp.getString("error"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                        try {


                                            JSONObject errorBag = resp.getJSONObject("error_bag");
                                            if(errorBag.opt("email")!=null)
                                            {
                                                JSONArray emailError = errorBag.getJSONArray("email");
                                                //Toast.makeText(AddClientFirst.this, emailError.get(0)+"", Toast.LENGTH_SHORT).show();
                                                email_text.setError(emailError.get(0).toString());
                                                email_text.requestFocus();

                                            }
                                            else if(errorBag.opt("username")!=null)
                                            {
                                                JSONArray emailError = errorBag.getJSONArray("username");
                                                //Toast.makeText(AddClientFirst.this, emailError.get(0)+"", Toast.LENGTH_SHORT).show();
                                                user_name_text.setError(emailError.get(0).toString());
                                                user_name_text.requestFocus();

                                            }else if(errorBag.opt("password")!=null)
                                            {
                                                JSONArray emailError = errorBag.getJSONArray("password");
                                                //Toast.makeText(AddClientFirst.this, emailError.get(0)+"", Toast.LENGTH_SHORT).show();
                                                password_text.setError(emailError.get(0).toString());
                                                password_text.requestFocus();


                                            }
                                        }
                                        catch (Exception ex)
                                        {
                                            ex.printStackTrace();;
                                            Log.e(Constants.TAG,ex.getMessage());
                                        }
                                    }
                                });
                                builder.show();
                            }
                            else  if(resp.getString("status_code").equals("404"))
                            {
                                prgDialog.dismiss();

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
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