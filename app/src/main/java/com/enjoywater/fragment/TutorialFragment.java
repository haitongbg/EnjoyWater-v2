package com.enjoywater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    @BindView(R.id.tv_description)
    TextView tvDescription;
    private String mTitle = "", mDescription = "";

    public static TutorialFragment newInstance(String title, String description) {
        TutorialFragment notifyFragment = new TutorialFragment();
        notifyFragment.init(title, description);
        return notifyFragment;
    }

    private void init(String title, String description) {
        mTitle = title;
        mDescription = description;
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
        tvDescription.setText(mDescription);
    }
}
