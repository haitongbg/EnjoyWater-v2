package com.enjoywater.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogRetrivePassword {
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tv_tutorial)
    TextView tvTutorial;
    @BindView(R.id.edt_active_code)
    EditText edtActiveCode;
    @BindView(R.id.btn_get_password)
    Button btnGetPassword;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_new_password_confirm)
    EditText edtNewPasswordConfirm;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    private Context mContext;
    private MainService mainService;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private User mUser;
    private String mToken;
    private boolean isLoading, isDelaying;
    private long mDelaySending;

    public DialogRetrivePassword(Context context, Handler callbackHandler, long delaySending) {
        mContext = context;
        mainService = MyApplication.getInstance().getMainService();
        mCallBackHandler = callbackHandler;
        mDelaySending = delaySending;
        mUser = Utils.getUser(mContext);
        mToken = Utils.getToken(mContext);
        initUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initUI() {
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_retrive_password);
        ButterKnife.bind(DialogRetrivePassword.this, mDialog);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up_over_screen);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layoutContent.startAnimation(animation);
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        btnClose.setOnClickListener(v -> {
            Animation endAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
            endAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Message message = new Message();
                    message.what = Constants.Value.ACTION_CLOSE;
                    message.obj = mDelaySending;
                    mCallBackHandler.sendMessage(message);
                    mDialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            layoutContent.startAnimation(endAnimation);
        });
        btnGetPassword.setOnClickListener(v -> {
            getPassword();
        });
        btnChangePassword.setOnClickListener(v -> {
            String code = edtActiveCode.getText().toString();
            if (code.isEmpty()) {
                Toast.makeText(mContext, "Vui lòng nhập mã kích hoạt", Toast.LENGTH_SHORT).show();
            } else {
                changePassword(code);
            }
        });
        if (mDelaySending < 1000) {
            isDelaying = false;
            mDelaySending = 0;
            btnGetPassword.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_green_corner_4));
            btnGetPassword.setTextColor(mContext.getResources().getColor(R.color.white));
            btnGetPassword.setText("Lấy mật khẩu");
        } else delaySending(mDelaySending);
        mDialog.show();
    }

    private void getPassword() {
        if (!isLoading && !isDelaying) {
            isLoading = true;
            progressLoading.setVisibility(View.VISIBLE);
            Call<BaseResponse> getActiveCode = mainService.getActiveCode(mUser.getUserInfo().getEmail());
            getActiveCode.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    isLoading = false;
                    BaseResponse getActivecodeResponse = response.body();
                    if (getActivecodeResponse != null) {
                        if (getActivecodeResponse.isSuccess()) {
                            progressLoading.setVisibility(View.GONE);
                            delaySending(120000);
                        } else {
                            String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                            if (getActivecodeResponse.getError() != null && getActivecodeResponse.getError().getMessage() != null && !getActivecodeResponse.getError().getMessage().isEmpty())
                                message = getActivecodeResponse.getError().getMessage();
                            showError(message);
                        }
                    } else {
                        showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    isLoading = false;
                    t.printStackTrace();
                    showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                }
            });
        }
    }

    private void delaySending(long delayCount) {
        isDelaying = true;
        btnGetPassword.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_grey_corner_4));
        btnGetPassword.setTextColor(mContext.getResources().getColor(R.color.black_9));
        btnGetPassword.setText("Chờ " + delayCount / 1000 + "s");
        new CountDownTimer(delayCount, 1000) {
            public void onTick(long millisUntilFinished) {
                btnGetPassword.setText("Chờ " + millisUntilFinished / 1000 + "s");
                mDelaySending = millisUntilFinished;
            }

            public void onFinish() {
                isDelaying = false;
                mDelaySending = 0;
                btnGetPassword.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_green_corner_4));
                btnGetPassword.setTextColor(mContext.getResources().getColor(R.color.white));
                btnGetPassword.setText("Lấy mật khẩu");
            }
        }.start();
    }

    private void changePassword(String code) {
        isLoading = true;
        progressLoading.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        Call<BaseResponse> active = mainService.activeByCode(mToken, code);
        active.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess()) {
                        Toast.makeText(mContext, "Kích hoạt tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = Constants.Value.ACTION_SUCCESS;
                        mCallBackHandler.sendMessage(message);
                        mDialog.dismiss();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty())
                            message = baseResponse.getError().getMessage();
                        showError(message);
                    }
                } else showError(Constants.DataNotify.DATA_ERROR);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                showError(Constants.DataNotify.DATA_ERROR);
            }
        });
    }

    private void showError(String message) {
        progressLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
