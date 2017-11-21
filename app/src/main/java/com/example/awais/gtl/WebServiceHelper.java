package com.example.awais.gtl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;

/**
 * Created by awais on 11/20/2017.
 */

public class WebServiceHelper {
    JSONObject respObject;
    Context context;
    String URL;
    JSONObject params;
    public WebServiceHelper() {

    }
    public JSONObject sendPostRequest(JSONObject params,Context context,String  URL)
    {
        try {


            this.context = context;
            this.URL = URL;
            this.params = params;
            return new RetrieveFeedTask().execute().get();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  null;
    }
    public JSONObject invokeCallAsync(Context context,String URL,JSONObject params){
        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();

            client.post(context ,URL, entity, "application/json",new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started\
                    Log.d(Constants.TAG, "here to start preloader");

                    respObject = new JSONObject();

                    // Set Progress Dialog Text


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    try {


                        super.onSuccess(statusCode, headers, resp);
                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "success data say congo : " + resp.toString());

                        if(statusCode==200) {

                            //prgDialog.dismiss();
                            respObject= resp;
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
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d(Constants.TAG, "status: " + statusCode);
                    Log.d(Constants.TAG, "success data say congo : " + errorResponse.toString());

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
        return  respObject;

    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            Log.d(Constants.TAG, "here to start preloader");

            respObject = new JSONObject();

            // Set Progress Dialog Text

            super.onPreExecute();


        }

        protected JSONObject doInBackground(String... Void) {
            try {
                // Show Progress Dialog

                // Make RESTful webservice call using AsyncHttpClient object
                cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
                SyncHttpClient client = new SyncHttpClient();

                client.post(context ,URL, entity, "application/json",new JsonHttpResponseHandler() {

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

                                //prgDialog.dismiss();
                                respObject= resp;
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
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d(Constants.TAG, "status: " + statusCode);
                        Log.d(Constants.TAG, "success data say congo : " + errorResponse.toString());

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
            return  respObject;

        }

        protected JSONObject onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
         return respObject;
        }
    }
}
