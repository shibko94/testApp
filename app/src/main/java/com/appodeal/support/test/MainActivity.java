package com.appodeal.support.test;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements MainViewInterface {

    int mainAd = 30;
    int bannerAd = 5;
    int firstClickFlag = 0;
    boolean isListReady = false;
    boolean taskFlag = true;
    boolean isButtonClicked = false;

    TextView tv;
    ProgressBar pb;
    Button clickButton;
    Runnable mainTickRunnable, bannerTickRunnable;
    ExecutorService executorService;
    RecyclerView recyclerView;

    Appodealer appodealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        appodealer = new Appodealer(this);

        appodealer.AppodealInits();
        appodealer.setCallbacks();

        Appodeal.show(this, Appodeal.BANNER_TOP);

        tv = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);

        executorService = Executors.newFixedThreadPool(5);

        setText(String.format(Locale.getDefault(), "00:%02d", mainAd));

        pb = findViewById(R.id.progressBar);
        clickButton = findViewById(R.id.clickButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstClickFlag == 0) {
                    firstClickFlag = -1;
                    tv.setTextColor(Color.RED);
                    taskFlag = false;
                }
                if (isListReady) {
                    recyclerView.setVisibility(View.VISIBLE);
                    clickButton.setVisibility(View.GONE);
                } else {
                    isButtonClicked = true;
                    pb.setVisibility(View.VISIBLE);
                    clickButton.setVisibility(View.GONE);
                }
            }
        });

        bannerTickRunnable = new Runnable() {
            @Override
            public void run() {
                while (bannerAd > 0) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bannerAd--;
                }
                Appodeal.hide(getActivity(), Appodeal.BANNER);

            }
        };

        mainTickRunnable = new Runnable() {
            @Override
            public void run() {
                while (mainAd > 0 && taskFlag) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mainAd--;
                    setText(String.format(Locale.getDefault(), "00:%02d", mainAd));
                }

                if (firstClickFlag != -1 && taskFlag) {
                    firstClickFlag = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
                        }
                    });
                }
            }

        };

        startMainTick();
    }

    @Override
    public void createList(List<NativeAd> nativeList) {
        if (isButtonClicked) {
            pb.setVisibility(View.GONE);
            clickButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (!isListReady) {
            isListReady = true;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            List<String> defaultData = new ArrayList<>();
            defaultData.add("Simple item #1");
            defaultData.add("Simple item #2");
            defaultData.add("Next item is NativeAd");

            MyListAdapter adapter = new MyListAdapter(defaultData, nativeList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void startMainTick() {
        mainAd = 30;
        executorService.submit(mainTickRunnable);
    }

    @Override
    public void startMinorTick() {
        executorService.submit(bannerTickRunnable);
    }

    public Activity getActivity() {
        return this;
    }

    public void setText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
