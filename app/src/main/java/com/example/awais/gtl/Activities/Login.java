package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Script;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by awais on 16/05/2017.
 */
public class Login  extends AppCompatActivity {
    Button loginBtn;
    private static final int PERMISSION_ALL = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        //check the login state
        SharedPreferences settings = getSharedPreferences("GTL-Settings",0);
        final String islogin = settings.getString("Login",null);
        if(islogin!=null)
        {
            Log.d(Constants.TAG,"here to send it to the main activity");
            startActivity(new Intent(Login.this, SalemanProfile.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }
        else
        {
            Log.d(Constants.TAG,"create login process");
            RelativeLayout loginLayout = (RelativeLayout) findViewById(R.id.parent_rlv);
            int loginLayoutAnimationId = R.anim.layout_animation_from_left_short_duration;
            LayoutAnimationController loginLayoutAnimation = AnimationUtils.loadLayoutAnimation(this, loginLayoutAnimationId);
            loginLayout.setLayoutAnimation(loginLayoutAnimation);
            final AutoCompleteTextView username = (AutoCompleteTextView) findViewById(R.id.login_useremail_text);
            final AutoCompleteTextView password = (AutoCompleteTextView) findViewById(R.id.login_pass_text);

            loginBtn =(Button)findViewById(R.id.btnLogin);
            loginBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                JSONObject params = new JSONObject();
                                params.put("username", username.getText());
                                params.put("password", password.getText());
                                Log.d("checklog", "sending request : "+params.toString());
                                invokeWS(params, Login.this);


                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.e(Constants.TAG,"exception: "+ex.getMessage());
                            }
                        }
                    });
        }


    }
    public void invokeWS(JSONObject params, Context context){
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
            client.post(context ,Constants.loginURL, entity, "application/json",new JsonHttpResponseHandler() {

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
                                String area;
                                if(resp.getString("assigned_area")==null)
                                {
                                    area ="Area Not Assigned";
                                }
                                else
                                {
                                    area =resp.getString("assigned_area");

                                }
                                setLogin("true",resp.getJSONObject("headers").toString(),resp.getString("name"),area);
                                startActivity(new Intent(Login.this, SalemanProfile.class));
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                finish();
                            }
                            else  if(resp.getString("status_code").equals("404"))
                            {
                                prgDialog.dismiss();

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Opps");
                                builder.setIcon(R.drawable.corss);
                                builder.setMessage("Username Password Incorrect..! ");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                });
                                builder.show();
                            }

//                            if (resp.getString("isUserExist").equals("true")) {
//                                setLogin("true", resp.getString("user_id"), resp.getString("user_name"), resp.getString("user_email"), resp.getString("user_image"));
//                                //     startActivity(new Intent(LoginActivity.this,ElectionActivity.class).putExtra("JSON",jsonResponse));
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                                finish();
//                            }
//                            else
//                            {
//                                ((TextView) findViewById(R.id.login_loader_text)).setText("Email or Pass Incorrect..");
//                                loginLoader.clearAnimation();
//                                loginLoader.setVisibility(View.GONE);
//
//                            }

                        }
                        if(statusCode==500)
                        {
                            prgDialog.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    try {


                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "data faliur : " + responseString);

                        prgDialog.dismiss();
                    }
                    catch (Exception ex) {
                    }
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
    public void setLogin(String login_chk, String headers, String name, String area)
    {
        Log.d("CheckLog","Setting islogin true +headers : " +headers);

        SharedPreferences setting = getSharedPreferences("GTL-Settings",0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("Login",login_chk);
        editor.putString("headers",headers);
        editor.putString("name",name);
        editor.putString("assigned_area",area);

        editor.commit();
        Log.d("CheckLog","header saved login saved.. call the main activity");
    }
}
