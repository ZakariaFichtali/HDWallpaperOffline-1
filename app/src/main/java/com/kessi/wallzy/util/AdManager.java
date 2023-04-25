package com.kessi.wallzy.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.kessi.wallzy.CustomUtils;
import com.kessi.wallzy.R;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.util.ArrayList;
import java.util.List;

public class AdManager {

    private static final String TAG = "AdManager";


    public interface adFinishedListener {
        void onAdFinished();
    }

    public AdManager() {

    }

    public void loadAds(Activity activity) {
        if (CustomUtils.showAds) {
            if (CustomUtils.interAd.equalsIgnoreCase("unity")) {
                UnityAds.load(CustomUtils.unityInter, new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String placementId) {
                        Log.e(TAG, "onUnityAdsAdLoaded: ");
                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                        Log.e(TAG, "onUnityAdsFailedToLoad: " + message);
                    }
                });
            }
        }
    }

    public void showBannerView(Activity context, RelativeLayout adContainer) {
        if (CustomUtils.showAds) {
            adContainer.setVisibility(View.VISIBLE);
            if (CustomUtils.bannerAd.equalsIgnoreCase("admob")) {

                AdView adView = new AdView(context);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(CustomUtils.admobBanner);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        adContainer.removeAllViews();
                        adContainer.addView(adView);
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                        super.onAdFailedToLoad(adError);
                        adContainer.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "onAdFailedToLoad: " + adError.getMessage());
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                        super.onAdClicked();
                    }
                });

                adView.loadAd(new AdRequest.Builder().build());

            } else if (CustomUtils.bannerAd.equalsIgnoreCase("facebook")) {

                com.facebook.ads.AdView adViewFb = new com.facebook.ads.AdView(context, CustomUtils.fbBanner, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Ad error callback
                        Log.e(TAG, "onError: banner " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Ad loaded callback
                        adContainer.removeAllViews();
                        adContainer.addView(adViewFb);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Ad clicked callback
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Ad impression logged callback
                    }
                };
                adViewFb.loadAd(adViewFb.buildLoadAdConfig().withAdListener(adListener).build());

            } else if (CustomUtils.bannerAd.equalsIgnoreCase("max")) {

                MaxAdView adViewMax = new MaxAdView(CustomUtils.applovinBnr, context);
                adViewMax.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        // Stretch to the width of the screen for banners to be fully functional
                        int width = ViewGroup.LayoutParams.MATCH_PARENT;

                        // Banner height on phones and tablets is 50 and 90, respectively
                        int heightPx = adViewMax.getResources().getDimensionPixelSize(R.dimen.banner_height);

                        adViewMax.setLayoutParams(new RelativeLayout.LayoutParams(width, heightPx));

                        // Set background or background color for banners to be fully functional
                        adViewMax.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

                        adContainer.removeAllViews();
                        adContainer.addView(adViewMax);
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });
                // Load the ad
                adViewMax.loadAd();

            } else if (CustomUtils.bannerAd.equalsIgnoreCase("ironsource")) {
                IronSourceBannerLayout banner = IronSource.createBanner(context, ISBannerSize.SMART);
                banner.setBannerListener(new BannerListener() {
                    @Override
                    public void onBannerAdLoaded() {
                        // Called after a banner ad has been successfully loaded
                        Log.e(TAG, "onBannerAdLoaded: ");
                        adContainer.removeAllViews();
                        adContainer.addView(banner);
                    }

                    @Override
                    public void onBannerAdLoadFailed(IronSourceError error) {
                        // Called after a banner has attempted to load an ad but failed.
                        Log.e(TAG, "onBannerAdLoadFailed: " + error);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adContainer.removeAllViews();
                            }
                        });
                    }

                    @Override
                    public void onBannerAdClicked() {
                        // Called after a banner has been clicked.
                    }

                    @Override
                    public void onBannerAdScreenPresented() {
                        // Called when a banner is about to present a full screen content.
                    }

                    @Override
                    public void onBannerAdScreenDismissed() {
                        // Called after a full screen content has been dismissed
                    }

                    @Override
                    public void onBannerAdLeftApplication() {
                        // Called when a user would be taken out of the application context.
                    }
                });

                IronSource.loadBanner(banner);

            } else if (CustomUtils.bannerAd.equalsIgnoreCase("unity")) {
                BannerView unityBanner = new BannerView(context, CustomUtils.unityBanner, new UnityBannerSize(320, 50));
                unityBanner.setListener(new BannerView.IListener() {
                    @Override
                    public void onBannerLoaded(BannerView bannerAdView) {
                        adContainer.removeAllViews();
                        adContainer.addView(bannerAdView);
                    }

                    @Override
                    public void onBannerClick(BannerView bannerAdView) {

                    }

                    @Override
                    public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                        Log.e(TAG, "onBannerFailedToLoad: " + errorInfo.errorMessage );
                    }

                    @Override
                    public void onBannerLeftApplication(BannerView bannerView) {

                    }
                });
                unityBanner.load();
            }
        } else {
            adContainer.setVisibility(View.GONE);
        }

    }

    public static int currentListPref = 0;
    public static int currentActivityPref = 0;

    public void showInterstitial(Activity context, boolean isList, adFinishedListener onAdFinishedListener) {
        if (CustomUtils.showAds) {

            if (isList) {
                if (currentListPref % CustomUtils.intersitial_click_list != 0) {
                    onAdFinishedListener.onAdFinished();
                    currentListPref += 1;
                    return;
                }
                currentListPref += 1;
            } else {
                if (currentActivityPref % CustomUtils.intersitial_click_activites != 0) {
                    onAdFinishedListener.onAdFinished();
                    currentActivityPref += 1;
                    return;
                }
                currentActivityPref += 1;
            }

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading Ads. Please wait...");
            if (CustomUtils.interAd.equalsIgnoreCase("admob")) {
                progressDialog.show();
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, CustomUtils.admobInter, adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                interstitialAd.show(context);
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        onAdFinishedListener.onAdFinished();
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        onAdFinishedListener.onAdFinished();
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                onAdFinishedListener.onAdFinished();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }
                );
            } else if (CustomUtils.interAd.equalsIgnoreCase("max")) {
                progressDialog.show();
                MaxInterstitialAd interstitialAd = new MaxInterstitialAd(CustomUtils.applovinInter, context);
                interstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        interstitialAd.showAd();
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        Log.e(TAG, "onAdLoadFailed: " + error);
                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        Log.e(TAG, "onAdDisplayFailed: " + error);
                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });

                interstitialAd.loadAd();
            } else if (CustomUtils.interAd.equalsIgnoreCase("facebook")) {
                final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(context, CustomUtils.fbInter);
                com.facebook.ads.InterstitialAdListener interstitialAdListener = new com.facebook.ads.InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        // Interstitial ad displayed callback
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        // Interstitial dismissed callback
                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Interstitial ad is loaded and ready to be displayed
                        // Show the ad
                        interstitialAd.show();
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Ad clicked callback
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Ad impression logged callback
                    }
                };

                // For auto play video ads, it's recommended to load the ad
                // at least 30 seconds before it is shown
                interstitialAd.loadAd(
                        interstitialAd.buildLoadAdConfig()
                                .withAdListener(interstitialAdListener)
                                .build());
            } else if (CustomUtils.interAd.equalsIgnoreCase("ironsource")) {
                IronSource.setInterstitialListener(new InterstitialListener() {
                    /**
                     * Invoked when Interstitial Ad is ready to be shown after load function was called.
                     */
                    @Override
                    public void onInterstitialAdReady() {
                        IronSource.showInterstitial();
                    }

                    /**
                     * invoked when there is no Interstitial Ad available after calling load function.
                     */
                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError error) {
                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    /**
                     * Invoked when the Interstitial Ad Unit is opened
                     */
                    @Override
                    public void onInterstitialAdOpened() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    /**
                     * Invoked when the ad is closed and the user is about to return to the application.
                     */
                    @Override
                    public void onInterstitialAdClosed() {
                        onAdFinishedListener.onAdFinished();
                    }

                    /**
                     * Invoked when Interstitial ad failed to show.
                     * @param error - An object which represents the reason of showInterstitial failure.
                     */
                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError error) {
                        onAdFinishedListener.onAdFinished();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    /**
                     * Invoked when the end user clicked on the interstitial ad, for supported networks only.
                     */
                    @Override
                    public void onInterstitialAdClicked() {
                    }

                    /** Invoked right before the Interstitial screen is about to open.
                     *  NOTE - This event is available only for some of the networks.
                     *  You should NOT treat this event as an interstitial impression, but rather use InterstitialAdOpenedEvent
                     */
                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }
                });
                IronSource.loadInterstitial();
            } else if (CustomUtils.interAd.equalsIgnoreCase("unity")) {
                UnityAds.show(context, CustomUtils.unityInter, new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        Log.e(TAG, "onUnityAdsError: " + message);
                        onAdFinishedListener.onAdFinished();
                        loadAds(context);
                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {

                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {

                    }

                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        onAdFinishedListener.onAdFinished();
                        loadAds(context);
                    }
                });
            } else {
                onAdFinishedListener.onAdFinished();
            }

        } else {
            onAdFinishedListener.onAdFinished();
        }
    }


    public void showSingleNative(Activity activity, LinearLayout mainLayout) {
        if (CustomUtils.showAds) {

            if (CustomUtils.nativeAd.equalsIgnoreCase("admob")) {
                Log.e(TAG, "showSingleNative: " + CustomUtils.admobNative);
                AdLoader adLoader = new AdLoader.Builder(activity, CustomUtils.admobNative)
                        .forNativeAd(ad -> {

                            NativeAdView adView =
                                    (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native, null);
                            populateNativeAdView(ad, adView);
                            mainLayout.setVisibility(View.VISIBLE);
                            mainLayout.removeAllViews();
                            mainLayout.addView(adView);
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                Log.e(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                                mainLayout.setVisibility(View.GONE);
                            }
                        })
                        .build();

                adLoader.loadAd(new AdRequest.Builder().build());
            } else if (CustomUtils.nativeAd.equalsIgnoreCase("facebook")) {
                com.facebook.ads.NativeAd nativefb = new com.facebook.ads.NativeAd(activity, CustomUtils.fbNative);
                NativeAdListener nativeAdListener = new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {
                        // Native ad finished downloading all assets

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Native ad failed to load
                        Log.e(TAG, "onError: native fb " + adError.getErrorMessage());
                        mainLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                        NativeAdLayout adView =
                                (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.fb_native, mainLayout, false);
                        inflateAdNative(activity, nativefb, adView);
                        mainLayout.setVisibility(View.VISIBLE);
                        mainLayout.removeAllViews();
                        mainLayout.addView(adView);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Native ad clicked
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Native ad impression

                    }
                };

                // Request an ad
                nativefb.loadAd(
                        nativefb.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());

            } else if (CustomUtils.nativeAd.equalsIgnoreCase("max")) {
                MaxNativeAdLoader maxNativeAdLoader = new MaxNativeAdLoader(CustomUtils.applovinNative, activity);
                maxNativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
                        params.height = CustomUtils.getDeviceSize(activity, 0) / 3;
                        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        mainLayout.setVisibility(View.VISIBLE);
                        mainLayout.removeAllViews();
                        mainLayout.addView(maxNativeAdView);
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        mainLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNativeAdClicked(MaxAd maxAd) {
                        super.onNativeAdClicked(maxAd);
                    }
                });
                maxNativeAdLoader.loadAd();
            } else {
                mainLayout.setVisibility(View.GONE);
            }
        } else {
            mainLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Populates a {@link NativeAdView} object with data from a given {@link com.facebook.ads.NativeAd}.
     *
     * @param ad     the object containing the ad's assets
     * @param adView the view to be populated
     */
    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd ad, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(ad.getHeadline());
        adView.getMediaView().setMediaContent(ad.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (ad.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(ad.getBody());
        }

        if (ad.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(ad.getCallToAction());
        }

        if (ad.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    ad.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (ad.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(ad.getPrice());
        }

        if (ad.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(ad.getStore());
        }

        if (ad.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(ad.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (ad.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(ad.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(ad);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = ad.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        }
    }

    private void inflateAdNative(Activity activity, com.facebook.ads.NativeAd nativeAd, NativeAdLayout nativeAdLayout) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.facebook_native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        com.facebook.ads.MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, CustomUtils.getDeviceSize(activity, 2) / 4);
        nativeAdMedia.setLayoutParams(params);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, clickableViews);
    }
}
