package com.webihostapp.xprofreevpnapp.utils;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.webihostapp.xprofreevpnapp.Preference;
import com.webihostapp.xprofreevpnapp.BuildConfig;

public class AdMod {

    public static void buildAdBanner(Context context, RelativeLayout linearLayout, int i, final MyAdListener myAdListener) {
        Preference preference = new Preference(context);
        if (!preference.isBooleenPreference(BillConfig.PRIMIUM_STATE)) {
            AdView adView = new AdView(context);
            if (i == 0) {
                adView.setAdSize(AdSize.BANNER);
            } else if (i == 1) {
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            } else if (i != 2) {
                adView.setAdSize(AdSize.SMART_BANNER);
            } else {
                adView.setAdSize(AdSize.LARGE_BANNER);
            }
            adView.setAdUnitId(BuildConfig.GOOGLE_BANNER);
            linearLayout.addView(adView);
            AdRequest build = new AdRequest.Builder().build();
            if (adView.getAdSize() != null || adView.getAdUnitId() != null) {
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        myAdListener.onAdClosed();
                    }


                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        myAdListener.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        myAdListener.onAdLoaded();
                    }
                });
                adView.loadAd(build);
                return;
            }
            adView.setVisibility(View.GONE);
        }
    }


    public interface MyAdListener {
        void onAdClicked();

        void onAdClosed();

        void onAdLoaded();

        void onAdOpened();

        void onFaildToLoad(int i);
    }
}
