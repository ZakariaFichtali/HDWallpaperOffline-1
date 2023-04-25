package com.kessi.wallzy;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.kessi.wallzy.util.AdManager;
import com.onesignal.OneSignal;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

import org.json.JSONException;
import org.json.JSONObject;

public class MyApp extends Application {
    private static final String ONESIGNAL_APP_ID = "95f949ef-98cd-41da-81ca-b753e1ef0829";

    //---------------------------------------------------------------------------------
    RequestQueue requestQueue;
    public static AdManager adManager;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    //---------------------------------------------------------------------------------



    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }

        requestQueue = Volley.newRequestQueue(this);
        adManager = new AdManager();
        
        MobileAds.initialize(this);
        AudienceNetworkAds.initialize(this);


        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this);
        
        
        callAds();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }


    //---------------------------------------------------------------------------------
    private void callAds() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,CustomUtils.jsonURL, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response)
                    {
                        try {

                            JSONObject adsJson = ((JSONObject) response).getJSONObject("Ads");
                            CustomUtils.showAds =  adsJson.getBoolean("show_ads");
                            CustomUtils.bannerAd =  adsJson.getString("bannerAd");
                            CustomUtils.interAd =  adsJson.getString("InterAd");
                            CustomUtils.nativeAd =  adsJson.getString("NativeAd");
                            CustomUtils.admobBanner =  adsJson.getString("admob_banner");
                            CustomUtils.admobInter =  adsJson.getString("admob_inter");
                            CustomUtils.admobNative =  adsJson.getString("admob_native");
                            CustomUtils.fbBanner =  adsJson.getString("facebook_banner");
                            CustomUtils.fbInter =  adsJson.getString("facebook_inter");
                            CustomUtils.fbNative =  adsJson.getString("facebook_native");
                            CustomUtils.applovinBnr =  adsJson.getString("max_banner");
                            CustomUtils.applovinInter =  adsJson.getString("max_inter");
                            CustomUtils.applovinNative =  adsJson.getString("max_native");
                            CustomUtils.ironsource_api = adsJson.getString("ironsource_api");
                            CustomUtils.unityTest = adsJson.getBoolean("unityTest");
                            CustomUtils.unityGameID = adsJson.getString("unityGameID");
                            CustomUtils.unityBanner = adsJson.getString("unityBanner");
                            CustomUtils.unityInter = adsJson.getString("unityInter");
                            CustomUtils.intersitial_click_activites = adsJson.getInt("intersitial_click_activites");
                            CustomUtils.intersitial_click_list = adsJson.getInt("intersitial_click_list");

                            UnityAds.initialize(getApplicationContext(), CustomUtils.unityGameID, CustomUtils.unityTest, new IUnityAdsInitializationListener() {
                                @Override
                                public void onInitializationComplete() {
                                    Log.e(TAG, "onInitializationComplete: " );
                                }

                                @Override
                                public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                                    Log.e(TAG, "onInitializationFailed: " + message );
                                }
                            });


                            CustomUtils.dataIsLoaded = 1;

                        } catch (JSONException e) {
                            CustomUtils.dataIsLoaded = 2;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        CustomUtils.dataIsLoaded = 2;
                    }
                });
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }
    //---------------------------------------------------------------------------------

}
