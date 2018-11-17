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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<Product> mProducts;
    private LayoutInflater mInflater;
    private RequestManager mRequestManager;
    private ProductListener mProductListener;
    private int mScreenWidth;

    public ProductAdapter(Context context, ArrayList<Product> products, ProductListener productListener) {
        this.mContext = context;
        this.mProducts = products;
        this.mProductListener = productListener;
        this.mInflater = LayoutInflater.from(mContext);
        this.mRequestManager = Glide.with(mContext);
        this.mScreenWidth = Utils.getScreenWidth(mContext);
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_product, viewGroup, false);
        return new CustomViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder customViewholder, int position) {
        customViewholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
        //return Integer.MAX_VALUE;
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view)
        LinearLayout itemView;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TvSegoeuiBold tvName;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            int realPosition = position % mProducts.size();
            Product product = mProducts.get(realPosition);
            //size
            itemView.getLayoutParams().width = mScreenWidth / 3;
            //image
            String image = product.getThumbnail();
            RequestOptions requestOptions = new RequestOptions()
                    .centerInside()
                    .placeholder(R.drawable.test_img_product)
                    .error(R.drawable.test_img_product);
            if (image != null && image.isEmpty())
                mRequestManager.load(image).apply(requestOptions).into(ivImage);
            else ivImage.setImageResource(R.drawable.test_img_product);
            //name
            String name = product.getName();
            if (name != null && !name.isEmpty()) {
                tvName.setText(name);
                tvName.setVisibility(View.VISIBLE);
            } else tvName.setVisibility(View.INVISIBLE);
            //selected
            if (product.isSelected()) {
                ivSelected.setImageResource(R.drawable.ic_radio_button_active);
            } else {
                ivSelected.setImageResource(R.drawable.ic_radio_button);
            }
            //click
            itemView.setOnClickListener(v -> {
                if (product.isSelected()) {
                    product.setSelected(false);
                    mProducts.get(realPosition).setSelected(false);
                    mProductListener.selectProduct(product);
                    ivSelected.setImageResource(R.drawable.ic_radio_button);
                } else {
                    product.setSelected(true);
                    mProducts.get(realPosition).setSelected(true);
                    mProductListener.selectProduct(product);
                    ivSelected.setImageResource(R.drawable.ic_radio_button_active);
                }
            });
        }
    }
}
