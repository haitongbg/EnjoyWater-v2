package com.enjoywater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enjoywater.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialFragment extends Fragment {
    private static final String TAG = "TutorialFragment";
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ic_desc_1)
    ImageView icDesc1;
    @BindView(R.id.tv_desc_1)
    TextView tvDesc1;
    @BindView(R.id.ic_desc_2)
    ImageView icDesc2;
    @BindView(R.id.tv_desc_2)
    TextView tvDesc2;
    @BindView(R.id.ic_desc_3)
    ImageView icDesc3;
    @BindView(R.id.tv_desc_3)
    TextView tvDesc3;
    @BindView(R.id.group_desc_1)
    Group groupDesc1;
    @BindView(R.id.group_desc_2)
    Group groupDesc2;
    @BindView(R.id.group_desc_3)
    Group groupDesc3;
    private String mTitle;
    private String[] mDescriptions;

    public static TutorialFragment newInstance(String title, String[] descriptions) {
        TutorialFragment notifyFragment = new TutorialFragment();
        notifyFragment.init(title, descriptions);
        return notifyFragment;
    }

    private void init(String title, String[] descriptions) {
        mTitle = title;
        mDescriptions = descriptions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle.setText(mTitle);
        groupDesc3.setVisibility(View.GONE);
        groupDesc2.setVisibility(View.GONE);
        groupDesc1.setVisibility(View.GONE);
        if (mDescriptions != null && mDescriptions.length > 0) {
            groupDesc1.setVisibility(View.VISIBLE);
            tvDesc1.setText(mDescriptions[0]);
            if (mDescriptions.length > 1) {
                groupDesc2.setVisibility(View.VISIBLE);
                tvDesc2.setText(mDescriptions[1]);
                if (mDescriptions.length > 2) {
                    groupDesc3.setVisibility(View.VISIBLE);
                    tvDesc3.setText(mDescriptions[2]);
                }
            }
        }
    }
}
