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
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogRetrivePassword {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tv_tutorial)
    TextView tvTutorial;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.btn_get_confirm_code)
    Button btnGetConfirmCode;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.edt_confirm_code)
    EditText edtConfirmCode;
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
    private boolean isLoading, isDelaying;
    private long mDelaySending;
    private String mEmail;


    public DialogRetrivePassword(Context context, Handler callbackHandler, long delaySending, String email) {
        mContext = context;
        mainService = MyApplication.getInstance().getMainService();
        mCallBackHandler = callbackHandler;
        mDelaySending = delaySending;
        mEmail = email;
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
        btnGetConfirmCode.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            if (email.isEmpty())
                Toast.makeText(mContext, "Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            else {
                mEmail = email;
                getConfirmCode(email);
            }
        });
        btnChangePassword.setOnClickListener(v -> {
            String code = edtConfirmCode.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String newPasswordRepeated = edtNewPasswordRepeated.getText().toString();
            if (mEmail.isEmpty())
                Toast.makeText(mContext, "Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            else if (code.isEmpty()) {
                Toast.makeText(mContext, "Vui lòng nhập mã xác nhận", Toast.LENGTH_SHORT).show();
            } else if (!Utils.isValidPasswordSimple(newPassword)) {
                Toast.makeText(mContext, "Vui lòng nhập mật khẩu hợp lệ", Toast.LENGTH_SHORT).show();
            } else if (!newPasswordRepeated.equals(newPassword)) {
                Toast.makeText(mContext, "Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else changePassword(mEmail, code, newPassword, newPasswordRepeated);
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
        edtEmail.setText(Utils.isValidEmail(mEmail) ? mEmail : "");
        edtEmail.setSelection(edtEmail.length());
        if (mDelaySending < 1000) {
            isDelaying = false;
            mDelaySending = 0;
            btnGetConfirmCode.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_green_corner_4));
            btnGetConfirmCode.setTextColor(mContext.getResources().getColor(R.color.white));
            btnGetConfirmCode.setText("Lấy mã");
        } else delaySending(mDelaySending);
        mDialog.show();
    }

    private void getConfirmCode(String email) {
        if (!isLoading && !isDelaying) {
            isLoading = true;
            progressLoading.setVisibility(View.VISIBLE);
            Call<BaseResponse> getConfirmCode = mainService.forgetPassword(email);
            getConfirmCode.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    isLoading = false;
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null) {
                        if (baseResponse.isSuccess()) {
                            progressLoading.setVisibility(View.GONE);
                            delaySending(120000);
                        } else {
                            String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                            if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty())
                                message = baseResponse.getError().getMessage();
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
        btnGetConfirmCode.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_grey_corner_4));
        btnGetConfirmCode.setTextColor(mContext.getResources().getColor(R.color.black_9));
        btnGetConfirmCode.setText("Chờ " + delayCount / 1000 + "s");
        new CountDownTimer(delayCount, 1000) {
            public void onTick(long millisUntilFinished) {
                btnGetConfirmCode.setText("Chờ " + millisUntilFinished / 1000 + "s");
                mDelaySending = millisUntilFinished;
            }

            public void onFinish() {
                isDelaying = false;
                mDelaySending = 0;
                btnGetConfirmCode.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_green_corner_4));
                btnGetConfirmCode.setTextColor(mContext.getResources().getColor(R.color.white));
                btnGetConfirmCode.setText("Lấy mã");
            }
        }.start();
    }

    private void changePassword(String email, String code, String newPassword, String newPasswordRepeated) {
        isLoading = true;
        progressLoading.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        Call<BaseResponse> changePassword = mainService.changePassword(email, code, newPassword, newPasswordRepeated);
        changePassword.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess()) {
                        Toast.makeText(mContext, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(Constants.Key.EMAIL, mEmail);
                        hashMap.put(Constants.Key.PASSWORD, newPassword);
                        Message message = new Message();
                        message.what = Constants.Value.ACTION_SUCCESS;
                        message.obj = hashMap;
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
