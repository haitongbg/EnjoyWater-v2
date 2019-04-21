package com.enjoywater.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.model.Order;
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


public class DialogOrderRating {
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tv_tutorial)
    TextView tvTutorial;
    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    private Context mContext;
    private MainService mainService;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private String mToken;
    private Order mOrder;

    public DialogOrderRating(Context context, Handler callbackHandler, Order order) {
        mContext = context;
        mainService = MyApplication.getInstance().getMainService();
        mCallBackHandler = callbackHandler;
        mOrder = order;
        mToken = Utils.getToken(mContext);
        initUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initUI() {
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_order_rating);
        ButterKnife.bind(DialogOrderRating.this, mDialog);
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
        btnConfirm.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            String comment = edtComment.getText().toString();
            if (rating == 0) Toast.makeText(mContext, "Vui lòng cho chúng tôi biết đánh giá của bạn.", Toast.LENGTH_SHORT).show();
            else {
                rateOrder(rating, comment);
            }
        });
        mDialog.show();
    }

    private void rateOrder(int rating, String comment) {
        progressLoading.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        Call<BaseResponse> rateOrder = mainService.rateOrder(mToken, mOrder.getId(), rating, comment);
        rateOrder.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess()) {
                        Toast.makeText(mContext, "Xin chân thành cảm ơn những đánh giá của bạn!", Toast.LENGTH_SHORT).show();
                        mOrder.setRate(rating);
                        Message message = new Message();
                        message.what = Constants.Value.ACTION_SUCCESS;
                        message.obj = mOrder;
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
