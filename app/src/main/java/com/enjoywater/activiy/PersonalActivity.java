package com.enjoywater.activiy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.enjoywater.R;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.text_coin)
    TextView textCoin;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.btn_coin_details)
    TextView btnCoinDetails;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.v_underline_name)
    View vUnderlineName;
    @BindView(R.id.text_gender)
    TextView textGender;
    @BindView(R.id.radio_male)
    RadioButton radioMale;
    @BindView(R.id.radio_female)
    RadioButton radioFemale;
    @BindView(R.id.radio_other)
    RadioButton radioOther;
    @BindView(R.id.radio_group_gender)
    RadioGroup radioGroupGender;
    @BindView(R.id.v_underline_gender)
    View vUnderlineGender;
    @BindView(R.id.text_birthday)
    TextView textBirthday;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.v_underline_birthday)
    View vUnderlineBirthday;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.v_underline_phone)
    View vUnderlinePhone;
    @BindView(R.id.text_email)
    TextView textEmail;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.v_underline_email)
    View vUnderlineEmail;
    @BindView(R.id.text_ref_code)
    TextView textRefCode;
    @BindView(R.id.tv_ref_code)
    TextView tvRefCode;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.btn_save)
    Button btnSave;
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
    private MainService mainService;
    private User mUser;
    private boolean isLoading = false;
    private Gson gson = new Gson();
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initUI();
        mainService = MyApplication.getInstance().getMainService();
        mUser = Utils.getUser(this);
        if (mUser != null) setDataUser();
        else showError(Constants.DataNotify.NOT_LOGIN_YET);
    }

    private void initUI() {
        btnBack.setOnClickListener(v -> onBackPressed());
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetOn(PersonalActivity.this)) {
                    if (!isLoading) {
                        getDataUser();
                    } else {
                        swipeRefresh.setRefreshing(false);
                    }
                } else {
                    swipeRefresh.setRefreshing(false);
                    showError(Constants.DataNotify.NO_CONNECTION);
                }
            }
        });
        //swipeRefresh.setEnabled(false);
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        btnSave.setOnClickListener(v -> {
            preUpdateUserInfo();
        });
    }

    private void getDataUser() {
        isLoading = true;
    }

    private void setDataUser() {
        String avatar = mUser.getUserInfo().getAvatar();
        if (avatar != null && !avatar.isEmpty())
            Glide.with(this).load(avatar).apply(RequestOptions.errorOf(R.drawable.avatar_default)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    ivBackground.setImageResource(R.drawable.bg_splash);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ivBackground.setImageDrawable(resource);
                    return false;
                }
            }).into(ivAvatar);
        else {
            ivAvatar.setImageResource(R.drawable.avatar_default);
            //ivBackground.setImageResource(R.drawable.bg_splash);
        }
        String name = mUser.getUserInfo().getName();
        if (name != null && !name.isEmpty()) {
            tvName.setText(name);
            edtName.setText(name);
            edtName.setSelection(edtName.length());
        } else {
            tvName.setText(R.string.guest);
            edtName.setText("");
        }
        tvCoin.setText(formatVND.format(mUser.getUserInfo().getCoin()));
        switch (mUser.getUserInfo().getGender()) {
            case Constants.Value.MALE: {
                radioMale.setChecked(true);
                break;
            }
            case Constants.Value.FEMALE: {
                radioFemale.setChecked(true);
                break;
            }
            default: {
                radioOther.setChecked(true);
                break;
            }
        }
        String birthday = mUser.getUserInfo().getBirthday();
        if (birthday != null && !birthday.isEmpty()) {
            calendar.setTimeInMillis(Long.parseLong(birthday) * 1000);
            tvBirthday.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        } else tvBirthday.setText("");
        if (mUser.getUserInfo().getPhone() != null)
            edtPhone.setText(mUser.getUserInfo().getPhone());
        tvEmail.setText(mUser.getUserInfo().getEmail());
        tvRefCode.setText(mUser.getUserInfo().getMyCode());
    }

    private void preUpdateUserInfo() {

    }

    private void showLoading(boolean goneContent) {
        if (goneContent) {
            swipeRefresh.setRefreshing(true);
            layoutContent.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.GONE);
        } else {
            layoutLoading.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.VISIBLE);
        }
        layoutError.setVisibility(View.GONE);
    }

    private void showContent() {
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(@NonNull String error) {
        layoutLoading.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvError.setText(error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }
}
