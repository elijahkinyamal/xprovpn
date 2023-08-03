package com.webihostapp.xprofreevpnapp.activities;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.widget.TextView;


import com.webihostapp.xprofreevpnapp.BuildConfig;
import com.webihostapp.xprofreevpnapp.Preference;
import com.webihostapp.xprofreevpnapp.R;
import com.webihostapp.xprofreevpnapp.utils.NetworkStateUtility;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static com.webihostapp.xprofreevpnapp.utils.BillConfig.IN_PURCHASE_KEY;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.One_Month_Sub;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.One_Year_Sub;
import static com.webihostapp.xprofreevpnapp.utils.BillConfig.Six_Month_Sub;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    Preference preference;



    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView version = findViewById(R.id.Version);
        version.setText(getString(R.string.version) + packageInfo.versionName);
        preference = new Preference(SplashActivity.this);
        preference.setStringpreference(IN_PURCHASE_KEY, BuildConfig.IN_APPKEY);
        preference.setStringpreference(One_Month_Sub, BuildConfig.MONTHLY);
        preference.setStringpreference(Six_Month_Sub, BuildConfig.SIX_MONTH);
        preference.setStringpreference(One_Year_Sub, BuildConfig.YEARLY);


        if (NetworkStateUtility.isOnline(this)) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }, 3000);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.network_error))
                    .setMessage(getString(R.string.network_error_message))
                    .setNegativeButton(getString(R.string.ok),
                            (dialog, id) -> {
                                dialog.cancel();
                                onBackPressed();
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
