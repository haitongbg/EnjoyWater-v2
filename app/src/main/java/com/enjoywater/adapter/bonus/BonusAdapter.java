package com.enjoywater.adapter.bonus;

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
import com.enjoywater.model.Bonus;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.LoadmoreViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BonusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LOADMORE = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;
    private ArrayList<Bonus> mBonusList;
    private LayoutInflater mInflater;
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");

    public BonusAdapter(Context context, ArrayList<Bonus> bonusList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mBonusList = bonusList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBonusList.get(position).isLoadmore()) return TYPE_LOADMORE;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADMORE:
                return new LoadmoreViewHolder(mInflater.inflate(R.layout.item_loadmore, parent, false));
            default:
                return new CustomViewHolder(mInflater.inflate(R.layout.item_bonus, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomViewHolder) ((CustomViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return mBonusList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_bonus)
        ImageView ivBonus;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.v_underline)
        View vUnderline;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(int position) {
            Bonus bonus = mBonusList.get(position);
            if (bonus != null) {
                String title = "";
                switch (bonus.getType()) {
                    case Constants.Value.REF: {
                        title = "Thưởng giới thiệu: " + bonus.getUserInfo().getName();
                        break;
                    }
                    case Constants.Value.ORDER: {
                        title = "Thưởng từ đơn hàng của " + bonus.getUserInfo().getName();
                        break;
                    }
                    default: {
                        title = "Thưởng từ hệ thống";
                        break;
                    }
                }
                tvTitle.setText(title);
                tvTime.setText(Utils.convertDateTimeToDateTime(bonus.getCreatedAt(), "GMT", 5, 8));
                tvContent.setText("+" + formatVND.format(bonus.getCoin()));
                vUnderline.setVisibility(position < mBonusList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }
}
