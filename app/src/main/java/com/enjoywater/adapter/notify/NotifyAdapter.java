package com.enjoywater.adapter.notify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Barrier;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.listener.NotifyListener;
import com.enjoywater.model.Notify;
import com.enjoywater.utils.Constants;
import com.enjoywater.view.LoadmoreViewHolder;
import com.enjoywater.view.TvSegoeuiSemiBold;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LOADMORE = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;
    private ArrayList<Notify> mNotifies;
    private LayoutInflater mInflater;
    private NotifyListener mNotifyListener;

    public NotifyAdapter(Context context, ArrayList<Notify> notifies, NotifyListener notifyListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mNotifies = notifies;
        this.mNotifyListener = notifyListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mNotifies.get(position).isLoadmore()) return TYPE_LOADMORE;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADMORE:
                return new LoadmoreViewHolder(mInflater.inflate(R.layout.item_loadmore, parent, false));
            default:
                return new CustomViewHolder(mInflater.inflate(R.layout.item_notify, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomViewHolder) ((CustomViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return mNotifies.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_notif)
        ImageView ivNotif;
        @BindView(R.id.iv_notif_unread)
        ImageView ivNotifUnread;
        @BindView(R.id.tv_title)
        TvSegoeuiSemiBold tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.barrier_under_content)
        Barrier barrierUnderContent;
        @BindView(R.id.v_underline)
        View vUnderline;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(int position) {
            Notify notify = mNotifies.get(position);
            if (notify != null) {
                tvTitle.setText(notify.getTitle() != null && !notify.getTitle().isEmpty() ? notify.getTitle() : "Thông báo");
                tvContent.setText(notify.getBody() != null ? notify.getBody() : "");
                ivNotifUnread.setVisibility(notify.getStatus().equals(Constants.Value.UNREAD) ? View.VISIBLE : View.GONE);
                //vUnderline.setVisibility(position < mNotifies.size() - 1 ? View.VISIBLE : View.INVISIBLE);
                itemView.setOnClickListener(v -> mNotifyListener.goNotifyDetails(notify));
            }
        }
    }
}
