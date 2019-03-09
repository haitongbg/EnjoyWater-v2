package com.enjoywater.activiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.adapter.bonus.BonusAdapter;
import com.enjoywater.adapter.notify.NotifyAdapter;
import com.enjoywater.listener.NotifyListener;
import com.enjoywater.model.Bonus;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Notify;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BonusHistoryActivity extends AppCompatActivity {
    private static final String TAG = "BonusHistoryActivity";
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_coin)
    ImageView ivCoin;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_bonus_history)
    RecyclerView rvBonusHistory;
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
    private MainService mainService;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private ArrayList<Bonus> mBonusList = new ArrayList<>();
    private BonusAdapter mBonusAdapter;
    private Bonus itemLoadmore = new Bonus(true);
    private int mPageIndex = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_history);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(this);
        mToken = Utils.getToken(this);
        initUI();
    }

    private void initUI() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetOn(BonusHistoryActivity.this)) {
                    if (!isLoading) {
                        mPageIndex = 1;
                        mBonusList.clear();
                        if (mBonusAdapter != null) mBonusAdapter.notifyDataSetChanged();
                        getBonusHistory();
                    } else {
                        swipeRefresh.setRefreshing(false);
                    }
                } else {
                    swipeRefresh.setRefreshing(false);
                    showError(Constants.DataNotify.NO_CONNECTION);
                }
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
        btnBack.setOnClickListener(view -> onBackPressed());
        mLayoutManager = new LinearLayoutManager(BonusHistoryActivity.this, RecyclerView.VERTICAL, false);
        rvBonusHistory.setLayoutManager(mLayoutManager);
        rvBonusHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mBonusAdapter != null
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() != -1
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() == (mBonusAdapter.getItemCount() - 1)
                        && mBonusList.get(mBonusAdapter.getItemCount() - 1).isLoadmore()
                        && mPageIndex != -1
                        && !isLoading) {
                    mPageIndex++;
                    getBonusHistory();
                }
            }
        });
        btnLogin.setOnClickListener(v -> {
            if (!isLoading) {
                startActivityForResult(new Intent(BonusHistoryActivity.this, LoginActivity.class), MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN);
                overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
            }
        });
        if (mUser == null || mToken.isEmpty()) {
            showError(Constants.DataNotify.NOT_LOGIN_YET);
        } else {
            tvCoin.setText(formatVND.format(mUser.getUserInfo().getCoin()));
            showLoading(true);
            getBonusHistory();
        }
    }

    private void getBonusHistory() {
        isLoading = true;
        Call<BaseResponse> getBonusHistory = mainService.getBonusHistory(mToken, 20, mPageIndex);
        getBonusHistory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess() && baseResponse.getData() != null && baseResponse.getData().isJsonArray()) {
                        ArrayList<Bonus> orders = new ArrayList<>();
                        JsonArray jsonArray = baseResponse.getData().getAsJsonArray();
                        if (jsonArray.size() > 0) {
                            for (int i = 0, z = jsonArray.size(); i < z; i++) {
                                if (jsonArray.get(i).isJsonObject()) {
                                    Bonus bonus = gson.fromJson(jsonArray.get(i).getAsJsonObject().toString(), Bonus.class);
                                    if (bonus != null) orders.add(bonus);
                                }
                            }
                        }
                        setData(orders);
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
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                isLoading = false;
                t.printStackTrace();
                showError(Constants.DataNotify.DATA_ERROR);
            }
        });

    }

    private void setData(ArrayList<Bonus> bonusList) {
        removeLoadmore();
        // add list
        if (!bonusList.isEmpty()) {
            showContent();
            int insertPosition = mBonusList.size();
            mBonusList.addAll(bonusList);
            if (mBonusAdapter == null) {
                mBonusAdapter = new BonusAdapter(BonusHistoryActivity.this, mBonusList);
                rvBonusHistory.setAdapter(mBonusAdapter);
            } else {
                mBonusAdapter.notifyItemRangeInserted(insertPosition, bonusList.size());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() < mBonusAdapter.getItemCount() - 1) {
                        mBonusList.add(itemLoadmore);
                        mBonusAdapter.notifyItemInserted(mBonusList.size() - 1);
                    }
                }
            }, 500);
        } else if (mBonusList.isEmpty()) {
            showError("Hiện chưa có thông báo mới.");
        }
    }

    private void removeLoadmore() {
        // remove loadmore
        if (!mBonusList.isEmpty() && mBonusList.get(mBonusList.size() - 1).isLoadmore()) {
            int removePosition = mBonusList.size() - 1;
            mBonusList.remove(removePosition);
            if (mBonusAdapter != null) mBonusAdapter.notifyItemRemoved(removePosition);
        }
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            rvBonusHistory.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            rvBonusHistory.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        rvBonusHistory.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        if (mPageIndex > 1) {
            removeLoadmore();
            mPageIndex = -1;
        } else {
            appbar.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
            rvBonusHistory.setVisibility(View.GONE);
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
            mToken = Utils.getToken(BonusHistoryActivity.this);
            mPageIndex = 1;
            mBonusList.clear();
            if (mBonusAdapter != null) mBonusAdapter.notifyDataSetChanged();
            showLoading(true);
            getBonusHistory();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.PROFILE_UPDATED: {
                mUser = (User) event.getObject();
                tvCoin.setText(formatVND.format(mUser.getUserInfo().getCoin()));
                break;
            }
            default:{
                //Log.e(TAG, "onMessageEvent " + gson.toJson(event));
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
