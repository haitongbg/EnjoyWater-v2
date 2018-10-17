package com.enjoywater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enjoywater.R;
import com.enjoywater.view.TvSegoeuiBold;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSemiBold;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.v_center)
    View vCenter;
    @BindView(R.id.tv_name)
    TvSegoeuiSemiBold tvName;
    @BindView(R.id.tv_dt)
    TvSegoeuiRegular tvDt;
    @BindView(R.id.tv_promote_point)
    TvSegoeuiBold tvPromotePoint;
    @BindView(R.id.layout_promote_point)
    LinearLayout layoutPromotePoint;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rvHome)
    RecyclerView rvHome;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tv_wellcome)
    TvSegoeuiSemiBold tvWellcome;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {

    }
}
