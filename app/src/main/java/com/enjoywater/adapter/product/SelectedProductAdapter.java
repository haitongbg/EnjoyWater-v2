package com.enjoywater.adapter.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.enjoywater.R;
import com.enjoywater.listener.ProductListener;
import com.enjoywater.model.Product;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.TvSegoeuiBold;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSemiBold;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedProductAdapter extends RecyclerView.Adapter<SelectedProductAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<Product> mProducts;
    private LayoutInflater mInflater;
    private ProductListener mProductListener;

    public SelectedProductAdapter(Context context, ArrayList<Product> products, ProductListener productListener) {
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
        @BindView(R.id.tv_count)
        TvSegoeuiSemiBold tvCount;
        @BindView(R.id.btn_add)
        ImageView btnAdd;
        @BindView(R.id.tv_price)
        TvSegoeuiSemiBold tvPrice;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            int realPosition = position % mProducts.size();
            Product product = mProducts.get(realPosition);
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
            int count = product.getCount();
            tvCount.setText(String .valueOf(count));
            if (count <= 1) btnSubtract.setImageResource(R.drawable.ic_subtract);
            else btnSubtract.setImageResource(R.drawable.ic_subtract_active);
            if (count >= 999) btnAdd.setImageResource(R.drawable.ic_add);
            else btnAdd.setImageResource(R.drawable.ic_add_active);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = product.getCount();
                    if (count < 999) {
                        count++;
                        product.setCount(count);
                        tvCount.setText(count + "");
                        mProducts.get(position).setCount(count);
                        mProductListener.updateProduct(product);
                        if (count == 2) btnSubtract.setImageResource(R.drawable.ic_subtract_active);
                        if (count == 999) btnAdd.setImageResource(R.drawable.ic_add);
                    }
                }
            });
            btnSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = product.getCount();
                    if (count > 1) {
                        count--;
                        product.setCount(count);
                        tvCount.setText(count + "");
                        mProducts.get(position).setCount(count);
                        mProductListener.updateProduct(product);
                        if (count == 1) btnSubtract.setImageResource(R.drawable.ic_subtract);
                        if (count == 998) btnAdd.setImageResource(R.drawable.ic_add_active);
                    } /*else {
                        product.setSelected(false);
                        mProductListener.selectProduct(product);
                    }*/
                }
            });
        }
    }
}
