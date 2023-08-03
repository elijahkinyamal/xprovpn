package com.webihostapp.xprofreevpnapp.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.webihostapp.xprofreevpnapp.R;

import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    ImageView ivfaq, ivShare, ivPrivacy, ivAbout;
    ImageView backToActivity;
    TextView activity_name;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        activity_name = findViewById(R.id.activity_name);
        backToActivity = findViewById(R.id.finish_activity);
        activity_name.setText("Menu");
        backToActivity.setOnClickListener(view -> finish());

        ivfaq = findViewById(R.id.imgfaq);
        ivShare = findViewById(R.id.imgshare);
        ivPrivacy = findViewById(R.id.imgprivacy);
        ivAbout = findViewById(R.id.imgabout);

        ivfaq.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this, Faq.class)));

        ivShare.setOnClickListener(view -> {
            Intent ishare = new Intent(Intent.ACTION_SEND);
            ishare.setType("text/plain");
            String sAux = "\n" + getResources().getString(R.string.app_name) + "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();
            ishare.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(ishare, "choose one"));
        });
        ivPrivacy.setOnClickListener(view -> {
            Uri uri = Uri.parse(getResources().getString(R.string.privacy_policy_link)); // missing 'http://' will cause crashed
            Intent intent_policy = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent_policy);
        });
        ivAbout.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.dev_email)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.improve_us_body));

            try {
                startActivity(Intent.createChooser(intent, "send mail"));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(MenuActivity.this, "No mail app found!!!", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(MenuActivity.this, "Unexpected Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

