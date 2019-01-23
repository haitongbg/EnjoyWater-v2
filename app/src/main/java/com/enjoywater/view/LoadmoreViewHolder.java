package com.enjoywater.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.enjoywater.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadmoreViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.progress_loadmore)
    ProgressWheel progressLoadmore;
    @BindView(R.id.tv_loadmore)
    TvSegoeuiSemiBold tvLoadmore;

    public LoadmoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        progressLoadmore.setProgress(0.5f);
        progressLoadmore.setCallback(progress -> {
            if (progress == 0) {
                progressLoadmore.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoadmore.setProgress(0.0f);
            }
        });
    }
}
