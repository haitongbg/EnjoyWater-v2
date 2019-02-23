package com.enjoywater.adapter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.listener.HomeListener;
import com.enjoywater.model.News;
import com.enjoywater.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaleNewsAdapter extends RecyclerView.Adapter<SaleNewsAdapter.CustomViewholder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<News> mSaleNewsList;
    private HomeListener mHomeListener;

    public SaleNewsAdapter(Context mContext, ArrayList<News> saleNewsList, HomeListener homeListener) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mSaleNewsList = saleNewsList;
        this.mHomeListener = homeListener;
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_home_list_sale_news_item, viewGroup, false);
        return new CustomViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder customViewholder, int position) {
        customViewholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mSaleNewsList.size();
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.item_view)
        CardView itemView;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            News news = mSaleNewsList.get(position);
            if (news.getImage() != null) Utils.loadImage(mContext, ivImage, news.getImage());
            String title = news.getTitle();
            if (title != null && !title.isEmpty()) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            } else tvTitle.setVisibility(View.GONE);
            ((RecyclerView.LayoutParams) itemView.getLayoutParams()).leftMargin = position == 0 ? mContext.getResources().getDimensionPixelSize(R.dimen.size_15) : mContext.getResources().getDimensionPixelSize(R.dimen.size_5);
            ((RecyclerView.LayoutParams) itemView.getLayoutParams()).rightMargin = position == (mSaleNewsList.size() - 1) ? mContext.getResources().getDimensionPixelSize(R.dimen.size_15) : mContext.getResources().getDimensionPixelSize(R.dimen.size_5);
            itemView.setOnClickListener(v -> mHomeListener.goSaleNewsDetail(news));
        }
    }
}
