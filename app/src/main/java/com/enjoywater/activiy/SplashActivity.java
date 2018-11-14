package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.enjoywater.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        new Handler().postDelayed(goMainRunnable, 2000);
    }

    private Runnable goMainRunnable = this::goMain;

    private void goMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
        finish();
    }
}
