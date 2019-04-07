package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.adapter.address.AddressAdapter;
import com.enjoywater.listener.AddressListener;
import com.enjoywater.model.Address;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.dialog.DialogOrderAddress;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAddressActivity extends AppCompatActivity {
    public static final int RESULT_CODE_CHANGE_ADDRESS = 124;
    private static final String TAG = "OrderAddressActivity";
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_address_list)
    RecyclerView rvAddressList;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.layout_error)
    RelativeLayout layoutError;
    @BindView(R.id.iv_add_address)
    ImageView ivAddAddress;
    @BindView(R.id.text_add_address)
    TextView textAddAddress;
    @BindView(R.id.btn_add_address)
    ConstraintLayout btnAddAddress;

    private MainService mainService;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private ArrayList<Address> mAddressList = new ArrayList<>();
    private AddressAdapter mAddressAdapter;
    private Address mSelectedAddress;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(this);
        mToken = Utils.getToken(this);
        mSelectedAddress = getIntent().getParcelableExtra(Constants.Key.ADDRESS);
        initUI();
    }

    private void initUI() {
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        btnBack.setOnClickListener(view -> onBackPressed());
        rvAddressList.setLayoutManager(new LinearLayoutManager(OrderAddressActivity.this, RecyclerView.VERTICAL, false));
        rvAddressList.setNestedScrollingEnabled(false);
        btnAddAddress.setOnClickListener(view -> addNewAddress());
        btnLogin.setOnClickListener(v -> {
            if (!isLoading) {
                startActivityForResult(new Intent(OrderAddressActivity.this, LoginActivity.class), MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN);
                overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
            }
        });
        if (mUser == null || mToken.isEmpty()) {
            showError(Constants.DataNotify.NOT_LOGIN_YET);
        } else setData();
    }

    private AddressListener mAddressListener = new AddressListener() {
        @Override
        public void selectAddress(Address address) {
            mSelectedAddress = address;
            onBackPressed();
        }

        @Override
        public void selectCity(City city) {

        }

        @Override
        public void selectDistrict(District district) {

        }

        @Override
        public void selectWard(Ward ward) {

        }

        @Override
        public void deteleAddress(Address address) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderAddressActivity.this);
            builder.setMessage("Bạn muốn xóa địa chỉ này?")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý", (dialog, id) -> {
                        removeAddress(address);
                    })
                    .setNegativeButton("Hủy", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private void setData() {
        showContent();
        mAddressList = mUser.getUserInfo().getOtherAddress();
        if (mAddressList != null && !mAddressList.isEmpty()) {
            if (mSelectedAddress != null) {
                for (int i = 0, z = mAddressList.size(); i < z; i++) {
                    mAddressList.get(i).setSelected(mAddressList.get(i).getKey() == mSelectedAddress.getKey());
                }
            } else {
                mAddressList.get(0).setSelected(true);
                mSelectedAddress = mAddressList.get(0);
            }
            if (mAddressAdapter == null) {
                mAddressAdapter = new AddressAdapter(OrderAddressActivity.this, mAddressList, mAddressListener);
                rvAddressList.setAdapter(mAddressAdapter);
            } else mAddressAdapter.notifyDataSetChanged();
            rvAddressList.setVisibility(View.VISIBLE);
        } else rvAddressList.setVisibility(View.GONE);
    }

    private void addNewAddress() {
        new DialogOrderAddress(OrderAddressActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mSelectedAddress = (Address) msg.obj;
                    mUser.getUserInfo().getOtherAddress().add(0, mSelectedAddress);
                    Utils.saveUser(OrderAddressActivity.this, mUser);
                    setData();
                }
            }
        });
    }

    private void removeAddress(Address address) {
        isLoading = true;
        showLoading(false);
        Call<BaseResponse> deleteAddress = mainService.removeAddress(mToken, String.valueOf(address.getKey()));
        deleteAddress.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                showContent();
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.isSuccess()) {
                        for (int i = 0; i < mAddressList.size(); i++) {
                            if (mAddressList.get(i).getKey() == address.getKey()) {
                                mAddressList.remove(i);
                                mAddressAdapter.notifyItemRemoved(i);
                                break;
                            }
                        }
                        if (mSelectedAddress.getKey() == address.getKey()) {
                            if (mAddressList != null && !mAddressList.isEmpty()) {
                                mSelectedAddress = mAddressList.get(0);
                                mAddressList.get(0).setSelected(true);
                                mAddressAdapter.notifyItemChanged(0);
                            } else mSelectedAddress = null;
                        }
                        mUser.getUserInfo().setOtherAddress(mAddressList);
                        Utils.saveUser(OrderAddressActivity.this, mUser);
                        Toast.makeText(OrderAddressActivity.this, "Xóa địa chỉ thành công.", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR;
                        if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty())
                            message = baseResponse.getError().getMessage();
                        Toast.makeText(OrderAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(OrderAddressActivity.this, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                showContent();
                Toast.makeText(OrderAddressActivity.this, Constants.DataNotify.DATA_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            rvAddressList.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            rvAddressList.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        rvAddressList.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        appbar.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        rvAddressList.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        if (error.equals(Constants.DataNotify.NOT_LOGIN_YET))
            btnLogin.setVisibility(View.VISIBLE);
        else btnLogin.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN && resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS) {
            mUser = Utils.getUser(OrderAddressActivity.this);
            mToken = Utils.getToken(OrderAddressActivity.this);
            setData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.PROFILE_UPDATED: {
                mUser = (User) event.getObject();
                setData();
                break;
            }
            default: {
                //Log.e(TAG, "onMessageEvent " + gson.toJson(event));
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra(Constants.Key.ADDRESS, mSelectedAddress);
        setResult(RESULT_CODE_CHANGE_ADDRESS, intent);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }
}
