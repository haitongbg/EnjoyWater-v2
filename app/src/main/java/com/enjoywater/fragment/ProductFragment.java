package com.enjoywater.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.LoginActivity;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.adapter.product.ProductAdapter;
import com.enjoywater.adapter.product.SelectedProductAdapter;
import com.enjoywater.listener.ProductListener;
import com.enjoywater.model.Product;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.rv_selected_products)
    RecyclerView rvSelectedProducts;
    @BindView(R.id.tv_total_product_price)
    TvSegoeuiSemiBold tvTotalProductPrice;
    @BindView(R.id.checkbox_delivery)
    CheckBox checkboxDelivery;
    @BindView(R.id.tv_total_delivery_fee)
    TvSegoeuiSemiBold tvTotalDeliveryFee;
    @BindView(R.id.checkbox_ship_in_2_hours)
    CheckBox checkboxShipIn2Hours;
    @BindView(R.id.checkbox_ship_in_24_hours)
    CheckBox checkboxShipIn24Hours;
    @BindView(R.id.tv_ship_in_24_hours_discount)
    TvSegoeuiSemiBold tvShipIn24HoursDiscount;
    @BindView(R.id.tv_total_price)
    TvSegoeuiSemiBold tvTotalPrice;
    @BindView(R.id.checkbox_pay_by_cash)
    CheckBox checkboxPayByCash;
    @BindView(R.id.checkbox_pay_by_point)
    CheckBox checkboxPayByPoint;
    @BindView(R.id.checkbox_pay_by_bill)
    CheckBox checkboxPayByBill;
    @BindView(R.id.btn_change_address)
    TvSegoeuiSemiBold btnChangeAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_adress)
    TextView tvAdress;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
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
        mToken = Utils.getString(mContext, Constants.Key.TOKEN, "");
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
        rvSelectedProducts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvSelectedProducts.setNestedScrollingEnabled(false);
        rvProducts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        /*SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvProducts);*/
        //
        checkboxDelivery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updatePrice();
        });
        checkboxShipIn24Hours.setOnClickListener(v -> {
            checkboxShipIn2Hours.setChecked(false);
            checkboxShipIn24Hours.setChecked(true);
            updatePrice();
        });
        checkboxShipIn2Hours.setOnClickListener(v -> {
            checkboxShipIn24Hours.setChecked(false);
            checkboxShipIn2Hours.setChecked(true);
            updatePrice();
        });
        checkboxPayByCash.setOnClickListener(v -> {
            checkboxPayByCash.setChecked(true);
            checkboxPayByPoint.setChecked(false);
            checkboxPayByBill.setChecked(false);
        });
        checkboxPayByPoint.setOnClickListener(v -> {
            checkboxPayByCash.setChecked(false);
            checkboxPayByPoint.setChecked(true);
            checkboxPayByBill.setChecked(false);
        });
        checkboxPayByBill.setOnClickListener(v -> {
            checkboxPayByCash.setChecked(false);
            checkboxPayByPoint.setChecked(false);
            checkboxPayByBill.setChecked(true);
        });
        btnOrder.setOnClickListener(v -> {

        });
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
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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
            int insertPosition = mProducts.size();
            mProducts.addAll(products);
            if (mProductAdapter == null) {
                mProducts.get(0).setSelected(true);
                mProductAdapter = new ProductAdapter(mContext, mProducts, mProductListener);
                rvProducts.setAdapter(mProductAdapter);
                mProductListener.selectProduct(mProducts.get(0));
            } else {
                mProductAdapter.notifyItemRangeInserted(insertPosition, products.size());
            }
        } else if (mProducts.isEmpty()) showError("Lỗi dữ liệu sản phẩm, xin thử lại");
    }

    private ProductListener mProductListener = new ProductListener() {
        @Override
        public void selectProduct(Product product) {
            if (product.isSelected()) {
                boolean existed = false;
                if (!mSelectedProducts.isEmpty()) {
                    for (Product selectedProduct : mSelectedProducts) {
                        if (selectedProduct.getId().equals(product.getId())) {
                            existed = true;
                            break;
                        }
                    }
                }
                if (!existed) {
                    product.setCount(1);
                    mSelectedProducts.add(product);
                }
            } else if (!mSelectedProducts.isEmpty()) {
                for (int i = 0, z = mSelectedProducts.size(); i < z; i++) {
                    if (mSelectedProducts.get(i).getId().equals(product.getId())) {
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
                    if (mSelectedProducts.get(i).getId().equals(product.getId())) {
                        mSelectedProducts.set(i, product);
                        break;
                    }
                }
                updatePrice();
            }
        }
    };

    private void updatePrice() {
        DecimalFormat formatVND = new DecimalFormat("###,###,###");
        DecimalFormat formatPercent = new DecimalFormat("#.0");
        long totalProductsPrice = 0;
        long totalProductDiscount = 0;
        long totalDeliveryFee = 0;
        if (mSelectedProducts != null && !mSelectedProducts.isEmpty()) {
            for (Product product : mSelectedProducts) {
                totalProductsPrice += product.getCount() * product.getAsk();
                totalProductDiscount += product.getCount() * product.getDiscount();
                totalDeliveryFee += product.getCount() * product.getDeliveryFee();
            }
            tvTotalProductPrice.setText(formatVND.format(totalProductsPrice) + " đ");
            tvTotalDeliveryFee.setText(formatVND.format(totalDeliveryFee) + " đ");
            tvShipIn24HoursDiscount.setText("-" + formatVND.format(totalProductDiscount) + " đ");
            if (totalProductDiscount > 0) {
                float discountPercent = (float) (totalProductDiscount * 100) / totalProductsPrice;
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
        long totalPrice = totalProductsPrice;
        if (checkboxDelivery.isChecked()) totalPrice += totalDeliveryFee;
        if (checkboxShipIn24Hours.isChecked()) totalPrice -= totalProductDiscount;
        tvTotalPrice.setText(formatVND.format(totalPrice) + " đ");
    }

    private void showLoading() {

    }

    private void showContent() {

    }

    private void showError(String error) {

    }
}
