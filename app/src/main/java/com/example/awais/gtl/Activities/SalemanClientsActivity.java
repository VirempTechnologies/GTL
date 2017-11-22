package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Adapters.ClientAdapter;
import com.example.awais.gtl.Adapters.OperationsAdapter;
import com.example.awais.gtl.Constants;
import com.example.awais.gtl.Pojos.Client;
import com.example.awais.gtl.Pojos.Operation;
import com.example.awais.gtl.R;
import com.example.awais.gtl.WebServiceHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by awais on 17/05/2017.
 */
public class SalemanClientsActivity extends AppCompatActivity {
    ClientAdapter adapter;
    ArrayList<Client> clientArrayList;
    RecyclerView salesman_clients_recycler_view;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences settings;
    JSONObject finalRespObject;
    WebServiceHelper webServiceHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salesman_clients_activity);
        coordinatorLayout =(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        settings = getSharedPreferences("GTL-Settings",0);
        String name = settings.getString("name",null);
        String area = settings.getString("assigned_area",null);
        ((TextView) findViewById(R.id.saleman_name)).setText(name);
        ((TextView) findViewById(R.id.saleman_area)).setText(area);
        webServiceHelper= new WebServiceHelper();
        clientArrayList= new ArrayList<>();
        try {


            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            actionBar.setElevation(0);
            settings = getSharedPreferences("GTL-Settings",0);
            final String headerss = settings.getString("headers",null);

            JSONObject headers = new JSONObject(headerss);
            JSONObject params = new JSONObject();
            params.put("headers", headers);
            Log.d("checklog", "sending request : " + params.toString());
            finalRespObject = webServiceHelper.sendPostRequest(params,this,Constants.getClientsUrl);
            if (finalRespObject!=null)
            {
                //Log.d(Constants.TAG,"the clients are: "+finalRespObject.toString());
                Client client = new Client();
                JSONArray parentArray = finalRespObject.getJSONArray("data");
                for (int i= 0; i<parentArray.length();i++)
                {
                    JSONObject firstObject = parentArray.getJSONObject(i);
                    client.setClient_id(firstObject.getInt("id"));
                    client.setCompany_name(firstObject.getString("company_name")+" Globe TeleLink GmbH");
                    if(firstObject.getString("current_bal").equals("null"))
                    {
                        client.setCurrent_bal("0.00 €");
                    }
                    else
                    {
                        client.setCurrent_bal(firstObject.getString("current_bal")+" €");

                    }
                    JSONObject userObject = firstObject.getJSONObject("user");
                    JSONObject personObject = userObject.getJSONObject("person");
                    client.setClient_name(personObject.getString("first_name")+" " +personObject.getString("last_name"));
                    clientArrayList.add(client);
                    client= new Client();
                }
            }


            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

            salesman_clients_recycler_view = (RecyclerView) findViewById(R.id.salesman_clients_recycler_view);
            adapter = new ClientAdapter(this, clientArrayList);
            salesman_clients_recycler_view.setLayoutManager(mLayoutManager);
            salesman_clients_recycler_view.setItemAnimator(new DefaultItemAnimator());
            salesman_clients_recycler_view.setAdapter(adapter);

            // grid animations
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
            salesman_clients_recycler_view.setLayoutAnimation(animation);
            //end
            //user profile animiation
            RelativeLayout profileLayout = (RelativeLayout) findViewById(R.id.user_profile_info_rlv);
            int animationId = R.anim.layout_animation_from_left;
            LayoutAnimationController profileDataAnimation = AnimationUtils.loadLayoutAnimation(this, animationId);
            profileLayout.setLayoutAnimation(profileDataAnimation);
            //end
            RelativeLayout profileImageLayout = (RelativeLayout) findViewById(R.id.parent_rlv);
            int profileImageAnimationId = R.anim.layout_animation_fall_down;
            LayoutAnimationController profileImageAnimation = AnimationUtils.loadLayoutAnimation(this, profileImageAnimationId);
            profileImageLayout.setLayoutAnimation(profileImageAnimation);
            //end


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
        Log.d(Constants.TAG,"here to refresh activity");
        Constants.bagProductArrayList.clear();
        Constants.cartItemsMap.clear();
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





}
