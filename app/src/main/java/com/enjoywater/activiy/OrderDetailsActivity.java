package com.enjoywater.activiy;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.Barrier;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.enjoywater.adapter.product.SelectedProductAdapter;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Order;
import com.enjoywater.model.Product;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.dialog.DialogOrderRating;
import com.enjoywater.view.dialog.DialogSubmitRefCode;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_order_code)
    ImageView ivOrderCode;
    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
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
    TextView tvCreatedTime;
    @BindView(R.id.text_delivery_time)
    TextView textDeliveryTime;
    @BindView(R.id.tv_delivery_time)
    TextView tvExpectedDeliveryTime;
    @BindView(R.id.iv_delivery_time)
    ImageView ivDeliveryTime;
    @BindView(R.id.group_order_status)
    Group groupOrderStatus;
    @BindView(R.id.layout_order_status)
    ConstraintLayout layoutOrderStatus;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.tv_name_and_phone)
    TextView tvNameAndPhone;
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
    TextView tvSelectedProducts;
    @BindView(R.id.rv_selected_products)
    RecyclerView rvSelectedProducts;
    @BindView(R.id.line_under_selected_products)
    View lineUnderSelectedProducts;
    @BindView(R.id.tv_delivery_climb)
    TextView tvDeliveryClimb;
    @BindView(R.id.tv_delivery_climb_fee)
    TextView tvDeliveryClimbFee;
    @BindView(R.id.group_delivery_climb)
    Group groupDeliveryClimb;
    @BindView(R.id.tv_ship_type)
    TextView tvShipType;
    @BindView(R.id.tv_ship_type_discount)
    TextView tvShipTypeDiscount;
    @BindView(R.id.tv_counpon)
    TextView tvCounpon;
    @BindView(R.id.tv_counpon_discount)
    TextView tvCounponDiscount;
    @BindView(R.id.group_counpon_discount)
    Group groupCounponDiscount;
    @BindView(R.id.line_under_delivery_options)
    View lineUnderDeliveryOptions;
    @BindView(R.id.text_total_payment)
    TextView textTotalPayment;
    @BindView(R.id.tv_total_payment)
    TextView tvTotalPayment;
    @BindView(R.id.text_payment_type)
    TextView textPaymentType;
    @BindView(R.id.tv_payment_type)
    TextView tvPaymentType;
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
    @BindView(R.id.text_total_price)
    TextView textTotalPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    private MainService mainService;
    private boolean isLoading = false;
    private String mToken;
    private Gson gson = new Gson();
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");
    private DecimalFormat formatPercent = new DecimalFormat("#.0");
    private Calendar calendar = Calendar.getInstance();
    private Order mOrder;
    private int mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        mToken = Utils.getToken(this);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.Key.ORDER)) mOrder = intent.getParcelableExtra(Constants.Key.ORDER);
            if (intent.hasExtra(Constants.Key.ORDER_ID)) mOrderId = intent.getIntExtra(Constants.Key.ORDER_ID, 0);
        }
        initUI();
    }

    private void initUI() {
        btnBack.setOnClickListener(v -> onBackPressed());
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetOn(OrderDetailsActivity.this)) {
                    if (!isLoading) {
                        showLoading(true);
                        getOrderDetails();
                    } else {
                        swipeRefresh.setRefreshing(false);
                    }
                } else {
                    swipeRefresh.setRefreshing(false);
                    showError(Constants.DataNotify.NO_CONNECTION);
                }
            }
        });
        //swipeRefresh.setEnabled(false);
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
        btnCancelOrder.setOnClickListener(v -> {
            if (Utils.isInternetOn(OrderDetailsActivity.this)) {
                if (!isLoading) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                    builder.setMessage("Bạn muốn hủy đơn hàng này?")
                            .setCancelable(false)
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    showLoading(false);
                                    cancelOrder();
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else {
                Toast.makeText(OrderDetailsActivity.this, Constants.DataNotify.NO_CONNECTION, Toast.LENGTH_SHORT).show();
            }
        });
        btnConfirmReceived.setOnClickListener(v -> {
            if (Utils.isInternetOn(OrderDetailsActivity.this)) {
                if (!isLoading) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                    builder.setMessage("Bạn xác nhận đã nhận được hàng?")
                            .setCancelable(false)
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    showLoading(false);
                                    confirmReceived();
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else {
                Toast.makeText(OrderDetailsActivity.this, Constants.DataNotify.NO_CONNECTION, Toast.LENGTH_SHORT).show();
            }
        });
        if (mOrder != null) {
            setDataOrder();
        } else if (mOrderId != 0) {
            showLoading(true);
            getOrderDetails();
        }
    }

    private void getOrderDetails() {
        isLoading = true;
        Call<BaseResponse> getOrderDetails = mainService.getOrderDetails(mToken, mOrderId);
        getOrderDetails.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                BaseResponse getOrderDetailsResponse = response.body();
                if (getOrderDetailsResponse != null) {
                    if (getOrderDetailsResponse.isSuccess() && getOrderDetailsResponse.getData() != null && getOrderDetailsResponse.getData().isJsonObject()) {
                        mOrder = gson.fromJson(getOrderDetailsResponse.getData(), Order.class);
                        setDataOrder();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (getOrderDetailsResponse.getError() != null && getOrderDetailsResponse.getError().getMessage() != null && !getOrderDetailsResponse.getError().getMessage().isEmpty())
                            message = getOrderDetailsResponse.getError().getMessage();
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

    private void setDataOrder() {
        showContent();
        mOrderId = mOrder.getId();
        // code
        tvOrderCode.setText("Mã đơn hàng: #" + mOrder.getId());
        // status
        String status = mOrder.getStatus();
        if (status.equals(Constants.Value.CANCELED)) {
            btnCancelOrder.setVisibility(View.GONE);
            groupOrderStatus.setVisibility(View.GONE);
            tvOrderStatus.setVisibility(View.VISIBLE);
            tvOrderStatus.setText("- Đã hủy");
            tvOrderStatus.setTextColor(getResources().getColor(R.color.black_3));
        } else {
            tvOrderStatus.setVisibility(View.GONE);
            groupOrderStatus.setVisibility(View.VISIBLE);
            btnCancelOrder.setVisibility(View.GONE);
            viewStatusOrdered.setBackgroundResource(R.color.black_c);
            viewStatusConfirmed.setBackgroundResource(R.color.black_c);
            viewStatusDelivering.setBackgroundResource(R.color.black_c);
            viewStatusLine.setBackgroundResource(R.color.black_c);
            ivStatusOrdered.setImageResource(R.drawable.ic_unsuccess);
            ivStatusConfirmed.setImageResource(R.drawable.ic_unsuccess);
            ivStatusDelivering.setImageResource(R.drawable.ic_unsuccess);
            ivStatusReceived.setImageResource(R.drawable.ic_unsuccess);
            tvStatusOrdered.setTextColor(getResources().getColor(R.color.black_c));
            tvStatusConfirmed.setTextColor(getResources().getColor(R.color.black_c));
            tvStatusDelivering.setTextColor(getResources().getColor(R.color.black_c));
            tvStatusReceived.setTextColor(getResources().getColor(R.color.black_c));
            btnConfirmReceived.setVisibility(View.GONE);
            textDeliveryTime.setText(R.string.expected_delivery_time);
            ((ConstraintLayout.LayoutParams) layoutSelectedProducts.getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(R.dimen.size_15);
            switch (status) {
                case Constants.Value.PENDING: {
                    btnCancelOrder.setVisibility(View.VISIBLE);
                    viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                    ivStatusOrdered.setImageResource(R.drawable.ic_success);
                    tvStatusOrdered.setTextColor(getResources().getColor(R.color.colorAccent));
                    break;
                }
                case Constants.Value.VERIFIED: {
                    btnCancelOrder.setVisibility(View.VISIBLE);
                    viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                    viewStatusConfirmed.setBackgroundResource(R.color.colorAccent);
                    ivStatusOrdered.setImageResource(R.drawable.ic_success);
                    ivStatusConfirmed.setImageResource(R.drawable.ic_success);
                    tvStatusOrdered.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvStatusConfirmed.setTextColor(getResources().getColor(R.color.colorAccent));
                    break;
                }
                case Constants.Value.DELIVERING: {
                    btnCancelOrder.setVisibility(View.GONE);
                    viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                    viewStatusConfirmed.setBackgroundResource(R.color.colorAccent);
                    viewStatusDelivering.setBackgroundResource(R.color.colorAccent);
                    ivStatusOrdered.setImageResource(R.drawable.ic_success);
                    ivStatusConfirmed.setImageResource(R.drawable.ic_success);
                    ivStatusDelivering.setImageResource(R.drawable.ic_success);
                    tvStatusOrdered.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvStatusConfirmed.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvStatusDelivering.setTextColor(getResources().getColor(R.color.colorAccent));
                    if (mOrder.isReceived()) {
                        mHandler.removeCallbacks(null);
                        if (mOrder.getRate() == 0) mHandler.postDelayed(openRatingRunnable, 1500);
                    } else {
                        btnConfirmReceived.setVisibility(View.VISIBLE);
                        ((ConstraintLayout.LayoutParams) layoutSelectedProducts.getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(R.dimen.size_60);
                    }
                    break;
                }
                case Constants.Value.DELIVERED: {
                    tvOrderStatus.setVisibility(View.VISIBLE);
                    tvOrderStatus.setText("- Đã giao hàng");
                    tvOrderStatus.setTextColor(getResources().getColor(R.color.colorAccentDark));
                    btnCancelOrder.setVisibility(View.GONE);
                    viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                    viewStatusConfirmed.setBackgroundResource(R.color.colorAccent);
                    viewStatusDelivering.setBackgroundResource(R.color.colorAccent);
                    viewStatusLine.setBackgroundResource(R.color.colorAccent);
                    ivStatusOrdered.setImageResource(R.drawable.ic_success);
                    ivStatusConfirmed.setImageResource(R.drawable.ic_success);
                    ivStatusDelivering.setImageResource(R.drawable.ic_success);
                    ivStatusReceived.setImageResource(R.drawable.ic_success);
                    tvStatusOrdered.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvStatusConfirmed.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvStatusDelivering.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvStatusReceived.setTextColor(getResources().getColor(R.color.colorAccent));
                    textDeliveryTime.setText(R.string.delivered_time);
                    break;
                }
                default:
                    break;
            }
        }
        // time
        String createdTime = mOrder.getCreatedAt();
        if (createdTime != null && !createdTime.isEmpty())
            tvCreatedTime.setText(Utils.convertDateTimeToDateTime(createdTime, "GMT", 5, 8));
        String expectedDeliveryTime = mOrder.getExpectedDelivery();
        if (expectedDeliveryTime != null && !expectedDeliveryTime.isEmpty())
            tvExpectedDeliveryTime.setText(Utils.convertDateTimeToDateTime(expectedDeliveryTime, "GMT", 5, 5));
        // address
        String name = mOrder.getReceiverName();
        if (name == null || name.isEmpty()) name = "Unknown name";
        String phone = mOrder.getPhone();
        if (phone == null || phone.isEmpty()) phone = "Unknown phone";
        tvNameAndPhone.setText(name + " - " + phone);
        String fullAddress = Utils.getFullAddress(mOrder.getProvince(), mOrder.getDistrict(), "", mOrder.getAddress());
        if (!fullAddress.isEmpty()) {
            tvAddress.setText(fullAddress);
            tvAddress.setVisibility(View.VISIBLE);
        } else tvAddress.setVisibility(View.GONE);
        String note = mOrder.getNotes();
        if (note != null && !note.isEmpty()) {
            groupNote.setVisibility(View.VISIBLE);
            tvNote.setText(note);
        } else groupNote.setVisibility(View.GONE);
        // products
        ArrayList<Product> mSelectedProducts = mOrder.getItems();
        if (mSelectedProducts != null && !mSelectedProducts.isEmpty()) {
            SelectedProductAdapter mSelectedProductAdapter = new SelectedProductAdapter(this, mSelectedProducts, null);
            rvSelectedProducts.setAdapter(mSelectedProductAdapter);
            int totalProductsPrice = 0;
            for (Product product : mSelectedProducts) {
                totalProductsPrice += product.getVolume() * product.getAsk();
            }
            //price
            tvTotalPrice.setText(formatVND.format(totalProductsPrice) + " đ");
            // climb
            if (mOrder.isDeliveryClimb()) {
                groupDeliveryClimb.setVisibility(View.VISIBLE);
                tvDeliveryClimbFee.setText(formatVND.format(mOrder.getDeliveryFee()) + " đ");
            } else groupDeliveryClimb.setVisibility(View.GONE);
            // ship type
            switch (mOrder.getDeliveryOpts()) {
                case "2h": {
                    tvShipType.setText("Giao hàng trong 2 giờ");
                    tvShipTypeDiscount.setVisibility(View.GONE);
                    break;
                }
                case "24h": {
                    String shipType;
                    if (mOrder.getOrderBySchedule() > 0) {
                        calendar.setTimeInMillis(mOrder.getOrderBySchedule() * 1000);
                        shipType = "Giao ngày " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    } else {
                        shipType = "Giao hàng trong 24 giờ";
                    }
                    int totalProductDiscount = mOrder.getDiscount();
                    if (totalProductDiscount > 0 && totalProductsPrice > 0) {
                        float discountPercent = (float) (totalProductDiscount * 100) / totalProductsPrice;
                        if (discountPercent >= 1.0f)
                            shipType = shipType + " (giảm " + formatPercent.format(discountPercent) + "%)";
                        else
                            shipType = shipType + " (giảm 0" + formatPercent.format(discountPercent) + "%)";
                    } else shipType = shipType + " (giảm 0%)";
                    tvShipType.setText(shipType);
                    tvShipTypeDiscount.setText("-" + formatVND.format(totalProductDiscount) + " đ");
                    tvShipTypeDiscount.setVisibility(View.VISIBLE);
                    break;
                }
                default:
                    break;
            }
            // coupon
            if (mOrder.getCouponDiscount() > 0) {
                groupCounponDiscount.setVisibility(View.VISIBLE);
                tvCounponDiscount.setText("-" + formatVND.format(mOrder.getCouponDiscount()) + " đ");
            } else groupCounponDiscount.setVisibility(View.GONE);
            // total payment
            tvTotalPayment.setText(formatVND.format(mOrder.getTotalPayment()) + " đ");
            // payment type
            switch (mOrder.getPaymentMethod()) {
                case Constants.Value.CASH: {
                    tvPaymentType.setText(R.string.cash);
                    break;
                }
                case Constants.Value.COIN: {
                    tvPaymentType.setText(R.string.coin);
                    break;
                }
                case Constants.Value.BILL: {
                    tvPaymentType.setText(R.string.bill);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private Handler mHandler = new Handler();
    private Runnable openRatingRunnable = this::openRatingDialog;

    @SuppressLint("HandlerLeak")
    private void openRatingDialog() {
        new DialogOrderRating(OrderDetailsActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mOrder = (Order) msg.obj;
                    setDataOrder();
                }
            }
        }, mOrder);
    }

    private void cancelOrder() {
        isLoading = true;
        Call<BaseResponse> cancelOrder = mainService.cancelOrder(mToken, mOrderId);
        cancelOrder.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse cancelOrderResponse = response.body();
                if (cancelOrderResponse != null) {
                    if (cancelOrderResponse.isSuccess()) {
                        Toast.makeText(OrderDetailsActivity.this, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        mOrder.setStatus(Constants.Value.CANCELED);
                        setDataOrder();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (cancelOrderResponse.getError() != null && cancelOrderResponse.getError().getMessage() != null && !cancelOrderResponse.getError().getMessage().isEmpty())
                            message = cancelOrderResponse.getError().getMessage();
                        Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailsActivity.this, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                }
                showContent();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(OrderDetailsActivity.this, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                showContent();
            }
        });
    }

    private void confirmReceived() {
        isLoading = true;
        Call<BaseResponse> confirmReceived = mainService.confirmReceived(mToken, mOrderId);
        confirmReceived.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse cancelOrderResponse = response.body();
                if (cancelOrderResponse != null) {
                    if (cancelOrderResponse.isSuccess()) {
                        Toast.makeText(OrderDetailsActivity.this, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                        mOrder.setReceived(true);
                        setDataOrder();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (cancelOrderResponse.getError() != null && cancelOrderResponse.getError().getMessage() != null && !cancelOrderResponse.getError().getMessage().isEmpty())
                            message = cancelOrderResponse.getError().getMessage();
                        Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        showContent();
                    }
                } else {
                    Toast.makeText(OrderDetailsActivity.this, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                    showContent();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(OrderDetailsActivity.this, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                showContent();
            }
        });
    }

    private void showLoading(boolean goneContent) {
        if (goneContent) {
            swipeRefresh.setRefreshing(true);
            layoutContent.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
        } else {
            layoutLoading.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(@NonNull String error) {
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvError.setText(error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.INSERT_ORDER: {
                Order order = (Order) event.getObject();
                if (order != null && order.getId() == mOrder.getId()) {
                    mOrder = order;
                    setDataOrder();
                }
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
}
