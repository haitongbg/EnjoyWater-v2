package com.enjoywater.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.BonusDetailsActivity;
import com.enjoywater.activiy.LoginActivity;
import com.enjoywater.activiy.MainActivity;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.activiy.NewsDetailsActivity;
import com.enjoywater.activiy.OrderDetailsActivity;
import com.enjoywater.adapter.notify.NotifyAdapter;
import com.enjoywater.listener.NotifyListener;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Notify;
import com.enjoywater.model.Order;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyFragment extends Fragment {
    private static final String TAG = "NotifyFragment";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_notif)
    RecyclerView rvNotif;
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
    @BindView(R.id.btn_login)
    Button btnLogin;
    private Context mContext;
    private MainService mainService;
    private String mToken;
    private Gson gson = new Gson();
    private ArrayList<Notify> mNotifies = new ArrayList<>();
    private NotifyAdapter mNotifyAdapter;
    private Notify itemLoadmore = new Notify(true);
    private int mPageIndex = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;

    public static NotifyFragment newInstance() {
        NotifyFragment notifyFragment = new NotifyFragment();
        return notifyFragment;
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
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
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
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetOn(mContext)) {
                    if (!isLoading) {
                        mPageIndex = 1;
                        mNotifies.clear();
                        if (mNotifyAdapter != null) mNotifyAdapter.notifyDataSetChanged();
                        getNotificationHistory();
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
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvNotif.setLayoutManager(mLayoutManager);
        rvNotif.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mNotifyAdapter != null
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() != -1
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() == (mNotifyAdapter.getItemCount() - 1)
                        && mNotifies.get(mNotifyAdapter.getItemCount() - 1).isLoadmore()
                        && mPageIndex != -1
                        && !isLoading) {
                    mPageIndex++;
                    getNotificationHistory();
                }
            }
        });
        btnLogin.setOnClickListener(v -> {
            if (!isLoading) {
                getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN);
                (getActivity()).overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
            }
        });
        if (mToken.isEmpty()) {
            showError(Constants.DataNotify.NOT_LOGIN_YET);
        } else {
            showLoading(true);
            getNotificationHistory();
        }
    }

    private void getNotificationHistory() {
        isLoading = true;
        Call<BaseResponse> getListNotify = mainService.getListNotify(mToken, 20, mPageIndex);
        getListNotify.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                isLoading = false;
                BaseResponse getListNotifyResponse = response.body();
                if (getListNotifyResponse != null) {
                    if (getListNotifyResponse.isSuccess() && getListNotifyResponse.getData() != null && getListNotifyResponse.getData().isJsonArray()) {
                        ArrayList<Notify> orders = new ArrayList<>();
                        JsonArray arrayNotifications = getListNotifyResponse.getData().getAsJsonArray();
                        if (arrayNotifications.size() > 0) {
                            for (int i = 0, z = arrayNotifications.size(); i < z; i++) {
                                if (arrayNotifications.get(i).isJsonObject()) {
                                    Notify notify = gson.fromJson(arrayNotifications.get(i).getAsJsonObject().toString(), Notify.class);
                                    if (notify != null) orders.add(notify);
                                }
                            }
                        }
                        setData(orders);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (getListNotifyResponse.getError() != null && getListNotifyResponse.getError().getMessage() != null && !getListNotifyResponse.getError().getMessage().isEmpty())
                            message = getListNotifyResponse.getError().getMessage();
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

    private void setData(ArrayList<Notify> notifies) {
        removeLoadmore();
        // add list
        if (!notifies.isEmpty()) {
            showContent();
            int insertPosition = mNotifies.size();
            mNotifies.addAll(notifies);
            if (mNotifyAdapter == null) {
                mNotifyAdapter = new NotifyAdapter(mContext, mNotifies, mNotifyListener);
                rvNotif.setAdapter(mNotifyAdapter);
            } else {
                mNotifyAdapter.notifyItemRangeInserted(insertPosition, notifies.size());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() < mNotifyAdapter.getItemCount() - 1) {
                        mNotifies.add(itemLoadmore);
                        mNotifyAdapter.notifyItemInserted(mNotifies.size() - 1);
                    }
                }
            }, 500);
        } else if (mNotifies.isEmpty()) {
            showError("Hiện chưa có thông báo mới.");
        }
    }

    private void removeLoadmore() {
        // remove loadmore
        if (!mNotifies.isEmpty() && mNotifies.get(mNotifies.size() - 1).isLoadmore()) {
            int removePosition = mNotifies.size() - 1;
            mNotifies.remove(removePosition);
            if (mNotifyAdapter != null) mNotifyAdapter.notifyItemRemoved(removePosition);
        }
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            rvNotif.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            rvNotif.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        rvNotif.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        if (mPageIndex > 1) {
            removeLoadmore();
            mPageIndex = -1;
        } else {
            appbar.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
            rvNotif.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            tvError.setText(error);
            if (error.equals(Constants.DataNotify.NOT_LOGIN_YET))
                btnLogin.setVisibility(View.VISIBLE);
            else btnLogin.setVisibility(View.GONE);
        }
    }

    private NotifyListener mNotifyListener = new NotifyListener() {

        @Override
        public void goNotifyDetails(Notify notify) {
            if (notify != null && notify.getType() != null) {
                switch (notify.getType()) {
                    case Constants.Value.ORDER: {
                        Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                        intent.putExtra(Constants.Key.ORDER_ID, notify.getContent());
                        startActivity(intent);
                        (getActivity()).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
                        break;
                    }
                    case Constants.Value.BONUS: {
                        Intent intent = new Intent(mContext, BonusDetailsActivity.class);
                        startActivity(intent);
                        (getActivity()).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
                        break;
                    }
                    case Constants.Value.NEWS: {
                        Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                        intent.putExtra(Constants.Key.NEWS_ID, notify.getContent());
                        startActivity(intent);
                        (getActivity()).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
                        break;
                    }
                    default:
                        break;
                }
                notify.setStatus(Constants.Value.READ);
                updateNotifyStatus(notify);
            }

        }
    };

    private void updateNotifyStatus(Notify notify) {
        isLoading = true;
        Call<BaseResponse> cancelNotification = mainService.updateNotifyStatus(mToken, notify.getId(), notify.getStatus());
        cancelNotification.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                showContent();
                BaseResponse cancelNotificationResponse = response.body();
                if (cancelNotificationResponse != null) {
                    if (cancelNotificationResponse.isSuccess()) {
                        updateNotification(notify);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (cancelNotificationResponse.getError() != null && cancelNotificationResponse.getError().getMessage() != null && !cancelNotificationResponse.getError().getMessage().isEmpty())
                            message = cancelNotificationResponse.getError().getMessage();
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(mContext, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                showContent();
            }
        });
    }

    private void updateNotification(Notify notify) {
        showContent();
        boolean existed = false;
        for (int i = 0, z = mNotifies.size(); i < z; i++) {
            if (mNotifies.get(i).getId() == notify.getId()) {
                existed = true;
                mNotifies.set(i, notify);
                mNotifyAdapter.notifyItemChanged(i);
                break;
            }
        }
        if (!existed) {
            mNotifies.add(0, notify);
            if (mNotifyAdapter != null) {
                mNotifyAdapter.notifyItemInserted(0);
                mNotifyAdapter.notifyItemChanged(1);
                rvNotif.scrollToPosition(0);
            }
            else {
                mNotifyAdapter = new NotifyAdapter(mContext, mNotifies, mNotifyListener);
                rvNotif.setAdapter(mNotifyAdapter);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN && resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS) {
            mToken = Utils.getToken(mContext);
            mPageIndex = 1;
            mNotifies.clear();
            if (mNotifyAdapter != null) mNotifyAdapter.notifyDataSetChanged();
            showLoading(true);
            getNotificationHistory();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.BONUS_UPDATED: {
                Notify notify = (Notify) event.getObject();
                updateNotification(notify);
                break;
            }
            case Constants.Key.ORDER_UPDATED: {
                Notify notify = (Notify) event.getObject();
                updateNotification(notify);
                break;
            }
            case Constants.Key.NEWS_UPDATED: {
                break;
            }
            default:{
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
}
