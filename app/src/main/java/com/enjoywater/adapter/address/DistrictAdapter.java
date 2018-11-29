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

import com.enjoywater.R;
import com.enjoywater.listener.AddressListener;
import com.enjoywater.model.Location.District;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<District> mDistricts;
    private LayoutInflater mInflater;
    private AddressListener mAddressListener;
    private District mDistrictSelected;

    public DistrictAdapter(Context mContext, ArrayList<District> districts, District districtSelected, AddressListener mAddressListener) {
        this.mContext = mContext;
        this.mDistricts = districts;
        this.mDistrictSelected = districtSelected;
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
        return mDistricts.size();
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
            District district = mDistricts.get(position);
            tvName.setText(district.getName() != null ? district.getName() : "");
            if (mDistrictSelected != null && district.getId() == mDistrictSelected.getId())
                ivSelected.setVisibility(View.VISIBLE);
            else ivSelected.setVisibility(View.GONE);
            itemView.setOnClickListener(v -> mAddressListener.selectDistrict(district));
        }
    }
}
