package com.enjoywater.adapter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.enjoywater.view.EmptyViewHolder;
import com.enjoywater.view.LoadmoreViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_UNKNOW = -1;
    private static final int TYPE_LOADMORE = 0;
    private static final int TYPE_SALE_NEWS = 1;
    private static final int TYPE_HOME_NEWS = 2;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<News> mHomes;
    private ArrayList<News> mSaleNewsList;
    private HomeListener mHomeListener;

    public HomeAdapter(Context context, ArrayList<News> homes, ArrayList<News> saleNews, HomeListener homeListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mHomes = homes;
        this.mSaleNewsList = saleNews;
        this.mHomeListener = homeListener;
    }

    @Override
    public int getItemViewType(int position) {
        News news = mHomes.get(position);
        if (news.isSaleNewsList()) return TYPE_SALE_NEWS;
        else if (news.isLoadmore()) return TYPE_LOADMORE;
        else return TYPE_HOME_NEWS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_SALE_NEWS: {
                View itemView = mInflater.inflate(R.layout.item_home_list_sale_news, viewGroup, false);
                return new SaleNewsListViewholder(itemView);
            }
            case TYPE_HOME_NEWS: {
                View itemView = mInflater.inflate(R.layout.item_home_news, viewGroup, false);
                return new HomeNewsViewholder(itemView);
            }
            case TYPE_LOADMORE: {
                View itemView = mInflater.inflate(R.layout.item_loadmore, viewGroup, false);
                return new LoadmoreViewHolder(itemView);
            }
            default: {
                View itemView = mInflater.inflate(R.layout.item_empty, viewGroup, false);
                return new EmptyViewHolder(itemView);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SaleNewsListViewholder)
            ((SaleNewsListViewholder) holder).setData(position);
        else if (holder instanceof HomeNewsViewholder)
            ((HomeNewsViewholder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return mHomes.size();
    }

    public class SaleNewsListViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_sale_news)
        RecyclerView rvSaleNews;
        private SaleNewsAdapter saleNewsAdapter;

        public SaleNewsListViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rvSaleNews.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        }

        public void setData(int position) {
            if (mSaleNewsList != null && !mSaleNewsList.isEmpty()) {
                saleNewsAdapter = new SaleNewsAdapter(mContext, mSaleNewsList, mHomeListener);
                rvSaleNews.setAdapter(saleNewsAdapter);
                rvSaleNews.setVisibility(View.VISIBLE);
            } else rvSaleNews.setVisibility(View.GONE);
        }
    }

    public class HomeNewsViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_sapo)
        TextView tvSapo;
        @BindView(R.id.item_view)
        CardView itemView;

        public HomeNewsViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            News news = mHomes.get(position);
            if (news.getImage() != null) Utils.loadImage(mContext, ivImage, news.getImage());
            String title = news.getTitle();
            if (title != null && !title.isEmpty()) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            } else tvTitle.setVisibility(View.GONE);
            String sapo = news.getDesctiption();
            if (sapo != null && !sapo.isEmpty()) {
                tvSapo.setText(sapo);
                tvSapo.setVisibility(View.VISIBLE);
            } else tvSapo.setVisibility(View.GONE);
            ((RecyclerView.LayoutParams) itemView.getLayoutParams()).topMargin = position == 0 ? mContext.getResources().getDimensionPixelSize(R.dimen.size_15) : mContext.getResources().getDimensionPixelSize(R.dimen.size_5);
            ((RecyclerView.LayoutParams) itemView.getLayoutParams()).bottomMargin = position == (mSaleNewsList.size() - 1) ? mContext.getResources().getDimensionPixelSize(R.dimen.size_15) : mContext.getResources().getDimensionPixelSize(R.dimen.size_5);
            itemView.setOnClickListener(v -> mHomeListener.goHomeNewsDetail(news));
        }
    }
}
