package com.enjoywater.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.LoginActivity;
import com.enjoywater.activiy.MainActivity;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.activiy.OrderDetailsActivity;
import com.enjoywater.adapter.product.ProductAdapter;
import com.enjoywater.adapter.product.SelectedProductAdapter;
import com.enjoywater.listener.ProductListener;
import com.enjoywater.model.Address;
import com.enjoywater.model.Coupon;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;
import com.enjoywater.model.Order;
import com.enjoywater.model.Product;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.dialog.DialogActiveAccount;
import com.enjoywater.view.dialog.DialogOrderAddress;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

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

public class ProductFragment extends Fragment {
    private static final String TAG = "ProductFragment";
    public static final int REQUEST_CODE_ADDRESS = 112;
    public static final int SHIP_TYPE_2H = 1;
    public static final int SHIP_TYPE_24H = 2;
    public static final int SHIP_TYPE_DATE = 3;
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.rv_selected_products)
    RecyclerView rvSelectedProducts;
    @BindView(R.id.tv_total_product_price)
    TextView tvTotalProductPrice;
    @BindView(R.id.checkbox_delivery_climb)
    CheckBox checkboxDeliveryClimb;
    @BindView(R.id.tv_total_delivery_fee)
    TextView tvTotalDeliveryFee;
    @BindView(R.id.checkbox_ship_in_2_hours)
    CheckBox checkboxShipIn2Hours;
    @BindView(R.id.checkbox_ship_in_24_hours)
    CheckBox checkboxShipIn24Hours;
    @BindView(R.id.tv_ship_type_discount)
    TextView tvShipIn24HoursDiscount;
    @BindView(R.id.checkbox_choose_ship_date)
    CheckBox checkboxChooseShipDate;
    @BindView(R.id.tv_choose_ship_date_discount)
    TextView tvChooseShipDateDiscount;
    @BindView(R.id.layout_choose_ship_date)
    LinearLayout layoutChooseShipDate;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.edt_coupon)
    EditText edtCoupon;
    @BindView(R.id.btn_apply_coupon)
    Button btnApplyCoupon;
    @BindView(R.id.tv_coupon_code)
    TextView tvCouponCode;
    @BindView(R.id.btn_cancel_coupon)
    ImageView btnCancelCoupon;
    @BindView(R.id.tv_counpon_discount)
    TextView tvCounponDiscount;
    @BindView(R.id.layout_coupon_discount)
    LinearLayout layoutCouponDiscount;
    @BindView(R.id.layout_coupon)
    LinearLayout layoutCoupon;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.checkbox_pay_by_cash)
    CheckBox checkboxPayByCash;
    @BindView(R.id.checkbox_pay_by_coin)
    CheckBox checkboxPayByCoin;
    @BindView(R.id.checkbox_pay_by_bill)
    CheckBox checkboxPayByBill;
    @BindView(R.id.btn_change_address)
    TextView btnChangeAddress;
    @BindView(R.id.ripple_forget_password)
    RippleView rippleForgetPassword;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
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
    @BindView(R.id.edt_note)
    EditText edtNote;
    private Context mContext;
    private MainService mainService;
    private boolean isLoading = false;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private ArrayList<Product> mProducts = new ArrayList<>();
    private ArrayList<Product> mSelectedProducts = new ArrayList<>();
    private ProductAdapter mProductAdapter;
    private SelectedProductAdapter mSelectedProductAdapter;
    private Address mAddress;
    private boolean isValidAddress = false;
    private int mTotalPrice, mTotalProductsPrice, mTotalProductDiscount, mTotalDeliveryFee;
    private ArrayList<City> mCities;
    private int mShipType;
    private String mPaymentType;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar = Calendar.getInstance();
    private Coupon mCoupon;
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");
    private DecimalFormat formatPercent = new DecimalFormat("#.0");
    private Order mLastOrderCreated;

    public static ProductFragment newInstance() {
        ProductFragment productFragment = new ProductFragment();
        return productFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(mContext);
        mToken = Utils.getToken(mContext);
        mCities = MyApplication.getInstance().getCities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
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
                        mProducts.clear();
                        if (mProductAdapter != null) mProductAdapter.notifyDataSetChanged();
                        mSelectedProducts.clear();
                        if (mSelectedProductAdapter != null)
                            mSelectedProductAdapter.notifyDataSetChanged();
                        updatePrice();
                        getProducts();
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
        rvSelectedProducts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvSelectedProducts.setNestedScrollingEnabled(false);
        rvProducts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        /*SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvProducts);*/
        //
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mShipType = SHIP_TYPE_DATE;
                updatePrice();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.setOnCancelListener(dialog -> {
            if (mShipType == SHIP_TYPE_2H) {
                checkboxChooseShipDate.setChecked(false);
                checkboxShipIn2Hours.setChecked(true);
            } else if (mShipType == SHIP_TYPE_24H) {
                checkboxChooseShipDate.setChecked(false);
                checkboxShipIn24Hours.setChecked(true);
            }
        });
        checkboxDeliveryClimb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updatePrice();
        });
        checkboxShipIn2Hours.setOnClickListener(v -> {
            checkboxShipIn24Hours.setChecked(false);
            checkboxChooseShipDate.setChecked(false);
            checkboxShipIn2Hours.setChecked(true);
            mShipType = SHIP_TYPE_2H;
            updatePrice();
        });
        checkboxShipIn24Hours.setOnClickListener(v -> {
            checkboxShipIn2Hours.setChecked(false);
            checkboxChooseShipDate.setChecked(false);
            checkboxShipIn24Hours.setChecked(true);
            mShipType = SHIP_TYPE_24H;
            updatePrice();
        });
        checkboxChooseShipDate.setOnClickListener(v -> {
            checkboxShipIn2Hours.setChecked(false);
            checkboxShipIn24Hours.setChecked(false);
            checkboxChooseShipDate.setChecked(true);
            datePickerDialog.show();
        });
        checkboxPayByCash.setOnClickListener(v -> {
            checkboxPayByCash.setChecked(true);
            checkboxPayByCoin.setChecked(false);
            checkboxPayByBill.setChecked(false);
            mPaymentType = Constants.Value.CASH;
        });
        checkboxPayByCoin.setOnClickListener(v -> {
            checkboxPayByCash.setChecked(false);
            checkboxPayByCoin.setChecked(true);
            checkboxPayByBill.setChecked(false);
            mPaymentType = Constants.Value.COIN;
        });
        checkboxPayByBill.setOnClickListener(v -> {
            checkboxPayByCash.setChecked(false);
            checkboxPayByCoin.setChecked(false);
            checkboxPayByBill.setChecked(true);
            mPaymentType = Constants.Value.BILL;
        });
        checkboxDeliveryClimb.setChecked(false);
        checkboxShipIn2Hours.setChecked(false);
        checkboxShipIn24Hours.setChecked(true);
        checkboxChooseShipDate.setChecked(false);
        mShipType = SHIP_TYPE_24H;
        checkboxPayByCash.setChecked(true);
        checkboxPayByCoin.setChecked(false);
        checkboxPayByBill.setChecked(false);
        mPaymentType = Constants.Value.CASH;
        checkboxPayByBill.setVisibility(View.GONE);
        tvChooseShipDateDiscount.setVisibility(View.GONE);
        layoutCouponDiscount.setVisibility(View.GONE);
        btnApplyCoupon.setOnClickListener(v -> {
            applyCoupon();
        });
        btnCancelCoupon.setOnClickListener(v -> {
            cancelCoupon();
        });
        btnChangeAddress.setOnClickListener(v -> {
            changeAddress();
        });
        btnOrder.setOnClickListener(v -> {
            validateOrder();
        });
        showLoading(true);
        getProducts();
    }

    private void getProducts() {
        isLoading = true;
        Call<BaseResponse> getProducts = mainService.getListProducts(mToken, 20, 1);
        getProducts.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse getProductsResponse = response.body();
                if (getProductsResponse != null) {
                    if (getProductsResponse.isSuccess() && getProductsResponse.getData() != null) {
                        if (getProductsResponse.getData().isJsonArray()) {
                            ArrayList<Product> products = new ArrayList<>();
                            JsonArray arrayProducts = getProductsResponse.getData().getAsJsonArray();
                            if (arrayProducts.size() > 0) {
                                for (int i = 0, z = arrayProducts.size(); i < z; i++) {
                                    if (arrayProducts.get(i).isJsonObject()) {
                                        Product product = gson.fromJson(arrayProducts.get(i).getAsJsonObject().toString(), Product.class);
                                        if (product != null) products.add(product);
                                    }
                                }
                            }
                            setDataProducts(products);
                        } else showError(Constants.DataNotify.DATA_ERROR);
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (getProductsResponse.getError() != null && getProductsResponse.getError().getMessage() != null && !getProductsResponse.getError().getMessage().isEmpty())
                            message = getProductsResponse.getError().getMessage();
                        showError(message);
                    }
                } else showError(Constants.DataNotify.DATA_ERROR);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                showError(Constants.DataNotify.DATA_ERROR);
            }
        });

    }

    private void setDataProducts(ArrayList<Product> products) {
        if (!products.isEmpty()) {
            showContent();
            int insertPosition = mProducts.size();
            mProducts.addAll(products);
            if (mProductAdapter == null) {
                mProducts.get(0).setSelected(true);
                mProducts.get(1).setSelected(true);
                mProductAdapter = new ProductAdapter(mContext, mProducts, mProductListener);
                rvProducts.setAdapter(mProductAdapter);
                mProductListener.selectProduct(mProducts.get(0));
                mProductListener.selectProduct(mProducts.get(1));
            } else {
                mProductAdapter.notifyItemRangeInserted(insertPosition, products.size());
            }
            setDataAddress();
        } else if (mProducts.isEmpty()) showError("Lỗi dữ liệu sản phẩm, xin thử lại");
    }

    private void setDataAddress() {
        if (mUser != null && mUser.getUserInfo().getOtherAddress() != null && !mUser.getUserInfo().getOtherAddress().isEmpty()) {
            mAddress = mUser.getUserInfo().getOtherAddress().get(0);
            if (mAddress != null) {
                String name = mAddress.getName();
                tvName.setText(((name != null && !name.isEmpty()) ? name : "Unknown name") + ",");
                tvName.setVisibility(View.VISIBLE);
                String phone = mAddress.getPhone();
                tvPhone.setText(((phone != null && !phone.isEmpty()) ? phone : "unknown phone") + ",");
                tvPhone.setVisibility(View.VISIBLE);
                int count = 0;
                String fullAddress = mAddress.getAddress();
                if (mAddress.getCityId() != null && !mAddress.getCityId().isEmpty()) {
                    for (City city : mCities) {
                        if (city.getId() != null && city.getId().equals(mAddress.getCityId()) && city.getName() != null && !city.getName().isEmpty()) {
                            ArrayList<District> districts = city.getDistricts();
                            if (districts != null && !districts.isEmpty() && mAddress.getDistrictId() != null && !mAddress.getDistrictId().isEmpty()) {
                                for (District district : districts) {
                                    if (district.getId() != null && district.getId().equals(mAddress.getDistrictId()) && district.getName() != null && !district.getName().isEmpty()) {
                                        ArrayList<Ward> wards = district.getWards();
                                        if (wards != null && !wards.isEmpty() && mAddress.getWardId() != null && !mAddress.getWardId().isEmpty()) {
                                            for (Ward ward : wards) {
                                                if (ward.getId() != null && !ward.getId().equals(mAddress.getWardId()) && ward.getName() != null && !ward.getName().isEmpty()) {
                                                    if (ward.getType() != null && !ward.getType().isEmpty())
                                                        fullAddress += (", " + ward.getType() + " " + ward.getName());
                                                    else fullAddress += (", " + ward.getName());
                                                    count++;
                                                    break;
                                                }
                                            }
                                        }
                                        if (district.getType() != null && !district.getType().isEmpty())
                                            fullAddress += (", " + district.getType() + " " + district.getName());
                                        else fullAddress += (", " + district.getName());
                                        count++;
                                        break;
                                    }
                                }
                            }
                            if (city.getType() != null && !city.getType().isEmpty())
                                fullAddress += (", " + city.getType() + " " + city.getName());
                            else fullAddress += (", " + city.getName());
                            count++;
                            break;
                        }
                    }
                }
                isValidAddress = count >= 2;
                tvAddress.setText((fullAddress != null && !fullAddress.isEmpty()) ? fullAddress : "Chưa có địa chỉ.");
                btnChangeAddress.setVisibility(View.VISIBLE);
            } else {
                tvName.setVisibility(View.GONE);
                tvPhone.setVisibility(View.GONE);
                btnChangeAddress.setVisibility(View.GONE);
                tvAddress.setText("Chưa có địa chỉ.");
                isValidAddress = false;
            }
        } else {
            tvName.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            btnChangeAddress.setVisibility(View.GONE);
            tvAddress.setText("Chưa có địa chỉ.");
            isValidAddress = false;
        }
    }

    private void changeAddress() {

    }

    private ProductListener mProductListener = new ProductListener() {
        @Override
        public void selectProduct(Product product) {
            if (product.isSelected()) {
                boolean existed = false;
                if (!mSelectedProducts.isEmpty()) {
                    for (Product selectedProduct : mSelectedProducts) {
                        if (selectedProduct.getId() == product.getId()) {
                            existed = true;
                            break;
                        }
                    }
                }
                if (!existed) {
                    product.setVolume(1);
                    product.setProductId(product.getId());
                    mSelectedProducts.add(product);
                }
            } else if (!mSelectedProducts.isEmpty()) {
                for (int i = 0, z = mSelectedProducts.size(); i < z; i++) {
                    if (mSelectedProducts.get(i).getId() == product.getId()) {
                        mSelectedProducts.remove(i);
                        break;
                    }
                }
            }
            if (!mSelectedProducts.isEmpty()) {
                if (mSelectedProductAdapter == null) {
                    mSelectedProductAdapter = new SelectedProductAdapter(mContext, mSelectedProducts, mProductListener);
                    rvSelectedProducts.setAdapter(mSelectedProductAdapter);
                } else mSelectedProductAdapter.notifyDataSetChanged();
                rvSelectedProducts.setVisibility(View.VISIBLE);
            } else {
                rvSelectedProducts.setVisibility(View.GONE);
            }
            updatePrice();
        }

        public void updateProduct(Product product) {
            if (mSelectedProducts.size() > 0) {
                for (int i = 0, z = mSelectedProducts.size(); i < z; i++) {
                    if (mSelectedProducts.get(i).getId() == product.getId()) {
                        mSelectedProducts.set(i, product);
                        break;
                    }
                }
                updatePrice();
            }
        }
    };

    private void applyCoupon() {
        String code = edtCoupon.getText().toString();
        if (code.isEmpty()) {
            Toast.makeText(mContext, "Bạn chưa nhập mã giảm giá", Toast.LENGTH_SHORT).show();
        } else {
            isLoading = true;
            layoutLoading.setVisibility(View.VISIBLE);
            mCoupon = null;
            updatePrice();
            Call<BaseResponse> getCouponDetails = mainService.getCouponDetails(code);
            getCouponDetails.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    isLoading = false;
                    layoutLoading.setVisibility(View.GONE);
                    BaseResponse getCounponDetailsResponse = response.body();
                    if (getCounponDetailsResponse != null) {
                        if (getCounponDetailsResponse.isSuccess() && getCounponDetailsResponse.getData() != null) {
                            if (getCounponDetailsResponse.getData().isJsonObject()) {
                                Coupon coupon = gson.fromJson(getCounponDetailsResponse.getData(), Coupon.class);
                                if (coupon != null && coupon.isEnabled()) {
                                    if (coupon.getStarted() * 1000 > System.currentTimeMillis())
                                        Toast.makeText(mContext, "Rất tiếc voucher này chưa được áp dụng.", Toast.LENGTH_SHORT).show();
                                    else if ((coupon.getEnded() * 1000 + 300000) < System.currentTimeMillis())
                                        Toast.makeText(mContext, "Rất tiếc voucher này đã hết hạn.", Toast.LENGTH_SHORT).show();
                                    else {
                                        mCoupon = coupon;
                                        updatePrice();
                                    }
                                } else
                                    Toast.makeText(mContext, "Rất tiếc voucher này đã hết hạn.", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(mContext, "Voucher không tồn tại.", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = "Voucher không tồn tại.";
                            if (getCounponDetailsResponse.getError() != null && getCounponDetailsResponse.getError().getMessage() != null && !getCounponDetailsResponse.getError().getMessage().isEmpty() && !getCounponDetailsResponse.getError().getMessage().equalsIgnoreCase("Invalid coupon"))
                                message = getCounponDetailsResponse.getError().getMessage();
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(mContext, "Voucher không tồn tại.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    isLoading = false;
                    layoutLoading.setVisibility(View.GONE);
                    t.printStackTrace();
                    Toast.makeText(mContext, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cancelCoupon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Bạn muốn gỡ bỏ mã giảm giá này?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCoupon = null;
                        layoutCouponDiscount.setVisibility(View.GONE);
                        updatePrice();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updatePrice() {
        mTotalProductsPrice = 0;
        mTotalProductDiscount = 0;
        mTotalDeliveryFee = 0;
        if (mSelectedProducts != null && !mSelectedProducts.isEmpty()) {
            for (Product product : mSelectedProducts) {
                mTotalProductsPrice += product.getVolume() * product.getAsk();
                mTotalProductDiscount += product.getVolume() * product.getDiscount();
                mTotalDeliveryFee += product.getVolume() * product.getDeliveryFee();
            }
            tvTotalProductPrice.setText(formatVND.format(mTotalProductsPrice) + " đ");
            tvTotalDeliveryFee.setText(formatVND.format(mTotalDeliveryFee) + " đ");
            tvShipIn24HoursDiscount.setText("-" + formatVND.format(mTotalProductDiscount) + " đ");
            if (mTotalProductDiscount > 0 && mTotalProductsPrice > 0) {
                float discountPercent = (float) (mTotalProductDiscount * 100) / mTotalProductsPrice;
                if (discountPercent >= 1.0f)
                    checkboxShipIn24Hours.setText("Giao hàng trong 24 giờ (giảm " + formatPercent.format(discountPercent) + "%)");
                else
                    checkboxShipIn24Hours.setText("Giao hàng trong 24 giờ (giảm 0" + formatPercent.format(discountPercent) + "%)");
            } else checkboxShipIn24Hours.setText("Giao hàng trong 24 giờ (giảm 0%)");
        } else {
            tvTotalProductPrice.setText("0 đ");
            tvTotalDeliveryFee.setText("0 đ");
            tvShipIn24HoursDiscount.setText("-0 đ");
            checkboxShipIn24Hours.setText("Giao hàng trong 24 giờ (giảm 0%)");
        }
        mTotalPrice = mTotalProductsPrice;
        if (checkboxDeliveryClimb.isChecked()) {
            mTotalPrice += mTotalDeliveryFee;
            tvTotalDeliveryFee.setTextColor(mContext.getResources().getColor(R.color.indigo_blue));
        } else tvTotalDeliveryFee.setTextColor(mContext.getResources().getColor(R.color.black_c));
        if (checkboxShipIn24Hours.isChecked()) {
            mTotalPrice -= mTotalProductDiscount;
            tvShipIn24HoursDiscount.setTextColor(mContext.getResources().getColor(R.color.indigo_blue));
        } else
            tvShipIn24HoursDiscount.setTextColor(mContext.getResources().getColor(R.color.black_c));
        if (checkboxChooseShipDate.isChecked()) {
            mTotalPrice -= mTotalProductDiscount;
            tvChooseShipDateDiscount.setVisibility(View.VISIBLE);
            tvChooseShipDateDiscount.setTextColor(mContext.getResources().getColor(R.color.indigo_blue));
            String text = "Giao ngày " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
            if (mTotalProductDiscount > 0 && mTotalProductsPrice > 0) {
                tvChooseShipDateDiscount.setText("-" + formatVND.format(mTotalProductDiscount) + " đ");
                float discountPercent = (float) (mTotalProductDiscount * 100) / mTotalProductsPrice;
                if (discountPercent >= 1.0f)
                    checkboxChooseShipDate.setText(text + " (giảm " + formatPercent.format(discountPercent) + "%)");
                else
                    checkboxChooseShipDate.setText(text + " (giảm 0" + formatPercent.format(discountPercent) + "%)");
            } else {
                checkboxChooseShipDate.setText(text + " (giảm 0%)");
                tvChooseShipDateDiscount.setText("-0 đ");
            }
        } else
            tvChooseShipDateDiscount.setTextColor(mContext.getResources().getColor(R.color.black_c));
        if (mCoupon != null) {
            if (mTotalProductsPrice < mCoupon.getRequireMinOrderValue()) {
                layoutCouponDiscount.setVisibility(View.GONE);
                Toast.makeText(mContext, "Rất tiếc, giá trị đơn hàng của bạn chưa đủ để sử dụng mã giảm giá này.", Toast.LENGTH_LONG).show();
                mCoupon = null;
            } else {
                layoutCouponDiscount.setVisibility(View.VISIBLE);
                edtCoupon.setText("");
                tvCouponCode.setText(mCoupon.getCode());
                int couponDiscount = 0;
                if (mCoupon.getType().equals("unit")) {
                    couponDiscount = mCoupon.getValue();
                } else if (mCoupon.getType().equals("percent")) {
                    couponDiscount = mTotalProductsPrice * mCoupon.getValue() / 100;
                    if (mCoupon.getMaxDiscountValue() > 0 && couponDiscount > mCoupon.getMaxDiscountValue())
                        couponDiscount = mCoupon.getMaxDiscountValue();
                    couponDiscount -= couponDiscount % 1000;
                }
                couponDiscount -= couponDiscount % 1000;
                tvCounponDiscount.setText("-" + formatVND.format(couponDiscount) + " đ");
                mTotalPrice -= couponDiscount;
            }
        } else {
            layoutCouponDiscount.setVisibility(View.GONE);
        }
        tvTotalPrice.setText((mTotalPrice > 0 ? formatVND.format(mTotalPrice) : 0) + " đ");
    }

    private CountDownTimer countDownDelaySending;
    private long delaySendingActiveCode = 0;

    @SuppressLint("HandlerLeak")
    private void validateOrder() {
        if (mSelectedProducts == null || mSelectedProducts.isEmpty()) {
            Toast.makeText(mContext, "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
        } else if (mTotalProductsPrice <= 0) {
            Toast.makeText(mContext, "Vui lòng đặt số lượng sản phẩm", Toast.LENGTH_SHORT).show();
        } else if (mUser == null || mToken.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Vui lòng đăng nhập để tiến hành đặt hàng.")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN);
                            (getActivity()).overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (!mUser.getUserInfo().isActivated()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Vui lòng kích hoạt tài khoản của bạn để tiến hành đặt hàng.")
                    .setCancelable(false)
                    .setPositiveButton("Kích hoạt", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new DialogActiveAccount(mContext, new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if (msg.what == Constants.Value.ACTION_SUCCESS) {
                                        mUser.getUserInfo().setActivated(true);
                                        Utils.saveUser(mContext, mUser);
                                        //validateOrder();
                                    } else if (msg.what == Constants.Value.ACTION_CLOSE) {
                                        delaySendingActiveCode = (long) msg.obj;
                                        countDownDelaySending = new CountDownTimer(delaySendingActiveCode, 1000) {
                                            public void onTick(long millisUntilFinished) {
                                                delaySendingActiveCode = millisUntilFinished;
                                            }

                                            public void onFinish() {
                                                delaySendingActiveCode = 0;
                                            }
                                        };
                                        countDownDelaySending.start();
                                    }
                                }
                            }, delaySendingActiveCode);
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (!isValidAddress) {
            new DialogOrderAddress(mContext, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constants.Value.ACTION_SUCCESS) {
                        Address address = (Address) msg.obj;
                        mUser.getUserInfo().getOtherAddress().add(address);
                        Utils.saveUser(mContext, mUser);
                        setDataAddress();
                        //validateOrder();
                    }
                }
            });
        } else if (checkboxPayByCoin.isChecked() && mUser.getUserInfo().getCoin() < mTotalPrice) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Điểm thưởng của bạn không đủ để thanh toán đơn hàng này. \n\nThanh toán bằng t iền mặt?")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý", (dialog, id) -> {
                        checkboxPayByCash.setChecked(true);
                        checkboxPayByCoin.setChecked(false);
                        mPaymentType = Constants.Value.CASH;
                        confirmOrder();
                    })
                    .setNegativeButton("Hủy", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Xác nhận đặt hàng")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialog, id) -> {
                    makeOrder();
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void makeOrder() {
        if (!Utils.isInternetOn(mContext)) {
            Toast.makeText(mContext, Constants.DataNotify.NO_CONNECTION, Toast.LENGTH_SHORT).show();
        } else if (!isLoading) {
            isLoading = true;
            showLoading(false);
            Order order = new Order();
            order.setNotes(edtNote.getText().toString());
            order.setProvince(mAddress.getCityId());
            order.setDistrict(mAddress.getDistrictId());
            order.setAddress(mAddress.getAddress());
            order.setReceiverName(mAddress.getName());
            order.setPhone(mAddress.getPhone());
            order.setOrderBySchedule(mShipType == SHIP_TYPE_DATE ? calendar.getTimeInMillis() / 1000 : 0);
            if (mCoupon != null) order.setCouponCode(mCoupon.getCode());
            order.setDeliveryOpts(mShipType == SHIP_TYPE_2H ? "2h" : "24h");
            order.setDeliveryClimb(checkboxDeliveryClimb.isChecked());
            order.setPaymentMethod(mPaymentType);
            order.setItems(mSelectedProducts);
            Call<BaseResponse> createOrder = mainService.createOrder(mToken, gson.toJsonTree(order).getAsJsonObject());
            createOrder.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    isLoading = false;
                    showContent();
                    BaseResponse createOrderResponse = response.body();
                    if (createOrderResponse != null) {
                        if (createOrderResponse.isSuccess() && createOrderResponse.getData() != null && createOrderResponse.getData().isJsonObject()) {
                            Toast.makeText(mContext, R.string.order_success, Toast.LENGTH_SHORT).show();
                            mCoupon = null;
                            mLastOrderCreated = gson.fromJson(createOrderResponse.getData(), Order.class);
                            Utils.saveString(mContext, Constants.Key.LAST_ORDER_CREATED, createOrderResponse.getData().toString());
                            updatePrice();
                            EventBus.getDefault().post(new EventBusMessage(Constants.Key.INSERT_ORDER, mLastOrderCreated));
                            Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                            intent.putExtra(Constants.Key.ORDER, mLastOrderCreated);
                            startActivity(intent);
                            (getActivity()).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
                        } else {
                            String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                            if (createOrderResponse.getError() != null && createOrderResponse.getError().getMessage() != null && !createOrderResponse.getError().getMessage().isEmpty())
                                message = createOrderResponse.getError().getMessage();
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(mContext, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                    showContent();
                }
            });
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN && resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS) {
            //Toast.makeText(mContext, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
            mUser = Utils.getUser(mContext);
            mToken = Utils.getToken(mContext);
            setDataAddress();
        } //else Toast.makeText(mContext, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.PROFILE_UPDATED: {
                mUser = (User) event.getObject();
                setDataAddress();
                break;
            }
            default: {
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
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }
}
