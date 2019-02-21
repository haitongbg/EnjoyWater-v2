package com.enjoywater.activiy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN_FROM_SPLASH = 112;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    private MainService mainService;
    private Handler mHandler = new Handler();
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        mHandler.postDelayed(goMainRunnable, 2000);
        mToken = Utils.getToken(this);
        if (!mToken.isEmpty()) loginByToken(mToken);
        else registeDevice("");
    }

    private Runnable goMainRunnable = this::goMain;

    private void goMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
        finish();
    }

    private void loginByToken(String token) {
        Call<BaseResponse> login = mainService.loginByToken(token);
        login.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                mHandler.removeCallbacks(goMainRunnable);
                BaseResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                        if (loginResponse.getData().isJsonObject()) {
                            mUser = gson.fromJson(loginResponse.getData().getAsJsonObject(), User.class);
                            if (mUser != null && mUser.getToken() != null && !mUser.getToken().isEmpty() && mUser.getUserInfo() != null) {
                                Utils.saveUser(SplashActivity.this, mUser);
                                registeDevice(mUser.getUserInfo().getId());
                            } else showErrorLogin();
                        } else showErrorLogin();
                    } else showErrorLogin();
                } else showErrorLogin();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showErrorLogin() {
        Utils.clearUser(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Phiên đăng nhập của bạn đã hết hạn, bạn có muốn đăng nhập lại không?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(SplashActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN_FROM_SPLASH);
                        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
                    }
                })
                .setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        goMain();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void registeDevice(String userId) {
        mHandler.removeCallbacks(goMainRunnable);
        String deviceId = Utils.getDeviceUuid(this);
        String devicetoken = Utils.getDeviceToken(this);
        Call<BaseResponse> registerDevice = mainService.registerDevice(userId, deviceId, devicetoken, Constants.Value.ANDROID, true);
        registerDevice.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                mHandler.postDelayed(goMainRunnable, 1000);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
                mHandler.postDelayed(goMainRunnable, 1000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN_FROM_SPLASH && resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS) {
            Toast.makeText(this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(goMainRunnable, 500);
    }
}
