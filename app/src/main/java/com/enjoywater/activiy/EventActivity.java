package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.model.User;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.RippleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_event_thumb)
    ImageView ivEventThumb;
    @BindView(R.id.tv_event_title)
    TextView tvEventTitle;
    @BindView(R.id.tv_event_content)
    TextView tvEventContent;
    @BindView(R.id.btn_copy_code)
    Button btnCopyCode;
    @BindView(R.id.btn_share_code)
    Button btnShareCode;
    @BindView(R.id.ripple_btn_view_my_coin)
    RippleView rippleBtnViewMyCoin;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        mUser = Utils.getUser(this);
        if (mUser != null) initUI();
        else {
            Toast.makeText(this, "Vui lòng đăng nhập để tham gia chương trình này.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initUI() {
        btnBack.setOnClickListener(view -> onBackPressed());
        rippleBtnViewMyCoin.setOnRippleCompleteListener(rippleView -> {
            startActivity(new Intent(EventActivity.this, BonusHistoryActivity.class));
            overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
        });
        btnShareCode.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Bạn tôi ơi! Muốn gọi nước sạch và tốt cho sức khỏe hãy tải ngay app Enjoy Water tại:\n"
                    + Constants.Value.APP_LINK
                    + "\nNhớ nhập mã giới thiệu của tôi là " + mUser.getUserInfo().getMyCode() + " để được tích điểm khi mua hàng nhé!");
            sendIntent.setType("text/plain");
            startActivity((Intent.createChooser(sendIntent, "Chia sẻ tới")));
        });
        btnCopyCode.setOnClickListener(view -> {
            Utils.copyTextToClipboard(this, mUser.getUserInfo().getMyCode());
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }
}
