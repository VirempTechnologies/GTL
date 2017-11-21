package com.example.awais.gtl.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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

        RelativeLayout loginLayout = (RelativeLayout) findViewById(R.id.parent_rlv);
        int loginLayoutAnimationId = R.anim.layout_animation_from_left_short_duration;
        LayoutAnimationController loginLayoutAnimation = AnimationUtils.loadLayoutAnimation(this, loginLayoutAnimationId);
        loginLayout.setLayoutAnimation(loginLayoutAnimation);

        loginBtn =(Button)findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(
            new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
//                JSONObject params = new JSONObject();
//                params.put("username", "rashid111");
//                params.put("password", "temperr");
//                Log.d("checklog", "sending request : "+params.toString());
//                invokeWS(params, Login.this);

                startActivity(new Intent(Login.this, SalemanProfile.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Log.e(Constants.TAG,"exception: "+ex.getMessage());
            }
            }
        });
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
                        Log.d(Constants.TAG, "user exist" + resp.getString("isUserExist"));

                        if(statusCode==200) {
                            prgDialog.dismiss();
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
}
