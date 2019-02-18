package com.enjoywater.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.enjoywater.view.TvSegoeuiSemiBold;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {
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
    @BindView(R.id.layout_error)
    RelativeLayout layoutError;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    public static PersonalFragment newInstance() {
        PersonalFragment homeFragment = new PersonalFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
