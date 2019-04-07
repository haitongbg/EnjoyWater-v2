package com.enjoywater.activiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.fragment.HomeFragment;
import com.enjoywater.fragment.NotifyFragment;
import com.enjoywater.fragment.OrdersFragment;
import com.enjoywater.fragment.PersonalFragment;
import com.enjoywater.fragment.ProductFragment;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.News;
import com.enjoywater.model.Notify;
import com.enjoywater.model.Order;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_LOGIN_FROM_MAIN = 111;
    public static final int REQUEST_CODE_CALL_PHONE_PERMISSION = 212;
    private static final String TAG = "MainActivity";
    int[] tabDrawableOn = {R.drawable.ic_tab_home_active, R.drawable.ic_tab_product_active, R.drawable.ic_tab_orders_active, R.drawable.ic_tab_notif_active, R.drawable.ic_tab_personal_active};
    int[] tabDrawableOff = {R.drawable.ic_tab_home, R.drawable.ic_tab_product, R.drawable.ic_tab_orders, R.drawable.ic_tab_notif, R.drawable.ic_tab_personal};
    HomeFragment homeFragment;
    ProductFragment productFragment;
    OrdersFragment ordersFragment;
    NotifyFragment notifyFragment;
    PersonalFragment personalFragment;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_tab_home)
    ImageView ivTabHome;
    @BindView(R.id.tv_tab_home)
    TextView tvTabHome;
    @BindView(R.id.underline_tab_home)
    View underlineTabHome;
    @BindView(R.id.layout_tab_home)
    RelativeLayout layoutTabHome;
    @BindView(R.id.iv_tab_product)
    ImageView ivTabProduct;
    @BindView(R.id.tv_tab_product)
    TextView tvTabProduct;
    @BindView(R.id.underline_tab_product)
    View underlineTabProduct;
    @BindView(R.id.layout_tab_product)
    RelativeLayout layoutTabProduct;
    @BindView(R.id.iv_tab_orders)
    ImageView ivTabOrders;
    @BindView(R.id.tv_tab_orders)
    TextView tvTabOrders;
    @BindView(R.id.underline_tab_orders)
    View underlineTabOrders;
    @BindView(R.id.layout_tab_orders)
    RelativeLayout layoutTabOrders;
    @BindView(R.id.iv_tab_notif)
    ImageView ivTabNotif;
    @BindView(R.id.tv_tab_notif)
    TextView tvTabNotif;
    @BindView(R.id.underline_tab_notif)
    View underlineTabNotif;
    @BindView(R.id.layout_tab_notif)
    RelativeLayout layoutTabNotif;
    @BindView(R.id.iv_tab_personal)
    ImageView ivTabPersonal;
    @BindView(R.id.tv_tab_personal)
    TextView tvTabPersonal;
    @BindView(R.id.underline_tab_personal)
    View underlineTabPersonal;
    @BindView(R.id.layout_tab_personal)
    RelativeLayout layoutTabPersonal;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int mTabSelected = 0;
    private MainService mainService;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        mTabSelected = getIntent().getIntExtra("tab_selected", 0);
        initUI();
    }

    private void initUI() {
        layoutTabHome.setOnClickListener(view -> viewPager.setCurrentItem(0));
        layoutTabProduct.setOnClickListener(view -> viewPager.setCurrentItem(1));
        layoutTabOrders.setOnClickListener(view -> viewPager.setCurrentItem(2));
        layoutTabNotif.setOnClickListener(view -> viewPager.setCurrentItem(3));
        layoutTabPersonal.setOnClickListener(view -> viewPager.setCurrentItem(4));
        initFragment();
    }

    public void initFragment() {
        homeFragment = new HomeFragment();
        productFragment = new ProductFragment();
        ordersFragment = new OrdersFragment();
        notifyFragment = new NotifyFragment();
        personalFragment = new PersonalFragment();
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(mTabSelected);
    }

    public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return homeFragment;
                case 1:
                    return productFragment;
                case 2:
                    return ordersFragment;
                case 3:
                    return notifyFragment;
                case 4:
                    return personalFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mTabSelected = position;
            changeTab(position);
            //hideTabNotif(mTabSelected);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public void changeTab(int position) {
        if (position == 0) {
            ivTabHome.setImageResource(tabDrawableOn[0]);
            tvTabHome.setTextColor(getResources().getColor(R.color.colorPrimary));
            ivTabProduct.setImageResource(tabDrawableOff[1]);
            tvTabProduct.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabOrders.setImageResource(tabDrawableOff[2]);
            tvTabOrders.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabNotif.setImageResource(tabDrawableOff[3]);
            tvTabNotif.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabPersonal.setImageResource(tabDrawableOff[4]);
            tvTabPersonal.setTextColor(getResources().getColor(R.color.black_b1));
            layoutTabHome.setBackgroundColor(getResources().getColor(R.color.black_f1));
            layoutTabProduct.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabOrders.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabNotif.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabPersonal.setBackgroundColor(getResources().getColor(R.color.white));
            underlineTabHome.setVisibility(View.VISIBLE);
            underlineTabProduct.setVisibility(View.GONE);
            underlineTabOrders.setVisibility(View.GONE);
            underlineTabNotif.setVisibility(View.GONE);
            underlineTabPersonal.setVisibility(View.GONE);
        }
        if (position == 1) {
            ivTabHome.setImageResource(tabDrawableOff[0]);
            tvTabHome.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabProduct.setImageResource(tabDrawableOn[1]);
            tvTabProduct.setTextColor(getResources().getColor(R.color.colorPrimary));
            ivTabOrders.setImageResource(tabDrawableOff[2]);
            tvTabOrders.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabNotif.setImageResource(tabDrawableOff[3]);
            tvTabNotif.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabPersonal.setImageResource(tabDrawableOff[4]);
            tvTabPersonal.setTextColor(getResources().getColor(R.color.black_b1));
            layoutTabHome.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabProduct.setBackgroundColor(getResources().getColor(R.color.black_f1));
            layoutTabOrders.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabNotif.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabPersonal.setBackgroundColor(getResources().getColor(R.color.white));
            underlineTabHome.setVisibility(View.GONE);
            underlineTabProduct.setVisibility(View.VISIBLE);
            underlineTabOrders.setVisibility(View.GONE);
            underlineTabNotif.setVisibility(View.GONE);
            underlineTabPersonal.setVisibility(View.GONE);
        }
        if (position == 2) {
            ivTabHome.setImageResource(tabDrawableOff[0]);
            tvTabHome.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabProduct.setImageResource(tabDrawableOff[1]);
            tvTabProduct.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabOrders.setImageResource(tabDrawableOn[2]);
            tvTabOrders.setTextColor(getResources().getColor(R.color.colorPrimary));
            ivTabNotif.setImageResource(tabDrawableOff[3]);
            tvTabNotif.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabPersonal.setImageResource(tabDrawableOff[4]);
            tvTabPersonal.setTextColor(getResources().getColor(R.color.black_b1));
            layoutTabHome.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabProduct.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabOrders.setBackgroundColor(getResources().getColor(R.color.black_f1));
            layoutTabNotif.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabPersonal.setBackgroundColor(getResources().getColor(R.color.white));
            underlineTabHome.setVisibility(View.GONE);
            underlineTabProduct.setVisibility(View.GONE);
            underlineTabOrders.setVisibility(View.VISIBLE);
            underlineTabNotif.setVisibility(View.GONE);
            underlineTabPersonal.setVisibility(View.GONE);
        }
        if (position == 3) {
            ivTabHome.setImageResource(tabDrawableOff[0]);
            tvTabHome.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabProduct.setImageResource(tabDrawableOff[1]);
            tvTabProduct.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabOrders.setImageResource(tabDrawableOff[2]);
            tvTabOrders.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabNotif.setImageResource(tabDrawableOn[3]);
            tvTabNotif.setTextColor(getResources().getColor(R.color.colorPrimary));
            ivTabPersonal.setImageResource(tabDrawableOff[4]);
            tvTabPersonal.setTextColor(getResources().getColor(R.color.black_b1));
            layoutTabHome.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabProduct.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabOrders.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabNotif.setBackgroundColor(getResources().getColor(R.color.black_f1));
            layoutTabPersonal.setBackgroundColor(getResources().getColor(R.color.white));
            underlineTabHome.setVisibility(View.GONE);
            underlineTabProduct.setVisibility(View.GONE);
            underlineTabOrders.setVisibility(View.GONE);
            underlineTabNotif.setVisibility(View.VISIBLE);
            underlineTabPersonal.setVisibility(View.GONE);
        }
        if (position == 4) {
            ivTabHome.setImageResource(tabDrawableOff[0]);
            tvTabHome.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabProduct.setImageResource(tabDrawableOff[1]);
            tvTabProduct.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabOrders.setImageResource(tabDrawableOff[2]);
            tvTabOrders.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabNotif.setImageResource(tabDrawableOff[3]);
            tvTabNotif.setTextColor(getResources().getColor(R.color.black_b1));
            ivTabPersonal.setImageResource(tabDrawableOn[4]);
            tvTabPersonal.setTextColor(getResources().getColor(R.color.colorPrimary));
            layoutTabHome.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabProduct.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabOrders.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabNotif.setBackgroundColor(getResources().getColor(R.color.white));
            layoutTabPersonal.setBackgroundColor(getResources().getColor(R.color.black_f1));
            underlineTabHome.setVisibility(View.GONE);
            underlineTabProduct.setVisibility(View.GONE);
            underlineTabOrders.setVisibility(View.GONE);
            underlineTabNotif.setVisibility(View.GONE);
            underlineTabPersonal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeFragment.onActivityResult(requestCode, resultCode, data);
        productFragment.onActivityResult(requestCode, resultCode, data);
        ordersFragment.onActivityResult(requestCode, resultCode, data);
        notifyFragment.onActivityResult(requestCode, resultCode, data);
        personalFragment.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CALL_PHONE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.Value.HOTLINE));
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.BONUS_UPDATED: {
                getDataUser();
                break;
            }
            case Constants.Key.ORDER_UPDATED: {
                int orderId = Integer.parseInt(((Notify) event.getObject()).getContent());
                if (orderId != 0) {
                    getOrderDetails(orderId);
                }
                break;
            }
            case Constants.Key.NEWS_UPDATED: {
                int newsId = Integer.parseInt(((Notify) event.getObject()).getContent());
                if (newsId != 0) {
                    getNewsDetails(newsId);
                }
                break;
            }
            default:{
                Log.e(TAG, "onMessageEvent " + (new Gson()).toJson(event));
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

    private void getDataUser() {
        String token = Utils.getToken(this);
        User user = Utils.getUser(this);
        if (user != null && !token.isEmpty()) {
            Call<BaseResponse> getUserInfo = mainService.getUserInfo(token);
            getUserInfo.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess() && baseResponse.getData() != null && baseResponse.getData().isJsonObject()) {
                        User.UserInfo userInfo = gson.fromJson(baseResponse.getData().getAsJsonObject(), User.UserInfo.class);
                        if (userInfo != null) {
                            user.setUserInfo(userInfo);
                            Utils.saveUser(MainActivity.this, user);
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getOrderDetails(int orderId) {
        String token = Utils.getToken(this);
        Call<BaseResponse> getOrderDetails = mainService.getOrderDetails(token, orderId);
        getOrderDetails.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse getOrderDetailsResponse = response.body();
                if (getOrderDetailsResponse != null && getOrderDetailsResponse.isSuccess() && getOrderDetailsResponse.getData() != null && getOrderDetailsResponse.getData().isJsonObject()) {
                    Order order = gson.fromJson(getOrderDetailsResponse.getData(), Order.class);
                    EventBus.getDefault().post(new EventBusMessage(Constants.Key.INSERT_ORDER, order));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getNewsDetails(int newsId) {
        String token = Utils.getToken(this);
        Call<BaseResponse> getNewsDetails = mainService.getNewsDetails(token, newsId);
        getNewsDetails.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.isSuccess() && baseResponse.getData() != null && baseResponse.getData().isJsonObject()) {
                    News news = gson.fromJson(baseResponse.getData().getAsJsonObject(), News.class);
                    if (news != null && news.getContent() != null && !news.getContent().isEmpty()) {
                        EventBus.getDefault().post(new EventBusMessage(Constants.Key.INSERT_NEWS, news));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
