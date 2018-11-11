package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.ripple_forget_password)
    RippleView rippleForgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_login_by_google)
    RelativeLayout btnLoginByGoogle;
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
        String jsonUser = Utils.getString(this, Constants.Key.USER, "");
        if (!jsonUser.isEmpty()) {
            mUser = gson.fromJson(jsonUser, User.class);
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
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if (email.isEmpty())
                Toast.makeText(this, R.string.empty_email, Toast.LENGTH_SHORT).show();
            else if (password.isEmpty())
                Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            else login(email, password);
        });
        btnLoginByGoogle.setOnClickListener(view -> {
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
                        } else
                            Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = getResources().getString(R.string.data_error);
                        if (loginResponse.getError() != null && loginResponse.getError().getMessage() != null && !loginResponse.getError().getMessage().isEmpty()) {
                            message = loginResponse.getError().getMessage();
                            if (message.equalsIgnoreCase("User invalid"))
                                message = getResources().getString(R.string.user_invalid);
                            if (message.equalsIgnoreCase("Password invalid"))
                                message = getResources().getString(R.string.password_invalid);
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

    private void goMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
        finish();
    }
}
