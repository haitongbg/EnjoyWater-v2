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
    @BindView(R.id.ripple_btn_next)
    RippleViewLinear rippleBtnNext;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ic_next)
    ImageView icNext;
    private MyPagerAdapter mPagerAdapter;
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstRun = Utils.getBoolean(this,"first_run", true);
        if (!isFirstRun) {
            startActivity(new Intent(TutorialActivity.this, SplashActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_tutorial);
            ButterKnife.bind(this);
            initUI();
            Utils.saveBoolean(this,"first_run", false);
        }
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
                    case 3: {
                        tvNext.setText(R.string.start);
                        icNext.setVisibility(View.GONE);
                        rippleBtnNext.setOnRippleCompleteListener(rippleView -> {
                            startActivity(new Intent(TutorialActivity.this, SplashActivity.class));
                            overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
                            finish();
                        });
                        break;
                    }
                    default: {
                        tvNext.setText(R.string.next);
                        icNext.setVisibility(View.VISIBLE);
                        rippleBtnNext.setOnRippleCompleteListener(rippleView -> {
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
        rippleBtnNext.setOnRippleCompleteListener(rippleView -> {
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
        TutorialFragment fragment1 = TutorialFragment.newInstance(getString(R.string.tutorial_1_title), getString(R.string.tutorial_1_desc));
        TutorialFragment fragment2 = TutorialFragment.newInstance(getString(R.string.tutorial_2_title), getString(R.string.tutorial_2_desc));
        TutorialFragment fragment3 = TutorialFragment.newInstance(getString(R.string.tutorial_3_title), getString(R.string.tutorial_3_desc));
        TutorialFragment fragment4 = TutorialFragment.newInstance(getString(R.string.tutorial_4_title), getString(R.string.tutorial_4_desc));
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
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
