package com.enjoywater.adapter.address;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.listener.AddressListener;
import com.enjoywater.model.Location.Ward;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WardAdapter extends RecyclerView.Adapter<WardAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<Ward> mWards;
    private LayoutInflater mInflater;
    private AddressListener mAddressListener;
    private Ward mWardSelected;

    public WardAdapter(Context mContext, ArrayList<Ward> wards, Ward wardSelected, AddressListener mAddressListener) {
        this.mContext = mContext;
        this.mWards = wards;
        this.mWardSelected = wardSelected;
        this.mAddressListener = mAddressListener;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_city, viewGroup, false);
        return new CustomViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder customViewholder, int position) {
        customViewholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mWards.size();
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.item_view)
        RelativeLayout itemView;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            Ward ward = mWards.get(position);
            tvName.setText(ward.getName() != null ? ward.getName() : "");
            if (mWardSelected != null && ward.getId().equals(mWardSelected.getId()))
                ivSelected.setVisibility(View.VISIBLE);
            else ivSelected.setVisibility(View.GONE);
            itemView.setOnClickListener(v -> {
                if (ward.isAvailable()) mAddressListener.selectWard(ward);
                else Toast.makeText(mContext, "Xin lỗi, chúng tôi hiện chưa thể phục vụ quý khách hàng ở khu vực này.", Toast.LENGTH_SHORT).show();

            });
        }
    }
}
