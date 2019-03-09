package com.enjoywater.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
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
import com.bumptech.glide.request.RequestOptions;
import com.enjoywater.R;
import com.enjoywater.activiy.EventActivity;
import com.enjoywater.activiy.LoginActivity;
import com.enjoywater.activiy.MainActivity;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.activiy.NewsDetailsActivity;
import com.enjoywater.activiy.PersonalActivity;
import com.enjoywater.adapter.home.HomeAdapter;
import com.enjoywater.listener.HomeListener;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.News;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
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
    TextView tvWellcome;
    @BindView(R.id.btn_event)
    ImageView btnEvent;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.v_separate_dot)
    View vSeparateDot;
    @BindView(R.id.iv_coin)
    ImageView ivCoin;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
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
    @BindView(R.id.group_user_info)
    Group groupUserInfo;
    @BindView(R.id.ripple_event)
    RippleView rippleEvent;

    private Context mContext;
    private MainService mainService;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private int mPageIndex = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;
    private ArrayList<News> mHomes = new ArrayList<>();
    private ArrayList<News> mSaleNewsList = new ArrayList<>();
    private HomeAdapter mHomeAdapter;
    private News itemLoadmore = new News(true, false);
    private News itemSaleNewsList = new News(false, true);
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");

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
        appbar.setOnClickListener(v -> {
            startActivity(new Intent(mContext, PersonalActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        });
        btnEvent.setOnClickListener(view -> {
            startActivity(new Intent(mContext, EventActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
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
                        && mHomes.get(mHomeAdapter.getItemCount() - 1).isLoadmore()
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
            groupUserInfo.setVisibility(View.VISIBLE);
            String avatar = mUser.getUserInfo().getAvatar();
            if (avatar != null && !avatar.isEmpty())
                Glide.with(mContext).load(avatar).apply(RequestOptions.errorOf(R.drawable.avatar_default)).into(ivAvatar);
            else ivAvatar.setImageResource(R.drawable.avatar_default);
            String name = mUser.getUserInfo().getName();
            if (name != null && !name.isEmpty()) tvUserName.setText(name);
            else tvUserName.setText(R.string.guest);
            tvUserType.setText(mUser.getUserInfo().getLevelInfo().getName());
            tvCoin.setText(formatVND.format(mUser.getUserInfo().getCoin()));
            Glide.with(mContext).load(R.drawable.gif_event).into(btnEvent);
        } else {
            tvWellcome.setVisibility(View.VISIBLE);
            groupUserInfo.setVisibility(View.GONE);
            ivAvatar.setImageResource(R.drawable.avatar_default);
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
                    mSaleNewsList.clear();
                    JsonArray jsonArray = baseResponse.getData().getAsJsonArray();
                    if (jsonArray.size() > 0) {
                        for (int i = 0, z = jsonArray.size(); i < z; i++) {
                            if (jsonArray.get(i).isJsonObject()) {
                                News saleNews = gson.fromJson(jsonArray.get(i).getAsJsonObject().toString(), News.class);
                                if (saleNews != null) mSaleNewsList.add(saleNews);
                            }
                        }
                    }
                    if (!mSaleNewsList.isEmpty()) mHomes.add(0, itemSaleNewsList);
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
        Call<BaseResponse> getDataNews = mainService.getListNews(mToken, 20, mPageIndex, Constants.Value.HOME);
        getDataNews.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess() && baseResponse.getData() != null && baseResponse.getData().isJsonArray()) {
                        ArrayList<News> homeNewsList = new ArrayList<>();
                        JsonArray jsonArray = baseResponse.getData().getAsJsonArray();
                        if (jsonArray.size() > 0) {
                            for (int i = 0, z = jsonArray.size(); i < z; i++) {
                                if (jsonArray.get(i).isJsonObject()) {
                                    News homeNews = gson.fromJson(jsonArray.get(i).getAsJsonObject().toString(), News.class);
                                    if (homeNews != null) homeNewsList.add(homeNews);
                                }
                            }
                        }
                        setDataHomeNews(homeNewsList);
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
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                isLoading = false;
                showError(Constants.DataNotify.DATA_ERROR);
            }
        });
    }

    private void setDataHomeNews(ArrayList<News> homeNewsList) {
        removeLoadmore();
        // add list
        if (!homeNewsList.isEmpty()) {
            showContent();
            int insertPosition = mHomes.size();
            mHomes.addAll(homeNewsList);
            if (mHomeAdapter == null) {
                mHomeAdapter = new HomeAdapter(mContext, mHomes, mSaleNewsList, mHomeListener);
                rvHome.setAdapter(mHomeAdapter);
            } else {
                mHomeAdapter.notifyItemRangeInserted(insertPosition, homeNewsList.size());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() < mHomeAdapter.getItemCount() - 1) {
                        mHomes.add(itemLoadmore);
                        mHomeAdapter.notifyItemInserted(mHomes.size() - 1);
                    }
                }
            }, 500);
        } else if (mHomes.isEmpty()) {
            showError("Hiện chưa có bài viết mới.");
        }
    }

    private HomeListener mHomeListener = new HomeListener() {
        @Override
        public void goHomeNewsDetail(News news) {
            Intent intent = new Intent(mContext, NewsDetailsActivity.class);
            intent.putExtra(Constants.Key.NEWS, news);
            startActivity(intent);
            (getActivity()).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        }

        @Override
        public void goSaleNewsDetail(News news) {
            Intent intent = new Intent(mContext, NewsDetailsActivity.class);
            intent.putExtra(Constants.Key.NEWS, news);
            startActivity(intent);
            (getActivity()).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        }
    };

    private void removeLoadmore() {
        // remove loadmore
        if (!mHomes.isEmpty() && mHomes.get(mHomes.size() - 1).isLoadmore()) {
            int removePosition = mHomes.size() - 1;
            mHomes.remove(removePosition);
            if (mHomeAdapter != null) {
                mHomeAdapter.notifyItemRemoved(removePosition);
            }
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
            if (error.equals(Constants.DataNotify.NOT_LOGIN_YET))
                btnLogin.setVisibility(View.VISIBLE);
            else btnLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN && resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS) {
            mToken = Utils.getToken(mContext);
            mUser = Utils.getUser(mContext);
            setDataUser();
            mPageIndex = 1;
            mHomes.clear();
            if (mHomeAdapter != null) mHomeAdapter.notifyDataSetChanged();
            showLoading(true);
            getDataSale();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.PROFILE_UPDATED: {
                mUser = (User) event.getObject();
                setDataUser();
                break;
            }
            case Constants.Key.INSERT_NEWS: {
                News news = (News) event.getObject();
                insertNews(news);
                break;
            }
            default: {
                //Log.e(TAG, "onMessageEvent " + gson.toJson(event));
                break;
            }
        }
    }

    private void insertNews(News news) {
        switch (news.getType()) {
            case Constants.Value.SALE: {
                boolean existed = false;
                for (int i = 0, z = mSaleNewsList.size(); i < z; i++) {
                    if (mSaleNewsList.get(i).getId() == news.getId()) {
                        existed = true;
                        mSaleNewsList.set(i, news);
                        if (mHomeAdapter != null) mHomeAdapter.notifyItemSaleChanged(i);
                        break;
                    }
                }
                if (!existed) {
                    mSaleNewsList.add(0, news);
                    if (mHomeAdapter != null) mHomeAdapter.notifyItemSaleInserted();
                }
                break;
            }
            case Constants.Value.HOME: {
                boolean existed = false;
                for (int i = 0, z = mHomes.size(); i < z; i++) {
                    if (mHomes.get(i).getId() == news.getId()) {
                        existed = true;
                        mHomes.set(i, news);
                        mHomeAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                if (!existed) {
                    int insertPosition;
                    if (mHomes.get(0).isSaleNewsList()) insertPosition = 1;
                    else insertPosition = 0;
                    mHomes.add(insertPosition, news);
                    if (mHomeAdapter != null) {
                        mHomeAdapter.notifyItemInserted(insertPosition);
                        mHomeAdapter.notifyItemChanged(insertPosition + 1);
                        rvHome.scrollToPosition(insertPosition);
                    } else {
                        mHomeAdapter = new HomeAdapter(mContext, mHomes, mSaleNewsList, mHomeListener);
                        rvHome.setAdapter(mHomeAdapter);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }
}
