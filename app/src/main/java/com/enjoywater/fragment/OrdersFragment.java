package com.enjoywater.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.adapter.order.HistoryOrdersAdapter;
import com.enjoywater.listener.OrderListener;
import com.enjoywater.model.Order;
import com.enjoywater.model.Product;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {
    @BindView(R.id.tv_title)
    TvSegoeuiSemiBold tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_orders)
    RecyclerView rvOrders;
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
    private Context mContext;
    private MainService mainService;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private ArrayList<Order> mOrders = new ArrayList<>();
    private HistoryOrdersAdapter mOrdersAdapter;
    private Order itemLoadmore = new Order(true);
    private int mPageIndex = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isViewRefreshed = false;
    private boolean isViewVisible = false;
    private boolean isLoading = false;

    public static OrdersFragment newInstance() {
        OrdersFragment homeFragment = new OrdersFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(mContext);
        mToken = Utils.getString(mContext, Constants.Key.TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
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
                        mOrders.clear();
                        if (mOrdersAdapter != null) mOrdersAdapter.notifyDataSetChanged();
                        getOrderHistory();
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
        rvOrders.setLayoutManager(mLayoutManager);
        rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mOrdersAdapter != null
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() != -1
                        && mLayoutManager.findLastCompletelyVisibleItemPosition() == (mOrdersAdapter.getItemCount() - 1)
                        && mOrders.get(mOrdersAdapter.getItemCount() - 1).isLoadmore()
                        && mPageIndex != -1
                        && !isLoading) {
                    mPageIndex++;
                    getOrderHistory();
                }
            }
        });
        showLoading(true);
        getOrderHistory();
    }

    private void getOrderHistory() {
        isLoading = true;
        Call<BaseResponse> getOrderHistory = mainService.getOrderHistory(mToken, 20, mPageIndex);
        getOrderHistory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                isLoading = false;
                BaseResponse getOrderHistoryResponse = response.body();
                if (getOrderHistoryResponse != null) {
                    if (getOrderHistoryResponse.isSuccess() && getOrderHistoryResponse.getData() != null) {
                        if (getOrderHistoryResponse.getData().isJsonArray()) {
                            ArrayList<Order> orders = new ArrayList<>();
                            JsonArray arrayOrders = getOrderHistoryResponse.getData().getAsJsonArray();
                            if (arrayOrders.size() > 0) {
                                for (int i = 0, z = arrayOrders.size(); i < z; i++) {
                                    if (arrayOrders.get(i).isJsonObject()) {
                                        Order order = gson.fromJson(arrayOrders.get(i).getAsJsonObject().toString(), Order.class);
                                        if (order != null) orders.add(order);
                                    }
                                }
                            }
                            setDataHistory(orders);
                        } else showError(Constants.DataNotify.DATA_ERROR);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (getOrderHistoryResponse.getError() != null && getOrderHistoryResponse.getError().getMessage() != null && !getOrderHistoryResponse.getError().getMessage().isEmpty())
                            message = getOrderHistoryResponse.getError().getMessage();
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

    private void setDataHistory(ArrayList<Order> orders) {
        removeLoadmore();
        // add list
        if (!orders.isEmpty()) {
            showContent();
            int insertPosition = mOrders.size();
            mOrders.addAll(orders);
            if (mOrdersAdapter == null) {
                mOrdersAdapter = new HistoryOrdersAdapter(mContext, mOrders, mOrderListener);
                rvOrders.setAdapter(mOrdersAdapter);
            } else {
                mOrdersAdapter.notifyItemRangeInserted(insertPosition, orders.size());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() < mOrdersAdapter.getItemCount() - 1) {
                        mOrders.add(itemLoadmore);
                        mOrdersAdapter.notifyItemInserted(mOrders.size() - 1);
                    }
                }
            }, 1000);
        } else if (mOrders.isEmpty()) {
            showError("Bạn chưa tạo đơn hàng nào.");
        }
    }

    private void removeLoadmore() {
        // remove loadmore
        if (!mOrders.isEmpty() && mOrders.get(mOrders.size() - 1).isLoadmore()) {
            int removePosition = mOrders.size() - 1;
            mOrders.remove(removePosition);
            if (mOrdersAdapter != null) mOrdersAdapter.notifyItemRemoved(removePosition);
        }
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            rvOrders.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        rvOrders.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        if (mPageIndex > 1) {
            removeLoadmore();
            mPageIndex = -1;
        } else {
            appbar.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
            rvOrders.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            tvError.setText(error);
        }
    }

    private OrderListener mOrderListener = new OrderListener() {
        @Override
        public void cancelOrder(Order order) {

        }

        @Override
        public void goOrderDetails(Order order) {

        }
    };
}
