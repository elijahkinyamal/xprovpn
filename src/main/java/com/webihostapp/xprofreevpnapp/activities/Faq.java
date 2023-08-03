package com.webihostapp.xprofreevpnapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.webihostapp.xprofreevpnapp.R;

import butterknife.ButterKnife;


public class Faq extends AppCompatActivity {

    ImageView backToActivity;
    TextView activity_name;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);

        activity_name = findViewById(R.id.activity_name);
        backToActivity = findViewById(R.id.finish_activity);
        activity_name.setText("Faq");
        backToActivity.setOnClickListener(view -> finish());
    }
}
