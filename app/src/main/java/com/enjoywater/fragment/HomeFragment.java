package com.enjoywater.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.enjoywater.R;
import com.enjoywater.adapter.home.HomeAdapter;
import com.enjoywater.model.User;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.TvSegoeuiBold;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.v_center)
    View vCenter;
    @BindView(R.id.tv_name)
    TvSegoeuiSemiBold tvName;
    @BindView(R.id.tv_promote_point)
    TvSegoeuiBold tvPromotePoint;
    @BindView(R.id.layout_promote_point)
    LinearLayout layoutPromotePoint;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rvHome)
    RecyclerView rvHome;
    @BindView(R.id.tv_wellcome)
    TvSegoeuiSemiBold tvWellcome;
    private Context mContext;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mUser = Utils.getUser(mContext);
        mToken = Utils.getString(mContext, Constants.Key.TOKEN, "");
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
        if (mUser != null && mUser.getId() != null && !mUser.getId().isEmpty() && mToken != null && !mToken.isEmpty()) {
            tvWellcome.setVisibility(View.GONE);
            tvName.setVisibility(View.VISIBLE);
            layoutPromotePoint.setVisibility(View.VISIBLE);
            String name = mUser.getName();
            if (name != null && !name.isEmpty()) tvName.setText(name);
            else tvName.setText(R.string.guest);
            String avatar = mUser.getAvatar();
            if (avatar != null && !avatar.isEmpty()) Glide.with(mContext).load(avatar).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    ivAvatar.setImageResource(R.drawable.ic_logo);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(ivAvatar);
        } else {
            tvWellcome.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
            layoutPromotePoint.setVisibility(View.GONE);
            ivAvatar.setImageResource(R.drawable.ic_logo);
        }
        rvHome.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvHome.setAdapter(new HomeAdapter(mContext, new ArrayList<>()));
    }
}
