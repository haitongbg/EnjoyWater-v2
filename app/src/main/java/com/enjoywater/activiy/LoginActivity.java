package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.LoginResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.v_center)
    View vCenter;
    @BindView(R.id.tv_slogan)
    TextView tvSlogan;
    @BindView(R.id.iv_email)
    ImageView ivEmail;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.ripple_retrieve_password)
    RippleView rippleRetrievePassword;
    @BindView(R.id.ripple_login)
    RippleView rippleLogin;
    @BindView(R.id.ripple_register)
    RippleView rippleRegister;
    @BindView(R.id.ripple_skip)
    RippleView rippleSkip;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    private Handler mHandler = new Handler();
    private MainService mainService;
    private User mUser;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        initUI();
        mHandler.removeCallbacks(goMainRunnable);
        mHandler.removeCallbacks(showLoginRunnable);
        mHandler.postDelayed(showLoginRunnable, 2000);
        String jsonUser = Utils.getString(this, Constants.Key.USER, "");
        if (!jsonUser.isEmpty()) {
            mUser = gson.fromJson(jsonUser, User.class);
        }
        if (mUser != null && mUser.getId() != null && !mUser.getId().isEmpty() && mUser.getToken() != null && !mUser.getToken().isEmpty()) {
            mHandler.removeCallbacks(showLoginRunnable);
            mHandler.postDelayed(goMainRunnable, 2000);
        }
    }

    private void initUI() {
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        layoutLoading.setVisibility(View.GONE);
        layoutLogin.setVisibility(View.GONE);
        vCenter.setVisibility(View.VISIBLE);
        tvSlogan.setVisibility(View.VISIBLE);
        rippleLogin.setOnRippleCompleteListener(rippleView -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if (email.isEmpty())
                Toast.makeText(this, R.string.empty_email, Toast.LENGTH_SHORT).show();
            else if (password.isEmpty())
                Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            else login(email, password);
        });
        rippleRegister.setOnRippleCompleteListener(rippleView -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        });
        rippleRetrievePassword.setOnRippleCompleteListener(rippleView -> {

        });
        rippleSkip.setOnRippleCompleteListener(rippleView -> {
            goMain();
        });
    }

    private void login(String email, String password) {
        layoutLoading.setVisibility(View.VISIBLE);
        Call<LoginResponse> login = mainService.login(email, password);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                layoutLoading.setVisibility(View.GONE);
                LoginResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                        String token = loginResponse.getData().getToken();
                        mUser = loginResponse.getData().getUser();
                        if (token != null && !token.isEmpty() && mUser != null && mUser.getId() != null && !mUser.getId().isEmpty()) {
                            mUser.setToken(token);
                            Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                            goMain();
                        } else Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = getResources().getString(R.string.data_error);
                        if (loginResponse.getError() != null && loginResponse.getError().getMessage() != null && !loginResponse.getError().getMessage().isEmpty()) {
                            message = loginResponse.getError().getMessage();
                            if (message.equalsIgnoreCase("User invalid")) message = getResources().getString(R.string.user_invalid);
                            if (message.equalsIgnoreCase("Password invalid")) message = getResources().getString(R.string.password_invalid);
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    private Runnable showLoginRunnable = () -> {
        vCenter.setVisibility(View.GONE);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(250);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvSlogan.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tvSlogan.startAnimation(animation);
    };

    private Runnable goMainRunnable = () -> {
        goMain();
    };

    private void goMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
        finish();
    }
}
