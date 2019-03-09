package com.enjoywater.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.activiy.PersonalActivity;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogChangePassword {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.edt_old_password)
    EditText edtOldPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.btn_visible_new_password)
    ImageView btnVisibleNewPassword;
    @BindView(R.id.edt_new_password_repeated)
    EditText edtNewPasswordRepeated;
    @BindView(R.id.btn_visible_new_password_repeated)
    ImageView btnVisibleNewPasswordRepeated;
    @BindView(R.id.tv_password_tutorial)
    TextView tvPasswordTutorial;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;
    @BindView(R.id.layout_content)
    ConstraintLayout layoutContent;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    private Context mContext;
    private MainService mainService;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private boolean isLoading;
    private String mEmail;
    private Gson gson = new Gson();
    private User mUser;

    public DialogChangePassword(Context context, Handler callbackHandler, String email) {
        mContext = context;
        mainService = MyApplication.getInstance().getMainService();
        mCallBackHandler = callbackHandler;
        mEmail = email;
        initUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initUI() {
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_change_password);
        ButterKnife.bind(DialogChangePassword.this, mDialog);
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
        btnChangePassword.setOnClickListener(v -> {
            String oldPassword = edtOldPassword.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String newPasswordRepeated = edtNewPasswordRepeated.getText().toString();
            if (!Utils.isValidPasswordSimple(oldPassword)) {
                Toast.makeText(mContext, "Mật khẩu hiện tại không hợp lệ.", Toast.LENGTH_SHORT).show();
            } else if (!Utils.isValidPasswordSimple(newPassword)) {
                Toast.makeText(mContext, "Mật khẩu mới không hợp lệ.", Toast.LENGTH_SHORT).show();
            } else if(newPassword.equals(oldPassword)) {
                Toast.makeText(mContext, "Mật khẩu mới không được trùng mật khẩu hiện tại.", Toast.LENGTH_SHORT).show();
            } else if (!newPasswordRepeated.equals(newPassword)) {
                Toast.makeText(mContext, "Xác nhận mật khẩu mới không khớp.", Toast.LENGTH_SHORT).show();
            } else checkPassword(mEmail, oldPassword, newPassword);
        });
        btnVisibleNewPassword.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edtNewPassword.setTypeface(Typeface.SANS_SERIF);
                    edtNewPassword.setSelection(edtNewPassword.length());
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtNewPassword.setTypeface(Typeface.SANS_SERIF);
                    edtNewPassword.setSelection(edtNewPassword.length());
                    break;
                }
                case MotionEvent.ACTION_OUTSIDE: {
                    edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtNewPassword.setTypeface(Typeface.SANS_SERIF);
                    edtNewPassword.setSelection(edtNewPassword.length());
                    break;
                }
            }
            return true;
        });
        btnVisibleNewPasswordRepeated.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    edtNewPasswordRepeated.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edtNewPasswordRepeated.setTypeface(Typeface.SANS_SERIF);
                    edtNewPasswordRepeated.setSelection(edtNewPasswordRepeated.length());
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    edtNewPasswordRepeated.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtNewPasswordRepeated.setTypeface(Typeface.SANS_SERIF);
                    edtNewPasswordRepeated.setSelection(edtNewPasswordRepeated.length());
                    break;
                }
                case MotionEvent.ACTION_OUTSIDE: {
                    edtNewPasswordRepeated.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtNewPasswordRepeated.setTypeface(Typeface.SANS_SERIF);
                    edtNewPasswordRepeated.setSelection(edtNewPasswordRepeated.length());
                    break;
                }
            }
            return true;
        });
        mDialog.show();
    }

    private void checkPassword(String email, String password, String newPassword) {
        if (!isLoading) {
            isLoading = true;
            progressLoading.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
            Call<BaseResponse> login = mainService.login(email, password);
            login.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    isLoading = false;
                    BaseResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                            if (loginResponse.getData().isJsonObject()) {
                                mUser = gson.fromJson(loginResponse.getData().getAsJsonObject(), User.class);
                                if (mUser != null && mUser.getToken() != null && !mUser.getToken().isEmpty() && mUser.getUserInfo() != null) {
                                    Utils.saveUser(mContext, mUser);
                                    changePassword(Utils.getToken(mContext), newPassword);
                                } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                            } else
                                showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                        } else {
                            String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                            if (loginResponse.getError() != null && loginResponse.getError().getMessage() != null && !loginResponse.getError().getMessage().isEmpty()) {
                                message = loginResponse.getError().getMessage();
                                if (message.equalsIgnoreCase("Invalid email"))
                                    message = mContext.getResources().getString(R.string.email_invalid);
                                if (message.equalsIgnoreCase("User invalid"))
                                    message = mContext.getResources().getString(R.string.user_invalid);
                                if (message.equalsIgnoreCase("Password invalid"))
                                    message = mContext.getResources().getString(R.string.password_wrong);
                            }
                            showError(message);
                        }
                    } else
                        showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
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

    private void changePassword(String token, String newPassword) {
        isLoading = true;
        Call<BaseResponse> changePassword = mainService.changePassword(token, newPassword);
        changePassword.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                        if (baseResponse.getData().isJsonObject()) {
                            User.UserInfo userInfo = gson.fromJson(baseResponse.getData().getAsJsonObject(), User.UserInfo.class);
                            if (userInfo != null) {
                                Toast.makeText(mContext, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                mUser.setUserInfo(userInfo);
                                Utils.saveUser(mContext, mUser);
                                Message message = new Message();
                                message.what = Constants.Value.ACTION_SUCCESS;
                                message.obj = mUser;
                                mCallBackHandler.sendMessage(message);
                                mDialog.dismiss();
                            } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                        } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                        if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty()) {
                            message = baseResponse.getError().getMessage();
                        }
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
