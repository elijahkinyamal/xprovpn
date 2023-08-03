package com.webihostapp.xprofreevpnapp.activities;

import static com.webihostapp.xprofreevpnapp.utils.BillConfig.INAPPSKUUNIT;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.IN_PURCHASE_KEY;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.One_Month_Sub;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.One_Year_Sub;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.PRIMIUM_STATE;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.PURCHASETIME;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.Six_Month_Sub;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aemerse.iap.DataWrappers;
import com.aemerse.iap.IapConnector;
import com.aemerse.iap.SubscriptionServiceListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.webihostapp.xprofreevpnapp.BuildConfig;
import com.webihostapp.xprofreevpnapp.Preference;
import com.webihostapp.xprofreevpnapp.R;
import com.webihostapp.xprofreevpnapp.utils.AdMod;
import com.webihostapp.xprofreevpnapp.utils.BillConfig;
import com.webihostapp.xprofreevpnapp.utils.Converter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unified.vpn.sdk.Callback;
import unified.vpn.sdk.RemainingTraffic;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.VpnException;
import unified.vpn.sdk.VpnState;


public abstract class UIActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final String TAG = MainActivity.class.getSimpleName();
    public String SKU_DELAROY_MONTHLY;
    public String SKU_DELAROY_SIXMONTH;
    public String SKU_DELAROY_YEARLY;
    public String base64EncodedPublicKey;


    private static InterstitialAd mInterstitialAd;

    @BindView(R.id.server_ip)
    TextView server_ip;
    @BindView(R.id.img_connect)
    ImageView img_connect;
    @BindView(R.id.connection_state)
    ImageView connectionStateTextView;

    @BindView(R.id.optimal_server_btn)
    LinearLayout currentServerBtn;
    @BindView(R.id.selected_server)
    TextView selectedServerTextView;
    @BindView(R.id.country_flag)
    ImageView country_flag;
    @BindView(R.id.uploading_speed)
    TextView uploading_speed_textview;
    @BindView(R.id.downloading_speed)
    TextView downloading_speed_textview;


    @BindView(R.id.premium)
    ImageView premium;
    Toolbar toolbar;
    Preference preference;
    boolean mSubscribedToDelaroy = false;
    boolean connected = false;
    String mDelaroySku = "";
    boolean mAutoRenewEnabled = false;


    int[] Onconnect = {R.drawable.ic_on};
    int[] Ondisconnect = {R.drawable.ic_off};
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    final Runnable mUIUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, 10000);
        }
    };


    protected abstract void isLoggedIn(Callback<Boolean> callback);

    protected abstract void loginToVpn();

    protected abstract void isConnected(Callback<Boolean> callback);

    protected abstract void connectToVpn();

    protected abstract void disconnectFromVnp();

    protected abstract void chooseServer();

    protected abstract void getCurrentServer(Callback<String> callback);

    protected abstract void checkRemainingTraffic();

    void complain(String message) {
        alert("Error: " + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    private void unlockdata() {
        if (mSubscribedToDelaroy) {
            unlock();
        } else {
            preference.setBooleanpreference(PRIMIUM_STATE, false);
        }
        if (!preference.isBooleenPreference(PRIMIUM_STATE)) {
            premium.setVisibility(View.VISIBLE);

        } else {
            premium.setVisibility(View.GONE);

        }


        MobileAds.initialize(this, initializationStatus -> {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.d("MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }
            LoadBannerAd();
            LoadInterstitialAd();
        });


    }

    public void unlock() {
        preference.setBooleanpreference(PRIMIUM_STATE, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loginToVpn();
        ImageView img_rate = findViewById(R.id.imgrate);
        img_rate.setOnClickListener(view -> {
            Uri uri = Uri.parse("market://details?id=" + UIActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + UIActivity.this.getPackageName())));
            }
        });
        ImageView img_menu = findViewById(R.id.imgmenu);
        img_menu.setOnClickListener(view -> startActivity(new Intent(UIActivity.this, MenuActivity.class)));

        preference = new Preference(this);
        if (BuildConfig.USE_IN_APP_PURCHASE) {
            base64EncodedPublicKey = preference.getStringpreference(IN_PURCHASE_KEY, base64EncodedPublicKey);
            SKU_DELAROY_MONTHLY = preference.getStringpreference(One_Month_Sub, SKU_DELAROY_MONTHLY);
            SKU_DELAROY_SIXMONTH = preference.getStringpreference(Six_Month_Sub, SKU_DELAROY_SIXMONTH);
            SKU_DELAROY_YEARLY = preference.getStringpreference(One_Year_Sub, SKU_DELAROY_YEARLY);


            ArrayList<String> nonConsumableKeys = new ArrayList<>();

            ArrayList<String> consumableKeys = new ArrayList<>();

            ArrayList<String> subscriptionKeys = new ArrayList<>();
            subscriptionKeys.add(SKU_DELAROY_MONTHLY);
            subscriptionKeys.add(SKU_DELAROY_SIXMONTH);
            subscriptionKeys.add(SKU_DELAROY_YEARLY);

            IapConnector iapConnector = new IapConnector(
                    this,
                    nonConsumableKeys,
                    consumableKeys,
                    subscriptionKeys,
                    base64EncodedPublicKey,
                    true
            );

            unlockdata();
            iapConnector.addSubscriptionListener(new SubscriptionServiceListener() {
                @Override
                public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                    Log.e("Subscribe", "yes" + purchaseInfo.getSku());
                    if (purchaseInfo.getSku().equals(SKU_DELAROY_MONTHLY) && purchaseInfo.isAutoRenewing()) {
                        mDelaroySku = SKU_DELAROY_MONTHLY;
                        mAutoRenewEnabled = true;
                        mSubscribedToDelaroy = true;
                    } else if (purchaseInfo.getSku().equals(SKU_DELAROY_SIXMONTH) && purchaseInfo.isAutoRenewing()) {
                        mDelaroySku = SKU_DELAROY_SIXMONTH;
                        mAutoRenewEnabled = true;
                        mSubscribedToDelaroy = true;
                    } else if (purchaseInfo.getSku().equals(SKU_DELAROY_YEARLY) && purchaseInfo.isAutoRenewing()) {
                        mDelaroySku = SKU_DELAROY_YEARLY;
                        mAutoRenewEnabled = true;
                        mSubscribedToDelaroy = true;
                    } else {
                        mDelaroySku = "";
                        mAutoRenewEnabled = false;
                        mSubscribedToDelaroy = false;
                    }

                    if (!mDelaroySku.equals("")) {
                        preference.setStringpreference(INAPPSKUUNIT, mDelaroySku);
                        preference.setLongpreference(PURCHASETIME, purchaseInfo.getPurchaseTime());
                    }
                    unlockdata();

                }

                @Override
                public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {


                }

                @Override
                public void onPricesUpdated(@NonNull Map<String, DataWrappers.SkuDetails> map) {

                }
            });

        } else {
            preference.setBooleanpreference(PRIMIUM_STATE, false);
            premium.setVisibility(View.GONE);


            MobileAds.initialize(this, initializationStatus -> {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }
                LoadInterstitialAd();
                LoadBannerAd();
            });


        }


    }


    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    startUIUpdateTask();
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUIUpdateTask();
    }

    @OnClick(R.id.premium)
    public void premiumMenu(View v) {
        startActivity(new Intent(this, GetPremiumActivity.class));
    }

    @OnClick(R.id.img_connect)
    public void onConnectBtnClick(View v) {
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    disconnectFromVnp();
                } else {
                    connectToVpn();
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
    }

    @OnClick(R.id.optimal_server_btn)
    public void onServerChooserClick(View v) {
        //showInterstial();
        chooseServer();
    }


    protected void startUIUpdateTask() {
        stopUIUpdateTask();
        mUIHandler.post(mUIUpdateRunnable);
    }

    protected void stopUIUpdateTask() {
        mUIHandler.removeCallbacks(mUIUpdateRunnable);
        updateUI();
    }


    protected void updateUI() {
        UnifiedSdk.getVpnState(new Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState vpnState) {
                switch (vpnState) {
                    case IDLE: {
                        Log.e(TAG, "success: IDLE");
                        connectionStateTextView.setImageResource(R.drawable.disc);
                        /*getip();*/
                        if (connected) {
                            connected = false;
                            animate(img_connect, Ondisconnect, 0, false);
                        }
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        ChangeBlockVisibility();
                        uploading_speed_textview.setText("");
                        downloading_speed_textview.setText("");

                        hideConnectProgress();
                        break;
                    }
                    case CONNECTED: {
                        Log.e(TAG, "success: CONNECTED");
                        if (!connected) {
                            connected = true;
                            animate(img_connect, Onconnect, 0, false);
                        }
                        connectionStateTextView.setImageResource(R.drawable.conne);
                        hideConnectProgress();
                        break;
                    }
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS: {
                        connectionStateTextView.setImageResource(R.drawable.connecting);
                        ChangeBlockVisibility();
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        showConnectProgress();
                        break;
                    }
                    case PAUSED: {
                        Log.e(TAG, "success: PAUSED");
                        ChangeBlockVisibility();
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        break;
                    }
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                selectedServerTextView.setText(R.string.select_country);
            }
        });
        getCurrentServer(new Callback<String>() {
            @Override
            public void success(@NonNull final String currentServer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        if (!currentServer.equals("")) {
                            Locale locale = new Locale("", currentServer);
                            Resources resources = getResources();
                            String sb = "drawable/" + currentServer.toLowerCase();
                            country_flag.setImageResource(resources.getIdentifier(sb, null, getPackageName()));
                            selectedServerTextView.setText(locale.getDisplayCountry());
                        } else {
                            country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                            selectedServerTextView.setText(R.string.select_country);
                        }
                    }
                });
            }

            @Override
            public void failure(@NonNull VpnException e) {
                country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                selectedServerTextView.setText(R.string.select_country);
            }
        });
    }

    private void ChangeBlockVisibility() {
        if (BuildConfig.USE_IN_APP_PURCHASE) {
            if (preference.isBooleenPreference(PRIMIUM_STATE)) {
                premium.setVisibility(View.GONE);
            } else {
                premium.setVisibility(View.VISIBLE);
            }
        } else {
            premium.setVisibility(View.GONE);
        }
    }

    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {


        int fadeInDuration = 500;
        int timeBetween = 3000;
        int fadeOutDuration = 1000;

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);

        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1, forever); //Calls itself until it gets to the end of the array
                } else {
                    if (forever) {
                        animate(imageView, images, 0, forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }


    protected void updateTrafficStats(long outBytes, long inBytes) {
        String outString = Converter.humanReadableByteCountOld(outBytes, false);
        String inString = Converter.humanReadableByteCountOld(inBytes, false);

        uploading_speed_textview.setText(inString);
        downloading_speed_textview.setText(outString);

    }

    protected void updateRemainingTraffic(RemainingTraffic remainingTrafficResponse) {
        if (remainingTrafficResponse.isUnlimited()) {

        } else {
            String trafficUsed = Converter.megabyteCount(remainingTrafficResponse.getTrafficUsed()) + "Mb";
            String trafficLimit = Converter.megabyteCount(remainingTrafficResponse.getTrafficLimit()) + "Mb";

        }
    }

    protected void ShowIPaddera(String ipaddress) {
        server_ip.setText(ipaddress);
    }


    protected void showConnectProgress() {

    }

    protected void hideConnectProgress() {

    }

    protected void showMessage(String msg) {
        Toast.makeText(UIActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    public void LoadBannerAd() {
        RelativeLayout adContainer = findViewById(R.id.adView);
        if (BuildConfig.GOOGlE_AD) {
            AdMod.buildAdBanner(getApplicationContext(), adContainer, 0, new AdMod.MyAdListener() {
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onFaildToLoad(int i) {
                }
            });
        }
    }

    private void LoadInterstitialAd() {
        if (BuildConfig.GOOGlE_AD) {
            Preference preference = new Preference(UIActivity.this);
            if (!preference.isBooleenPreference(BillConfig.PRIMIUM_STATE)) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(this, (BuildConfig.GOOGLE_INTERSTITIAL), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.e(TAG, "onAdLoaded");
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.e("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.e("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
            }
        }
    }


    public void showInterstial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(UIActivity.this);
        }
    }
}