package com.enjoywater.adapter.address;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.listener.AddressListener;
import com.enjoywater.model.Address;
import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Address> mAddressList;
    private LayoutInflater mInflater;
    private ArrayList<City> mCities;
    private AddressListener mAddressListener;

    public AddressAdapter(Context context, ArrayList<Address> addressList, AddressListener addressListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mCities = MyApplication.getInstance().getCities();
        this.mAddressList = addressList;
        this.mAddressListener = addressListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomViewHolder) ((CustomViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_location)
        ImageView ivLocation;
        @BindView(R.id.tv_address_line)
        TextView tvAddressLine;
        @BindView(R.id.iv_info)
        ImageView ivInfo;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.v_underline)
        View vUnderline;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(int position) {
            Address address = mAddressList.get(position);
            if (address != null) {
                String addressLine = address.getAddress();
                if (address.getCityId() != null && !address.getCityId().isEmpty()) {
                    for (City city : mCities) {
                        if (city.getId() != null && city.getId().equals(address.getCityId()) && city.getName() != null && !city.getName().isEmpty()) {
                            ArrayList<District> districts = city.getDistricts();
                            if (districts != null && !districts.isEmpty() && address.getDistrictId() != null && !address.getDistrictId().isEmpty()) {
                                for (District district : districts) {
                                    if (district.getId() != null && district.getId().equals(address.getDistrictId()) && district.getName() != null && !district.getName().isEmpty()) {
                                        ArrayList<Ward> wards = district.getWards();
                                        if (wards != null && !wards.isEmpty() && address.getWardId() != null && !address.getWardId().isEmpty()) {
                                            for (Ward ward : wards) {
                                                if (ward.getId() != null && !ward.getId().equals(address.getWardId()) && ward.getName() != null && !ward.getName().isEmpty()) {
                                                    if (ward.getType() != null && !ward.getType().isEmpty())
                                                        addressLine += (", " + ward.getType() + " " + ward.getName());
                                                    else addressLine += (", " + ward.getName());
                                                    break;
                                                }
                                            }
                                        }
                                        if (district.getType() != null && !district.getType().isEmpty())
                                            addressLine += (", " + district.getType() + " " + district.getName());
                                        else addressLine += (", " + district.getName());
                                        break;
                                    }
                                }
                            }
                            if (city.getType() != null && !city.getType().isEmpty())
                                addressLine += (", " + city.getType() + " " + city.getName());
                            else addressLine += (", " + city.getName());
                            break;
                        }
                    }
                }
                tvAddressLine.setText(addressLine);
                tvName.setText(address.getName());
                tvPhone.setText(address.getPhone());
                ivSelected.setImageResource(address.isSelected() ? R.drawable.ic_tick_green : R.drawable.ic_tick_grey);
                ivDelete.setOnClickListener(view -> mAddressListener.deteleAddress(address));
                itemView.setOnClickListener(view -> mAddressListener.selectAddress(address));
            }
        }
    }
}
