package com.enjoywater.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enjoywater.R;
import com.enjoywater.activiy.BonusHistoryActivity;
import com.enjoywater.activiy.EventActivity;
import com.enjoywater.activiy.LoginActivity;
import com.enjoywater.activiy.MainActivity;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.activiy.PersonalActivity;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.enjoywater.view.dialog.DialogSubmitRefCode;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalFragment extends Fragment {
    private static final String TAG = "PersonalFragment";
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.iv_coin)
    ImageView ivCoin;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.group_user_info)
    Group groupUserInfo;
    @BindView(R.id.tv_wellcome)
    TextView tvWellcome;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_user_code)
    ImageView ivUserCode;
    @BindView(R.id.text_user_code)
    TextView textUserCode;
    @BindView(R.id.tv_user_code)
    TextView tvUserCode;
    @BindView(R.id.btn_user_code)
    ConstraintLayout btnUserCode;
    @BindView(R.id.iv_share_code)
    ImageView ivShareCode;
    @BindView(R.id.text_share_code)
    TextView textShareCode;
    @BindView(R.id.btn_share_code)
    ConstraintLayout btnShareCode;
    @BindView(R.id.iv_submit_code)
    ImageView ivSubmitCode;
    @BindView(R.id.text_submit_code)
    TextView textSubmitCode;
    @BindView(R.id.v_overlay_submit_code)
    View vOverlaySubmitCode;
    @BindView(R.id.btn_submit_code)
    ConstraintLayout btnSubmitCode;
    @BindView(R.id.iv_rate_app)
    ImageView ivRateApp;
    @BindView(R.id.text_rate_app)
    TextView textRateApp;
    @BindView(R.id.btn_rate_app)
    ConstraintLayout btnRateApp;
    @BindView(R.id.iv_feedback_email)
    ImageView ivFeedbackEmail;
    @BindView(R.id.text_feedback_email)
    TextView textFeedbackEmail;
    @BindView(R.id.btn_feedback_email)
    ConstraintLayout btnFeedbackEmail;
    @BindView(R.id.iv_hotline)
    ImageView ivHotline;
    @BindView(R.id.text_hotline)
    TextView textHotline;
    @BindView(R.id.btn_hotline)
    ConstraintLayout btnHotline;
    @BindView(R.id.iv_website)
    ImageView ivWebsite;
    @BindView(R.id.text_website)
    TextView textWebsite;
    @BindView(R.id.btn_website)
    ConstraintLayout btnWebsite;
    @BindView(R.id.iv_logout)
    ImageView ivLogout;
    @BindView(R.id.text_logout)
    TextView textLogout;
    @BindView(R.id.btn_logout)
    ConstraintLayout btnLogout;
    @BindView(R.id.btn_login_now)
    Button btnLoginNow;
    @BindView(R.id.group_user_action)
    Group groupUserAction;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.btn_event)
    ImageView btnEvent;
    @BindView(R.id.ripple_event)
    RippleView rippleEvent;
    @BindView(R.id.layout_content)
    ConstraintLayout layoutContent;
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
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private Context mContext;
    private MainService mainService;
    private User mUser;
    private String mToken;
    private Gson gson = new Gson();
    private boolean isLoading = false;
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");

    public static PersonalFragment newInstance() {
        PersonalFragment homeFragment = new PersonalFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(mContext);
        mToken = Utils.getToken(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(() -> {
            if (Utils.isInternetOn(mContext)) {
                if (!isLoading) {
                    getDataUser();
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            } else {
                swipeRefresh.setRefreshing(false);
                showError(Constants.DataNotify.NO_CONNECTION);
            }
        });
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        btnLoginNow.setOnClickListener(v -> {
            if (!isLoading) {
                getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN);
                (getActivity()).overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
            }
        });
        btnUserCode.setOnClickListener(v -> {
            Utils.copyTextToClipboard(mContext, mUser.getUserInfo().getMyCode());
        });
        btnShareCode.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Bạn tôi ơi! Muốn gọi nước sạch và tốt cho sức khỏe hãy tải ngay app Enjoy Water tại:\n"
                    + Constants.Value.APP_LINK
                    + "\nNhớ nhập mã giới thiệu của tôi là " + mUser.getUserInfo().getMyCode() + " để được tích điểm khi mua hàng nhé!");
            sendIntent.setType("text/plain");
            startActivity((Intent.createChooser(sendIntent, "Chia sẻ tới")));
        });
        btnSubmitCode.setOnClickListener(v -> {
            submitCode();
        });
        btnRateApp.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.Value.ANDROID_LINK)));
        });
        btnFeedbackEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:nuockhoangvital@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi của khách hàng");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });
        btnHotline.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Gọi đến hotline của Enjoy Water?")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Thao tác này cần cấp quyền quay số điện thoại", Snackbar.LENGTH_INDEFINITE).setAction("Cho Phép",
                                            v1 -> ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MainActivity.REQUEST_CODE_CALL_PHONE_PERMISSION)).show();
                                } else {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MainActivity.REQUEST_CODE_CALL_PHONE_PERMISSION);
                                }
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.Value.HOTLINE));
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });
        btnWebsite.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.Value.WEBSITE_LINK));
            startActivity(browserIntent);
        });
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Bạn muốn đăng xuất?")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý", (dialog, id) -> {
                        logout();
                    })
                    .setNegativeButton("Hủy", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        rippleEvent.setOnRippleCompleteListener(rippleView -> {
            startActivity(new Intent(mContext, EventActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        });
        tvCoin.setOnClickListener(view -> {
            startActivity(new Intent(mContext, BonusHistoryActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        });
        setDataUser();
    }

    private void getDataUser() {
        if (mToken != null && !mToken.isEmpty()) {
            isLoading = true;
            Call<BaseResponse> getUserInfo = mainService.getUserInfo(mToken);
            getUserInfo.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    isLoading = false;
                    if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null) {
                        if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                            if (baseResponse.getData().isJsonObject()) {
                                User.UserInfo userInfo = gson.fromJson(baseResponse.getData().getAsJsonObject(), User.UserInfo.class);
                                if (userInfo != null) {
                                    mUser.setUserInfo(userInfo);
                                    Utils.saveUser(mContext, mUser);
                                    setDataUser();
                                } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                            } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                        } else {
                            String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                            if (baseResponse.getError() != null && baseResponse.getError().getMessage() != null && !baseResponse.getError().getMessage().isEmpty()) {
                                message = baseResponse.getError().getMessage();
                            }
                            showError(message);
                        }
                    } else showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    isLoading = false;
                    if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
                    t.printStackTrace();
                    showError(Constants.DataNotify.DATA_ERROR_TRY_AGAIN);
                }
            });
        } else showError(Constants.DataNotify.NOT_LOGIN_YET);
    }

    private void setDataUser() {
        if (mUser != null) {
            showContent();
            btnLoginNow.setVisibility(View.GONE);
            tvWellcome.setVisibility(View.GONE);
            groupUserInfo.setVisibility(View.VISIBLE);
            groupUserAction.setVisibility(View.VISIBLE);
            rippleEvent.setVisibility(View.VISIBLE);
            String avatar = mUser.getUserInfo().getAvatar();
            if (avatar != null && !avatar.isEmpty())
                Glide.with(mContext).load(avatar).apply(RequestOptions.errorOf(R.drawable.avatar_default)).into(ivAvatar);
            else ivAvatar.setImageResource(R.drawable.avatar_default);
            String name = mUser.getUserInfo().getName();
            if (name != null && !name.isEmpty()) tvUserName.setText(name);
            else tvUserName.setText(R.string.guest);
            tvUserType.setText(mUser.getUserInfo().getLevelInfo().getName());
            tvCoin.setText(formatVND.format(mUser.getUserInfo().getCoin()));
            tvUserCode.setText(mUser.getUserInfo().getMyCode());
            if (mUser.getUserInfo().getRefId() != null && !mUser.getUserInfo().getRefId().isEmpty() && !mUser.getUserInfo().getRefId().equals("0"))
                vOverlaySubmitCode.setVisibility(View.VISIBLE);
            else vOverlaySubmitCode.setVisibility(View.GONE);
            Glide.with(mContext).load(R.drawable.gif_event).into(btnEvent);
            appbar.setOnClickListener(v -> {
                startActivity(new Intent(mContext, PersonalActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
            });
        } else showError(Constants.DataNotify.NOT_LOGIN_YET);
    }

    @SuppressLint("HandlerLeak")
    private void submitCode() {
        new DialogSubmitRefCode(mContext, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mUser = (User) msg.obj;
                    Utils.saveUser(mContext, mUser);
                    vOverlaySubmitCode.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void logout() {
        isLoading = true;
        showLoading(false);
        Utils.clearUser(mContext);
        String deviceId = Utils.getDeviceUuid(mContext);
        String devicetoken = Utils.getDeviceToken(mContext);
        Call<BaseResponse> registerDevice = mainService.registerDevice("", deviceId, devicetoken, Constants.Value.ANDROID, true);
        registerDevice.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                reStart();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                reStart();
            }
        });

    }

    private void reStart() {
        Intent intent = getActivity().getIntent();
        intent.putExtra("tab_selected", 4);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }

    private void showLoading(boolean goneContent) {
        layoutLoading.setVisibility(View.VISIBLE);
        if (goneContent) {
            appbar.setVisibility(View.GONE);
            layoutContent.setVisibility(View.GONE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        appbar.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        if (error.equals(Constants.DataNotify.NOT_LOGIN_YET)) {
            showContent();
            groupUserInfo.setVisibility(View.GONE);
            groupUserAction.setVisibility(View.GONE);
            rippleEvent.setVisibility(View.GONE);
            tvWellcome.setVisibility(View.VISIBLE);
            btnLoginNow.setVisibility(View.VISIBLE);
            ivAvatar.setImageResource(R.drawable.avatar_default);
            appbar.setOnClickListener(v -> {
                getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN);
                (getActivity()).overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
            });
        } else {
            appbar.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
            layoutContent.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            tvError.setText(error);
            btnLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_LOGIN_FROM_MAIN && resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS) {
            mToken = Utils.getToken(mContext);
            mUser = Utils.getUser(mContext);
            setDataUser();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        switch (event.getAction()) {
            case Constants.Key.PROFILE_UPDATED: {
                mUser = (User) event.getObject();
                setDataUser();
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
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }
}
