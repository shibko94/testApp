package com.appodeal.support.test;

import android.app.Activity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;


public class Appodealer {

    private String appKey = "f6b5dc1a1860abc7b9d2276ace762370b510e6cbff1ef88c";

    private Activity parent;
    private MainViewInterface mainViewInterface;

    public Appodealer(MainViewInterface mainViewInterface) {
        parent = mainViewInterface.getActivity();
        this.mainViewInterface = mainViewInterface;
    }


    public void AppodealInits() {
        Appodeal.disableLocationPermissionCheck();
        Appodeal.setTesting(true);
        Appodeal.initialize(parent, appKey, Appodeal.BANNER | Appodeal.INTERSTITIAL | Appodeal.NATIVE);
    }

    public void setCallbacks() {
        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int height, boolean isPrecache) {
            }

            @Override
            public void onBannerFailedToLoad() {
            }

            @Override
            public void onBannerShown() {
                mainViewInterface.startMinorTick();
            }

            @Override
            public void onBannerClicked() {
            }
        });

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
            }

            @Override
            public void onInterstitialFailedToLoad() {
            }

            @Override
            public void onInterstitialShown() {
            }

            @Override
            public void onInterstitialClicked() {
            }

            @Override
            public void onInterstitialClosed() {
                mainViewInterface.startMainTick();
            }
        });

        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                mainViewInterface.createList(Appodeal.getNativeAds(1));
            }

            @Override
            public void onNativeFailedToLoad() {
            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {
            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {
            }
        });
    }


}
