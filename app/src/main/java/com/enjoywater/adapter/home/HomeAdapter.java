package com.enjoywater.adapter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoywater.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TOPLIST = 0;
    private static final int TYPE_NEWS = 1;
    private static final int TYPE_LOADMORE = 2;
    private Context mContext;
    private ArrayList<Object> mObjects;
    private LayoutInflater mInflater;

    public HomeAdapter(Context mContext, ArrayList<Object> mObjects) {
        this.mContext = mContext;
        this.mObjects = mObjects;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_TOPLIST;
        else return TYPE_NEWS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_TOPLIST: {
                View itemView = mInflater.inflate(R.layout.item_home_toplist, viewGroup, false);
                return new TopListViewholder(itemView);
            }
            case TYPE_NEWS: {
                View itemView = mInflater.inflate(R.layout.item_home_news, viewGroup, false);
                return new NewsViewholder(itemView);
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopListViewholder) ((TopListViewholder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class TopListViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_toplist)
        RecyclerView rvToplist;

        public TopListViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rvToplist.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        }

        public void setData(int position) {
            rvToplist.setAdapter(new TopListAdapter(mContext, new ArrayList<>()));
        }
    }

    public class NewsViewholder extends RecyclerView.ViewHolder {
        public NewsViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
