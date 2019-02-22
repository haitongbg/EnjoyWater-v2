package com.enjoywater.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalFragment extends Fragment {
    private static final String TAG = "PersonalFragment";
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TvSegoeuiSemiBold tvName;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.iv_coin)
    ImageView ivCoin;
    @BindView(R.id.tv_coin)
    TvSegoeuiSemiBold tvCoin;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.text_user_code)
    TextView textUserCode;
    @BindView(R.id.tv_user_code)
    TvSegoeuiSemiBold tvUserCode;
    @BindView(R.id.btn_user_code)
    ConstraintLayout btnUserCode;
    @BindView(R.id.text_send_code)
    TextView textSendCode;
    @BindView(R.id.btn_send_code)
    ConstraintLayout btnSendCode;
    @BindView(R.id.text_submit_code)
    TextView textSubmitCode;
    @BindView(R.id.btn_submit_code)
    ConstraintLayout btnSubmitCode;
    @BindView(R.id.text_rate_app)
    TextView textRateApp;
    @BindView(R.id.btn_rate_app)
    ConstraintLayout btnRateApp;
    @BindView(R.id.text_cskh)
    TextView textCskh;
    @BindView(R.id.btn_cskh)
    ConstraintLayout btnCskh;
    @BindView(R.id.text_email)
    TextView textEmail;
    @BindView(R.id.btn_email)
    ConstraintLayout btnEmail;
    @BindView(R.id.text_website)
    TextView textWebsite;
    @BindView(R.id.btn_website)
    ConstraintLayout btnWebsite;
    @BindView(R.id.text_logout)
    TextView textLogout;
    @BindView(R.id.btn_logout)
    ConstraintLayout btnLogout;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.btn_promotion)
    RippleView btnPromotion;
    @BindView(R.id.layout_content)
    ConstraintLayout layoutContent;
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
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private Context mContext;
    private MainService mainService;
    private String mToken;
    private Gson gson = new Gson();
    private boolean isLoading = false;

    public static PersonalFragment newInstance() {
        PersonalFragment homeFragment = new PersonalFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mainService = MyApplication.getInstance().getMainService();
        mToken = Utils.getToken(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        isLoading = true;
        showLoading(false);
        Utils.clearUser(mContext);
        String deviceId = Utils.getDeviceUuid(mContext);
        String devicetoken = Utils.getDeviceToken(mContext);
        Call<BaseResponse> registerDevice = mainService.registerDevice("", deviceId, devicetoken, Constants.Value.ANDROID, true);
        registerDevice.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                reStart();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
                reStart();
            }
        });

    }

    private void reStart() {
        startActivity(getActivity().getIntent());
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            layoutContent.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        appbar.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        if (error.equals(getString(R.string.not_login_yet)))
            btnLogin.setVisibility(View.VISIBLE);
        else btnLogin.setVisibility(View.GONE);
    }
}
