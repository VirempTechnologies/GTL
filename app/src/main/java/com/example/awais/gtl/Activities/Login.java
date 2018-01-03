package com.example.awais.gtl.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * Created by awais on 16/05/2017.
 */
public class Login  extends AppCompatActivity {
    Button loginBtn;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        getSupportActionBar().hide();
        permissionsToRequest = findUnAskedPermissions(permissions);
        //calling the os to take permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
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
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    prgDialog.dismiss();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle);
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
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application to to save your location . Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

}
