package com.enjoywater.activiy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Barrier;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.adapter.product.SelectedProductAdapter;
import com.enjoywater.model.Address;
import com.enjoywater.model.Product;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.TvRobotoMedium;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailsActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TvSegoeuiSemiBold tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_order_code)
    ImageView ivOrderCode;
    @BindView(R.id.tv_order_code)
    TvRobotoMedium tvOrderCode;
    @BindView(R.id.btn_cancel_order)
    Button btnCancelOrder;
    @BindView(R.id.view_status_line)
    View viewStatusLine;
    @BindView(R.id.iv_status_ordered)
    ImageView ivStatusOrdered;
    @BindView(R.id.tv_status_ordered)
    TextView tvStatusOrdered;
    @BindView(R.id.view_status_ordered)
    View viewStatusOrdered;
    @BindView(R.id.iv_status_confirmed)
    ImageView ivStatusConfirmed;
    @BindView(R.id.tv_status_confirmed)
    TextView tvStatusConfirmed;
    @BindView(R.id.view_status_confirmed)
    View viewStatusConfirmed;
    @BindView(R.id.iv_status_delivering)
    ImageView ivStatusDelivering;
    @BindView(R.id.tv_status_delivering)
    TextView tvStatusDelivering;
    @BindView(R.id.view_status_delivering)
    View viewStatusDelivering;
    @BindView(R.id.iv_status_received)
    ImageView ivStatusReceived;
    @BindView(R.id.tv_status_received)
    TextView tvStatusReceived;
    @BindView(R.id.barrier_under_order_status)
    Barrier barrierUnderOrderStatus;
    @BindView(R.id.text_order_time)
    TextView textOrderTime;
    @BindView(R.id.iv_order_time)
    ImageView ivOrderTime;
    @BindView(R.id.tv_order_time)
    TvRobotoMedium tvOrderTime;
    @BindView(R.id.text_delivery_time)
    TextView textDeliveryTime;
    @BindView(R.id.tv_delivery_time)
    TvRobotoMedium tvDeliveryTime;
    @BindView(R.id.iv_delivery_time)
    ImageView ivDeliveryTime;
    @BindView(R.id.layout_order_status)
    ConstraintLayout layoutOrderStatus;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    @BindView(R.id.text_address)
    TvRobotoMedium textAddress;
    @BindView(R.id.tv_name_and_phone)
    TvSegoeuiSemiBold tvNameAndPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.text_note)
    TextView textNote;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.group_note)
    Group groupNote;
    @BindView(R.id.layout_address)
    ConstraintLayout layoutAddress;
    @BindView(R.id.iv_selected_products)
    ImageView ivSelectedProducts;
    @BindView(R.id.tv_selected_products)
    TvRobotoMedium tvSelectedProducts;
    @BindView(R.id.rv_selected_products)
    RecyclerView rvSelectedProducts;
    @BindView(R.id.line_under_selected_products)
    View lineUnderSelectedProducts;
    @BindView(R.id.tv_delivery_climb)
    TextView tvDeliveryClimb;
    @BindView(R.id.tv_delivery_climb_fee)
    TvSegoeuiSemiBold tvDeliveryClimbFee;
    @BindView(R.id.group_delivery_climb)
    Group groupDeliveryClimb;
    @BindView(R.id.tv_ship_in_24_hours)
    TextView tvShipIn24Hours;
    @BindView(R.id.tv_ship_in_24_hours_discount)
    TvSegoeuiSemiBold tvShipIn24HoursDiscount;
    @BindView(R.id.group_ship_in_24_hours)
    Group groupShipIn24Hours;
    @BindView(R.id.tv_counpon)
    TextView tvCounpon;
    @BindView(R.id.tv_counpon_discount)
    TvSegoeuiSemiBold tvCounponDiscount;
    @BindView(R.id.group_counpon_discount)
    Group groupCounponDiscount;
    @BindView(R.id.line_under_delivery_options)
    View lineUnderDeliveryOptions;
    @BindView(R.id.text_total_payment)
    TvSegoeuiSemiBold textTotalPayment;
    @BindView(R.id.tv_total_payment)
    TvSegoeuiSemiBold tvTotalPayment;
    @BindView(R.id.text_payment_type)
    TvSegoeuiSemiBold textPaymentType;
    @BindView(R.id.tv_payment_type)
    TvSegoeuiSemiBold tvPaymentType;
    @BindView(R.id.layout_selected_products)
    ConstraintLayout layoutSelectedProducts;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    @BindView(R.id.btn_confirm_received)
    Button btnConfirmReceived;
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
    private MainService mainService;
    private boolean isLoading = false;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private ArrayList<Product> mSelectedProducts = new ArrayList<>();
    private SelectedProductAdapter mSelectedProductAdapter;
    private Address mAddress;
    private int mTotalPayment, mTotalProductsPrice, mTotalProductDiscount, mTotalDeliveryFee, mCouponDiscount;
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");
    private DecimalFormat formatPercent = new DecimalFormat("#.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(this);
        mToken = Utils.getString(this, Constants.Key.TOKEN, "");
        initUI();
    }

    private void initUI() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetOn(OrderDetailsActivity.this)) {
                    if (!isLoading) {

                    } else {
                        swipeRefresh.setRefreshing(false);
                    }
                } else {
                    swipeRefresh.setRefreshing(false);
                    showError(Constants.DataNotify.NO_CONNECTION);
                }
            }
        });
        swipeRefresh.setEnabled(false);
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        rvSelectedProducts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvSelectedProducts.setNestedScrollingEnabled(false);
        //showLoading(true);
        getOrderDetails();
    }

    private void getOrderDetails() {

    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(goneContent ? View.GONE : View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        swipeRefresh.setEnabled(false);
    }

    private void showContent() {
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        swipeRefresh.setEnabled(false);
    }

    private void showError(@NonNull String error) {
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        swipeRefresh.setEnabled(true);
    }
}
