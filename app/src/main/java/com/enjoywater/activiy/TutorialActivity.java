package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.fragment.TutorialFragment;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.RippleViewLinear;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabDots)
    TabLayout tabDots;
    @BindView(R.id.ripple_btn_skip)
    RippleViewLinear rippleBtnSkip;
    @BindView(R.id.main_content)
    ConstraintLayout mainContent;
    @BindView(R.id.btn_next)
    LinearLayout btnNext;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ic_next)
    ImageView icNext;
    private MyPagerAdapter mPagerAdapter;
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        initUI();
        Utils.saveBoolean(this, "first_run", false);
    }

    private void initUI() {
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tabDots.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                switch (position) {
                    case 4: {
                        tvNext.setText(R.string.start);
                        icNext.setVisibility(View.GONE);
                        btnNext.setOnClickListener(v -> {
                            startActivity(new Intent(TutorialActivity.this, SplashActivity.class));
                            overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
                            finish();
                        });
                        break;
                    }
                    default: {
                        tvNext.setText(R.string.next);
                        icNext.setVisibility(View.VISIBLE);
                        btnNext.setOnClickListener(v -> {
                            viewPager.setCurrentItem(mCurrentPosition + 1);
                        });
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        btnNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(mCurrentPosition + 1);
        });
        rippleBtnSkip.setOnRippleCompleteListener(rippleView -> {
            startActivity(new Intent(TutorialActivity.this, SplashActivity.class));
            overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
            finish();
        });
        initFragment();
    }

    private void initFragment() {
        ArrayList<Fragment> fragmentList = mPagerAdapter.getFragmentList();
        /*TutorialFragment fragment1 = TutorialFragment.newInstance("Ứng dụng gọi nước thông minh 4.0", new String[] {"Chỉ với 1 click, chúng tôi đã sẵn sàng phục vụ nhu cầu uống nước sạch của bạn!"});
        TutorialFragment fragment2 = TutorialFragment.newInstance("Ở đây chúng tôi chỉ có NƯỚC TỐT!", new String[] {"Nước tốt là nước không có tính Acid, gây ảnh hưởng đến dạ dày.",
                "Nước tốt là nước có nhiều khoáng chất cho cơ thể.",
                "Nước tốt là nước được kiểm tra vệ sinh ATTP 3 tháng/lần."});
        TutorialFragment fragment3 = TutorialFragment.newInstance("Phục vụ Quý khách hàng TỪ TÂM", new String[] {"Sức khỏe của Quý khách hàng là sức khỏe của chúng tôi.",
                "Chúng tôi mua bảo hiểm cho tất cả các sản phẩm đã cung cấp.",
                "Chúng tôi phục vụ theo Lợi ích của Quý khách hàng."});
        TutorialFragment fragment4 = TutorialFragment.newInstance("Tặng 20.000 điểm thưởng khi đăng ký tài khoản thành công!", new String[] {"Tặng ngay 5.000 điểm thưởng khi nhập mã giới thiệu của bạn bè.",
                "Tặng 10% giá trị đơn hàng đầu tiên khi giao dịch thành công."});
        TutorialFragment fragment5 = TutorialFragment.newInstance("Mỗi lời giới thiệu App là thêm một nguồn thu nhập thụ động cho bạn!", new String[] {"Tặng ngay 5.000 điểm thưởng khi bạn bè nhập mã giới thiệu của bạn.",
                "Tặng 2% giá trị đơn hàng mỗi khi người được bạn giới thiệu mua hàng thành công.",
                "Người được giới thiệu cũng sẽ nhận ngay 1% giá trị mỗi đơn hàng."});*/
        TutorialFragment fragment1 = TutorialFragment.newInstance("Ứng dụng \"Đặt nước 1 chạm trong kỷ  nguyên 4.0\"",
                new String[]{"Một chạm khi hết nước, 1 chạm để phục vụ.",
                        "Một chạm thông minh, 1 chạm tiết kiệm."});
        TutorialFragment fragment2 = TutorialFragment.newInstance("Chúng tôi chỉ có NƯỚC TỐT",
                new String[]{"Nước tốt để trung hòa acid thừa trong cơ thể, ngừa đau dạ dày và ung thư.",
                        "Nước tốt làm chậm quá trình lão hóa trong cơ thể.",
                        "Nước tốt bổ sung khoáng chất hàng ngày cho cơ thể bạn."});
        TutorialFragment fragment3 = TutorialFragment.newInstance("Phục vụ TỪ TÂM",
                new String[]{"Sức khỏe của khách hàng quan trọng hơn sức khỏe của chúng tôi.",
                        "Sản phẩm được Bảo Việt bảo hiểm VSATTP.",
                        "Phục vụ vì \"Lợi ích của Khách hàng\"."});
        TutorialFragment fragment4 = TutorialFragment.newInstance("Tặng 20.000 điểm thưởng khi đăng ký tài khoản thành công!",
                new String[]{"Tặng 5.000 điểm thưởng khi bạn bè nhập mã giới thiệu của bạn.",
                        "Tặng điểm thưởng bằng 10% giá trị đơn hàng đầu tiên."});
        TutorialFragment fragment5 = TutorialFragment.newInstance("Giới thiệu app cho bạn bè, tạo thu nhập thụ động.",
                new String[]{"Khi bạn của bạn mua hàng, bạn được điểm thưởng bằng 2% giá trị đơn hàng đó.",
                        "Mọi đơn hàng phát sinh đều được tích điểm thưởng."});
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
        fragmentList.add(fragment5);
        mPagerAdapter.notifyDataSetChanged();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFrag(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public ArrayList<Fragment> getFragmentList() {
            return fragmentList;
        }
    }
}
