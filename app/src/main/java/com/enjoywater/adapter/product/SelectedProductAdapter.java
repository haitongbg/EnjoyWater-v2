package com.enjoywater.adapter.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.enjoywater.R;
import com.enjoywater.listener.ProductListener;
import com.enjoywater.model.Product;
import com.enjoywater.view.TvSegoeuiSemiBold;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedProductAdapter extends RecyclerView.Adapter<SelectedProductAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<Product> mProducts;
    private LayoutInflater mInflater;
    private ProductListener mProductListener;

    public SelectedProductAdapter(Context context, ArrayList<Product> products, @Nullable ProductListener productListener) {
        this.mContext = context;
        this.mProducts = products;
        this.mProductListener = productListener;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_selected_product, viewGroup, false);
        return new CustomViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder customViewholder, int position) {
        customViewholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TvSegoeuiSemiBold tvName;
        @BindView(R.id.btn_subtract)
        ImageView btnSubtract;
        @BindView(R.id.edt_count)
        EditText edtCount;
        @BindView(R.id.btn_add)
        ImageView btnAdd;
        @BindView(R.id.tv_price)
        TvSegoeuiSemiBold tvPrice;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void setData(int position) {
            Product product = mProducts.get(position);
            Log.e("selectedProduct", (new Gson()).toJson(product));
            //name
            String name = product.getName();
            if (name != null && !name.isEmpty()) {
                tvName.setText(name);
                tvName.setVisibility(View.VISIBLE);
            } else tvName.setVisibility(View.INVISIBLE);
            //price
            DecimalFormat formatVNĐ = new DecimalFormat("###,###,###");
            tvPrice.setText(formatVNĐ.format(product.getAsk()) + " đ");
            //count
            int count = product.getVolume();
            if (mProductListener == null) {
                edtCount.setText("x" + count);
                btnAdd.setVisibility(View.GONE);
                btnSubtract.setVisibility(View.GONE);
                edtCount.setFocusable(false);
            } else {
                edtCount.setText(count + "");
                btnAdd.setVisibility(View.VISIBLE);
                btnSubtract.setVisibility(View.VISIBLE);
                if (count <= 1) btnSubtract.setImageResource(R.drawable.ic_subtract);
                else btnSubtract.setImageResource(R.drawable.ic_subtract_active);
                if (count >= 99) btnAdd.setImageResource(R.drawable.ic_add);
                else btnAdd.setImageResource(R.drawable.ic_add_active);
                edtCount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int c = 0;
                        if (s.length() > 0) c = Integer.parseInt(s.toString());
                        product.setVolume(c);
                        mProducts.get(position).setVolume(c);
                        mProductListener.updateProduct(product);
                        btnAdd.setImageResource(R.drawable.ic_add_active);
                        btnSubtract.setImageResource(R.drawable.ic_subtract_active);
                        if (c <= 1) btnSubtract.setImageResource(R.drawable.ic_subtract);
                        if (c == 99) btnAdd.setImageResource(R.drawable.ic_add);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                btnAdd.setOnClickListener(v -> {
                    int count1 = product.getVolume();
                    if (count1 < 99) {
                        count1++;
                        edtCount.setText(count1 + "");
                    }
                });
                btnSubtract.setOnClickListener(v -> {
                    int count12 = product.getVolume();
                    if (count12 > 1) {
                        count12--;
                        edtCount.setText(count12 + "");
                    }
                });
            }
        }
    }
}
