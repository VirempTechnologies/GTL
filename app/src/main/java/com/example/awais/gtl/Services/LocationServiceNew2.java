package com.example.awais.gtl.Services;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.awais.gtl.Constants;
import com.example.awais.gtl.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

//import com.google.android.gms.common.GooglePlayServicesUtil;

public class LocationServiceNew2 extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = Constants.TAG;
    SharedPreferences setting;
    // use the websmithing defaultUploadWebsite for testing and then check your
    // location with your browser here: https://www.websmithing.com/gpstracker/displaymap.php
    private String defaultUploadWebsite;

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        setting = getSharedPreferences("GTL-Settings", 0);


        //  defaultUploadWebsite = getString(R.string.default_upload_website);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if we are currently trying to get a location and the alarm manager has called this again,
        // no need to start processing a new location.
        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    private void startTracking() {
        Log.d(TAG, "startTracking");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }
    }

//    protected void sendLocationDataToWebsite(Location location) {
//
//        // formatted for mysql datetime format
////        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        dateFormat.setTimeZone(TimeZone.getDefault());
////        Date date = new Date(location.getTime());
////
////        SharedPreferences sharedPreferences = this.getSharedPreferences("GTL-Settings", Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////
////        float totalDistanceInMeters = sharedPreferences.getFloat("totalDistanceInMeters", 0f);
////
////        boolean firstTimeGettingPosition = sharedPreferences.getBoolean("firstTimeGettingPosition", true);
////
////        if (firstTimeGettingPosition) {
////            editor.putBoolean("firstTimeGettingPosition", false);
////        } else {
////            Location previousLocation = new Location("");
////            previousLocation.setLatitude(sharedPreferences.getFloat("previousLatitude", 0f));
////            previousLocation.setLongitude(sharedPreferences.getFloat("previousLongitude", 0f));
////
////            float distance = location.distanceTo(previousLocation);
////            totalDistanceInMeters = distance;
////            Log.d("checkLog","meters: "+ totalDistanceInMeters);
////            editor.putFloat("totalDistanceInMeters", totalDistanceInMeters);
////        }
////
////        editor.putFloat("previousLatitude", (float)location.getLatitude());
////        editor.putFloat("previousLongitude", (float)location.getLongitude());
////        editor.apply();
////
////        final RequestParams requestParams = new RequestParams();
////        requestParams.put("latitude", Double.toString(location.getLatitude()));
////        requestParams.put("longitude", Double.toString(location.getLongitude()));
////
////        Double speedInMilesPerHour = location.getSpeed()* 2.2369;
////        requestParams.put("speed",  Integer.toString(speedInMilesPerHour.intValue()));
////
////        try {
////            requestParams.put("date", URLEncoder.encode(dateFormat.format(date), "UTF-8"));
////        } catch (UnsupportedEncodingException e) {}
////
////        requestParams.put("locationmethod", location.getProvider());
////
////        if (totalDistanceInMeters > 0) {
////            requestParams.put("distance", String.format("%.1f", totalDistanceInMeters / 1609)); // in miles,
////        } else {
////            requestParams.put("distance", "0.0"); // in miles
////        }
////
////        requestParams.put("username", sharedPreferences.getString("userName", ""));
////        requestParams.put("phonenumber", sharedPreferences.getString("appID", "")); // uuid
////        requestParams.put("sessionid", sharedPreferences.getString("sessionID", "")); // uuid
////
////        Double accuracyInFeet = location.getAccuracy()* 3.28;
////        requestParams.put("accuracy",  Integer.toString(accuracyInFeet.intValue()));
////
////        Double altitudeInFeet = location.getAltitude() * 3.28;
////        requestParams.put("extrainfo",  Integer.toString(altitudeInFeet.intValue()));
////
////        requestParams.put("eventtype", "android");
////
////        Float direction = location.getBearing();
////        requestParams.put("direction",  Integer.toString(direction.intValue()));
////
////        final String uploadWebsite = sharedPreferences.getString("defaultUploadWebsite", defaultUploadWebsite);
//
////        LoopjHttpClient.get(uploadWebsite, requestParams, new AsyncHttpResponseHandler() {
////            @Override
////            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
////                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - success", uploadWebsite, requestParams, responseBody, headers, statusCode, null);
////                stopSelf();
////            }
////            @Override
////            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
////                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - failure", uploadWebsite, requestParams, errorResponse, headers, statusCode, e);
////                stopSelf();
////            }
////        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "position: " + location.getLatitude() + ", " + location.getLongitude() + " accuracy: " + location.getAccuracy());

            // we have our desired accuracy of 500 meters so lets quit this service,
            // onDestroy will be called and stop our location uodates
            if (location.getAccuracy() < 500.0f) {
                try {
                    //stopLocationUpdates();
                    //send locaton to sercies
                    final String headerss = setting.getString("headers", null);
                    JSONObject headers = new JSONObject(headerss);
                    JSONObject params = new JSONObject();
                    params.put("headers", headers);
                    params.put("longitude", location.getLongitude());
                    params.put("latitude", location.getLatitude());

                    Log.d(Constants.TAG, "sending request : " + params.toString());

                    sendLocationDataToWebsite(params, getApplicationContext());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    private void stopLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(20 *60* 1000); // milliseconds
        locationRequest.setFastestInterval((20 *60* 1000)-(1 *60* 1000)); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient connection has been suspend");
    }

    public void sendLocationDataToWebsite(JSONObject params, final Context context){
        try {
            // Show Progress Dialog

            // Make RESTful webservice call using AsyncHttpClient object
            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(context ,Constants.getSaveSalesmanCordsUrl, entity, "application/json",new JsonHttpResponseHandler() {

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
                            Log.d(Constants.TAG, "status 200");
                            Log.d(Constants.TAG, resp.optString("success"));

                        }
                        else if(statusCode==500)
                        {
                            Log.e(Constants.TAG, "status 500");

                        }
                        else if(statusCode==404)
                        {
                            Log.e(Constants.TAG, "status 404");

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
                    Log.e(Constants.TAG, "failure 1");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e(Constants.TAG, "failure 2");

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
