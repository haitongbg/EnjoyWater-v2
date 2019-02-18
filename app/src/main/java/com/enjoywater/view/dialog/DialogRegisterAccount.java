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
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogRegisterAccount {

    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tv_tutorial)
    TextView tvTutorial;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.tv_password_tutorial)
    TextView tvPasswordTutorial;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    private Context mContext;
    private MainService mainService;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private boolean isLoading;
    private Gson gson = new Gson();

    public DialogRegisterAccount(Context context, Handler callbackHandler) {
        mContext = context;
        mainService = MyApplication.getInstance().getMainService();
        mCallBackHandler = callbackHandler;
        initUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initUI() {
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_register_account);
        ButterKnife.bind(DialogRegisterAccount.this, mDialog);
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
                    mCallBackHandler.sendMessage(message);
                    mDialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            layoutContent.startAnimation(endAnimation);
        });
        btnRegister.setOnClickListener(v -> {
            if (!isLoading) {
                preRegister();
            }
        });
        mDialog.show();
    }

    private void preRegister() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();
        if (name.isEmpty())
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
        else if (!Utils.isValidEmail(email))
            Toast.makeText(mContext, R.string.email_invalid, Toast.LENGTH_SHORT).show();
        else if (!Utils.isValidPhone(phone))
            Toast.makeText(mContext, R.string.phone_invalid, Toast.LENGTH_SHORT).show();
        else if (!Utils.isValidPasswordSimple(password))
            Toast.makeText(mContext, R.string.password_invalid, Toast.LENGTH_SHORT).show();
        else register(name, email, phone, password);
    }

    private void register(String name, String email, String phone, String password) {
        isLoading = true;
        progressLoading.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        Call<BaseResponse> register = mainService.register(name, email, phone, password);
        register.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                        if (baseResponse.getData().isJsonObject()) {
                            User user = gson.fromJson(baseResponse.getData().getAsJsonObject(), User.class);
                            if (user != null && user.getToken() != null && !user.getToken().isEmpty() && user.getUserInfo() != null) {
                                Message message = new Message();
                                message.what = Constants.Value.ACTION_SUCCESS;
                                message.obj = user;
                                mCallBackHandler.sendMessage(message);
                                mDialog.dismiss();
                            } else showError(Constants.DataNotify.DATA_ERROR);
                        } else
                            showError(Constants.DataNotify.DATA_ERROR);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty()) {
                            message = baseResponse.getError().getMessage();
                        }
                        showError(message);
                    }
                } else
                    showError(Constants.DataNotify.DATA_ERROR);
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
