package com.enjoywater.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.enjoywater.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.v_empty)
    View vEmpty;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
