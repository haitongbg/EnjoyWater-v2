package com.enjoywater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.view.TvSegoeuiSemiBold;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductFragment extends Fragment {


    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.rv_selected_products)
    RecyclerView rvSelectedProducts;
    @BindView(R.id.tv_total_product_price)
    TvSegoeuiSemiBold tvTotalProductPrice;
    @BindView(R.id.tv_total_delivery_fee)
    TvSegoeuiSemiBold tvTotalDeliveryFee;
    @BindView(R.id.tv_total_discount)
    TvSegoeuiSemiBold tvTotalDiscount;
    @BindView(R.id.tv_total_price)
    TvSegoeuiSemiBold tvTotalPrice;
    @BindView(R.id.tv_adress)
    TextView tvAdress;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    public static ProductFragment newInstance() {
        ProductFragment homeFragment = new ProductFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }
}
