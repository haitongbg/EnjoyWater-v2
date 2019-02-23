package com.enjoywater.activiy;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.model.News;
import com.enjoywater.utils.Constants;
import com.enjoywater.view.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.web_view)
    WebView webView;
    private News mNews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        mNews = getIntent().getParcelableExtra(Constants.Key.NEWS);
        if (mNews != null && mNews.getContent() != null && !mNews.getContent().isEmpty()) initUI();
        else {
            Toast.makeText(this, "Không có thông tin bài viết.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void initUI() {
        if (mNews.getTitle() != null && !mNews.getTitle().isEmpty()) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mNews.getTitle());
        } else tvTitle.setVisibility(View.GONE);
        btnBack.setOnClickListener(view -> onBackPressed());
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setInitialScale(1);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_NONE, null);
            webView.setWebContentsDebuggingEnabled(true);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadUrl(mNews.getContent());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }
}
