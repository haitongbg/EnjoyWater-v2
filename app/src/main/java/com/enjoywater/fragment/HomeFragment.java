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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.adapter.home.HomeAdapter;
import com.enjoywater.model.News;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.TvSegoeuiBold;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_wellcome)
    TvSegoeuiSemiBold tvWellcome;
    @BindView(R.id.btn_event)
    ImageView btnEvent;
    @BindView(R.id.tv_user_name)
    TvSegoeuiSemiBold tvUserName;
    @BindView(R.id.tv_user_type)
    TvSegoeuiSemiBold tvUserType;
    @BindView(R.id.v_separate_dot)
    View vSeparateDot;
    @BindView(R.id.iv_coin)
    ImageView ivCoin;
    @BindView(R.id.tv_coin)
    TvSegoeuiBold tvCoin;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rvHome)
    RecyclerView rvHome;
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
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private int mPageIndex = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;
    private ArrayList<Object> mHomes = new ArrayList<>();
    private HomeAdapter mHomeAdapter;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(mContext);
        mToken = Utils.getToken(mContext);
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
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(() -> {
            if (Utils.isInternetOn(mContext)) {
                if (!isLoading) {
                    mPageIndex = 1;
                    mHomes.clear();
                    if (mHomeAdapter != null) mHomeAdapter.notifyDataSetChanged();
                    getDataSale();
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            } else {
                swipeRefresh.setRefreshing(false);
                showError(Constants.DataNotify.NO_CONNECTION);
            }
        });
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvHome.setLayoutManager(mLayoutManager);
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mHomeAdapter != null
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() != -1
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() == (mHomeAdapter.getItemCount() - 1)
                        && mHomes.get(mHomeAdapter.getItemCount() - 1) instanceof News
                        && ((News) mHomes.get(mHomeAdapter.getItemCount() - 1)).isLoadmore()
                        && mPageIndex != -1
                        && !isLoading) {
                    mPageIndex++;
                    getDataNews();
                }
            }
        });
        setDataUser();
        showLoading(true);
        getDataSale();
    }

    private void setDataUser() {
        if (mUser != null) {
            tvWellcome.setVisibility(View.GONE);
            tvUserName.setVisibility(View.VISIBLE);
            tvCoin.setVisibility(View.VISIBLE);
            btnEvent.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.gif_event).into(btnEvent);
            String name = mUser.getUserInfo().getName();
            if (name != null && !name.isEmpty()) tvUserName.setText(name);
            else tvUserName.setText(R.string.guest);
            tvUserType.setText(mUser.getUserInfo().getLevelInfo().getName());
            String avatar = mUser.getUserInfo().getAvatar();
            if (avatar != null && !avatar.isEmpty())
                Glide.with(mContext).load(avatar).apply(RequestOptions.errorOf(R.drawable.avatar_default)).into(ivAvatar);
        } else {
            tvWellcome.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.GONE);
            tvCoin.setVisibility(View.GONE);
            ivAvatar.setImageResource(R.drawable.ic_logo);
        }
    }

    private void getDataSale() {
        isLoading = true;
        Call<BaseResponse> getDataSale = mainService.getListNews(mToken, 20, 1, Constants.Value.SALE);
        getDataSale.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.isSuccess() && baseResponse.getData() != null && baseResponse.getData().isJsonArray()) {
                    ArrayList<News> saleNewsList = new ArrayList<>();
                    JsonArray jsonArray = baseResponse.getData().getAsJsonArray();
                    if (jsonArray.size() > 0) {
                        for (int i = 0, z = jsonArray.size(); i < z; i++) {
                            if (jsonArray.get(i).isJsonObject()) {
                                News saleNews = gson.fromJson(jsonArray.get(i).getAsJsonObject().toString(), News.class);
                                if (saleNews != null) saleNewsList.add(saleNews);
                            }
                        }
                    }
                }
                getDataNews();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
                getDataNews();
            }
        });
    }

    private void getDataNews() {
        isLoading = true;

    }

    private void removeLoadmore() {
        // remove loadmore
        if (!mHomes.isEmpty() && mHomes.get(mHomes.size() - 1) instanceof News && ((News) mHomes.get(mHomes.size() - 1)).isLoadmore()) {
            int removePosition = mHomes.size() - 1;
            mHomes.remove(removePosition);
            if (mHomeAdapter != null) mHomeAdapter.notifyItemRemoved(removePosition);
        }
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            rvHome.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            rvHome.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        rvHome.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        if (mPageIndex > 1) {
            removeLoadmore();
            mPageIndex = -1;
        } else {
            appbar.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
            rvHome.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            tvError.setText(error);
            if (error.equals(getString(R.string.not_login_yet)))
                btnLogin.setVisibility(View.VISIBLE);
            else btnLogin.setVisibility(View.GONE);
        }
    }
}
