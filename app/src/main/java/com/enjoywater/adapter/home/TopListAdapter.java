package com.enjoywater.adapter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.enjoywater.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopListAdapter extends RecyclerView.Adapter<TopListAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<Object> mObjects;
    private LayoutInflater mInflater;

    public TopListAdapter(Context mContext, ArrayList<Object> mObjects) {
        this.mContext = mContext;
        this.mObjects = mObjects;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_home_toplist_item, viewGroup, false);
        return new CustomViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder customViewholder, int position) {
        customViewholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.iv_hot_sale)
        ImageView ivHotSale;
        @BindView(R.id.item_view)
        CardView itemView;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            if (position == 0) ((RecyclerView.LayoutParams)itemView.getLayoutParams()).leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.size_7);
        }
    }
}
