package com.enjoywater.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.adapter.address.CityAdapter;
import com.enjoywater.adapter.address.DistrictAdapter;
import com.enjoywater.adapter.address.WardAdapter;
import com.enjoywater.listener.AddressListener;
import com.enjoywater.model.Address;
import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogOrderAddress {
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.btn_select_city)
    LinearLayout btnSelectCity;
    @BindView(R.id.rv_city)
    RecyclerView rvCity;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.btn_select_district)
    LinearLayout btnSelectDistrict;
    @BindView(R.id.rv_district)
    RecyclerView rvDistrict;
    @BindView(R.id.tv_ward)
    TextView tvWard;
    @BindView(R.id.btn_select_ward)
    LinearLayout btnSelectWard;
    @BindView(R.id.rv_ward)
    RecyclerView rvWard;
    @BindView(R.id.edt_addressDetail)
    EditText edtAddressDetail;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    private Context mContext;
    private MainService mainService;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private User mUser;
    private String mToken;
    private City mCitySelected;
    private District mDistrictSelected;
    private Ward mWardSelected;
    private Address mAddress;
    private ArrayList<City> mCities;
    private AddressListener mAddressListener;
    private boolean isLoading;

    public DialogOrderAddress(Context context, Handler callbackHandler) {
        mContext = context;
        mainService = MyApplication.getInstance().getMainService();
        mCallBackHandler = callbackHandler;
        mUser = Utils.getUser(mContext);
        mToken = Utils.getToken(mContext);
        mCities = MyApplication.getInstance().getCities();
        initUI();
    }

    public DialogOrderAddress(Context context, Handler callbackHandler, Address address) {
        mContext = context;
        mCallBackHandler = callbackHandler;
        mAddress = address;
        mUser = Utils.getUser(mContext);
        mCities = MyApplication.getInstance().getCities();
        initUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initUI() {
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_order_address);
        ButterKnife.bind(DialogOrderAddress.this, mDialog);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up_over_screen);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layoutContent.startAnimation(animation);
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        rvCity.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvDistrict.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvWard.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        if (mUser != null) {
            if (mUser.getUserInfo().getName() != null && !mUser.getUserInfo().getName().isEmpty())
                edtName.setText(mUser.getUserInfo().getName());
            if (mUser.getUserInfo().getPhone() != null && !mUser.getUserInfo().getPhone().isEmpty())
                edtPhone.setText(mUser.getUserInfo().getPhone());
        }
        btnSelectCity.setOnClickListener(v -> {
            showListCity();
        });
        btnSelectDistrict.setOnClickListener(v -> {
            if (!tvCity.getText().toString().isEmpty()) showListDistrict();
        });
        btnSelectWard.setOnClickListener(v -> {
            if (!tvDistrict.getText().toString().isEmpty()) showListWard();
        });
        mAddressListener = new AddressListener() {
            @Override
            public void selectCity(City city) {
                if (mCitySelected == null || !mCitySelected.getId().equals(city.getId())) {
                    mDistrictSelected = null;
                    tvDistrict.setText("");
                    mWardSelected = null;
                    tvWard.setText("");
                }
                mCitySelected = city;
                tvCity.setText(mCitySelected.getName() != null ? mCitySelected.getName() : "");
                rvCity.setVisibility(View.GONE);
            }

            @Override
            public void selectDistrict(District district) {
                if (mDistrictSelected == null || !mDistrictSelected.getId().equals(district.getId())) {
                    mWardSelected = null;
                    tvWard.setText("");
                }
                mDistrictSelected = district;
                tvDistrict.setText(mDistrictSelected.getName() != null ? mDistrictSelected.getName() : "");
                rvDistrict.setVisibility(View.GONE);
            }

            @Override
            public void selectWard(Ward ward) {
                mWardSelected = ward;
                tvWard.setText(mWardSelected.getName() != null ? mWardSelected.getName() : "");
                rvWard.setVisibility(View.GONE);
            }
        };
        btnClose.setOnClickListener(v -> {
            Animation endAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
            endAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Message message = new Message();
                    message.what = Constants.Value.ACTION_CLOSE;
                    mCallBackHandler.sendMessage(message);
                    mDialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            layoutContent.startAnimation(endAnimation);
        });
        btnSave.setOnClickListener(v -> {
            if (!isLoading) validateAddress();
        });
        if (mCities == null || mCities.isEmpty()) {
            //load data cities from api
        }
        mDialog.show();
    }

    private void validateAddress() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String addressDetail = edtAddressDetail.getText().toString();
        if (name.isEmpty())
            Toast.makeText(mContext, "Vui lòng nhập Họ và Tên", Toast.LENGTH_SHORT).show();
        else if (!Utils.isValidPhone(phone))
            Toast.makeText(mContext, "Vui lòng nhập SĐT hợp lệ", Toast.LENGTH_SHORT).show();
        else if (mCitySelected == null)
            Toast.makeText(mContext, "Vui lòng chọn Tỉnh/Thành phố", Toast.LENGTH_SHORT).show();
        else if (mDistrictSelected == null)
            Toast.makeText(mContext, "Vui lòng chọn Quận/Huyện", Toast.LENGTH_SHORT).show();
            //else if (mWardSelected == null) Toast.makeText(mContext,"Vui lòng chọn Phường/Xã", Toast.LENGTH_SHORT).show();
        else if (addressDetail.isEmpty())
            Toast.makeText(mContext, "Vui lòng nhập địa chỉ cụ thể của bạn", Toast.LENGTH_SHORT).show();
        else {
            //mAddress = new Address(name, phone, mCitySelected.getId(), mDistrictSelected.getId(), mWardSelected.getId(), addressDetail);
            mAddress = new Address(name, phone, mCitySelected.getId(), mDistrictSelected.getId(), addressDetail);
            addNewAddress(mAddress);
        }
    }

    private void addNewAddress(Address address) {
        isLoading = true;
        layoutContent.setVisibility(View.GONE);
        progressLoading.setVisibility(View.VISIBLE);
        //Call<BaseResponse> addNewAddress = mainService.addNewAddress(mToken, address.getName(), address.getPhone(), address.getCityId(), address.getDistrictId(), address.getWardId(), address.getAddress(), 1);
        Call<BaseResponse> addNewAddress = mainService.addNewAddress(mToken, address.getName(), address.getPhone(), address.getCityId(), address.getDistrictId(), address.getAddress(), 1);
        addNewAddress.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess()) {
                        Toast.makeText(mContext, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = Constants.Value.ACTION_SUCCESS;
                        message.obj = address;
                        mCallBackHandler.sendMessage(message);
                        mDialog.dismiss();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty())
                            message = baseResponse.getError().getMessage();
                        showError(message);
                    }
                } else showError(Constants.DataNotify.DATA_ERROR);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                showError(Constants.DataNotify.DATA_ERROR);
            }
        });
    }

    private void showListCity() {
        if (rvCity.getVisibility() == View.GONE) {
            if (mCities != null && !mCities.isEmpty()) {
                rvCity.setAdapter(new CityAdapter(mContext, mCities, mCitySelected, mAddressListener));
                rvCity.setVisibility(View.VISIBLE);
                rvDistrict.setVisibility(View.GONE);
                rvWard.setVisibility(View.GONE);
                if (mCitySelected != null) {
                    for (int i = 0, z = mCities.size(); i < z; i++) {
                        if (mCities.get(i).getId() == mCitySelected.getId()) {
                            rvCity.scrollToPosition(i);
                            break;
                        }
                    }
                }
            }
        } else rvCity.setVisibility(View.GONE);
    }

    private void showListDistrict() {
        if (rvDistrict.getVisibility() == View.GONE) {
            if (mCitySelected != null && mCitySelected.getDistricts() != null && !mCitySelected.getDistricts().isEmpty()) {
                ArrayList<District> districts = mCitySelected.getDistricts();
                rvDistrict.setAdapter(new DistrictAdapter(mContext, districts, mDistrictSelected, mAddressListener));
                rvCity.setVisibility(View.GONE);
                rvDistrict.setVisibility(View.VISIBLE);
                rvWard.setVisibility(View.GONE);
                if (mDistrictSelected != null) {
                    for (int i = 0, z = districts.size(); i < z; i++) {
                        if (districts.get(i).getId() == mDistrictSelected.getId()) {
                            rvDistrict.scrollToPosition(i);
                            break;
                        }
                    }
                }
            }
        } else rvDistrict.setVisibility(View.GONE);
    }

    private void showListWard() {
        if (rvWard.getVisibility() == View.GONE) {
            if (mDistrictSelected != null && mDistrictSelected.getWards() != null && !mDistrictSelected.getWards().isEmpty()) {
                ArrayList<Ward> wards = mDistrictSelected.getWards();
                rvWard.setAdapter(new WardAdapter(mContext, wards, mWardSelected, mAddressListener));
                rvCity.setVisibility(View.GONE);
                rvDistrict.setVisibility(View.GONE);
                rvWard.setVisibility(View.VISIBLE);
                if (mWardSelected != null) {
                    for (int i = 0, z = wards.size(); i < z; i++) {
                        if (wards.get(i).getId() == mWardSelected.getId()) {
                            rvWard.scrollToPosition(i);
                            break;
                        }
                    }
                }
            }
        } else rvWard.setVisibility(View.GONE);
    }

    private void showError(String message) {
        progressLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
