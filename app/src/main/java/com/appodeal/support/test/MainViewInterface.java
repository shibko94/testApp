package com.appodeal.support.test;

import android.app.Activity;

import com.appodeal.ads.NativeAd;

import java.util.List;

public interface MainViewInterface {

    void startMainTick();

    void startMinorTick();

    void createList(List<NativeAd> nativeAdList);

    Activity getActivity();
}
