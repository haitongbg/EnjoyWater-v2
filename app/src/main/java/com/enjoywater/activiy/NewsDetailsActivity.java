package com.enjoywater.activiy;


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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.model.News;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailsActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.progress_loading_url)
    ProgressWheel progressLoadingUrl;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.layout_error)
    RelativeLayout layoutError;
    private News mNews;
    private MainService mainService;
    private String mToken;
    private boolean isLoading = false;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);
        initUI();
        mainService = MyApplication.getInstance().getMainService();
        mToken = Utils.getToken(this);
        News news = getIntent().getParcelableExtra(Constants.Key.NEWS);
        if (news != null && news.getContent() != null && !news.getContent().isEmpty())
            setDataNews(news);
        else {
            int newsId = getIntent().getIntExtra(Constants.Key.NEWS_ID, 0);
            if (newsId != 0) {
                showLoading(true);
                getNewsDetails(newsId);
            } else {
                Toast.makeText(this, "Không có thông tin bài viết.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initUI() {
        btnBack.setOnClickListener(view -> onBackPressed());
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        progressLoadingUrl.setProgress(0.5f);
        progressLoadingUrl.setCallback(progress -> {
            if (progress == 0) {
                progressLoadingUrl.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoadingUrl.setProgress(0.0f);
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
                progressLoadingUrl.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressLoadingUrl.setVisibility(View.GONE);
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
    }

    private void setDataNews(News news) {
        showContent();
        mNews = news;
        if (mNews.getTitle() != null && !mNews.getTitle().isEmpty()) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mNews.getTitle());
        } else tvTitle.setVisibility(View.GONE);
        webView.loadUrl(mNews.getContent());
    }

    private void getNewsDetails(int newsId) {
        isLoading = true;
        Call<BaseResponse> getNewsDetails = mainService.getNewsDetails(mToken, newsId);
        getNewsDetails.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess() && baseResponse.getData() != null && baseResponse.getData().isJsonObject()) {
                        News news = gson.fromJson(baseResponse.getData().getAsJsonObject(), News.class);
                        if (news != null && news.getContent() != null && !news.getContent().isEmpty()) {
                            setDataNews(news);
                        } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                        if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty()) {
                            message = baseResponse.getError().getMessage();
                        }
                        showError(message);
                    }
                } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        appbar.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        if (error.equals(Constants.DataNotify.NOT_LOGIN_YET))
            btnLogin.setVisibility(View.VISIBLE);
        else btnLogin.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }
}
