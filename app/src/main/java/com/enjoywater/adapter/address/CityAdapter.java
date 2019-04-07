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
import com.enjoywater.model.Location.City;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CustomViewholder> {
    private Context mContext;
    private ArrayList<City> mCities;
    private LayoutInflater mInflater;
    private AddressListener mAddressListener;
    private City mCitySelected;

    public CityAdapter(Context mContext, ArrayList<City> mCities, City citySelected, AddressListener mAddressListener) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.mCitySelected = citySelected;
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
        return mCities.size();
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
            City city = mCities.get(position);
            tvName.setText(city.getName() != null ? city.getName() : "");
            tvName.setTextColor(city.isAvailable() ? mContext.getResources().getColor(R.color.black_0) : mContext.getResources().getColor(R.color.black_9));
            if (mCitySelected != null && city.getId().equals(mCitySelected.getId()))
                ivSelected.setVisibility(View.VISIBLE);
            else ivSelected.setVisibility(View.GONE);
            itemView.setOnClickListener(v -> {
                if (city.isAvailable()) mAddressListener.selectCity(city);
                else Toast.makeText(mContext, "Xin lỗi, chúng tôi hiện chưa thể phục vụ quý khách hàng ở khu vực này.", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
