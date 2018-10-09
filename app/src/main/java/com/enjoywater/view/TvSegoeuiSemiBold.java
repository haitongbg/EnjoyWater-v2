package com.enjoywater.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TvSegoeuiSemiBold extends AppCompatTextView {
    private static Typeface mTypeface;

    public TvSegoeuiSemiBold(final Context context) {
        this(context, null);
    }

    public TvSegoeuiSemiBold(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvSegoeuiSemiBold(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "seguisb.ttf");
        }
        setTypeface(mTypeface);
    }

    public interface OnLayoutListener {
        void onLayouted(AppCompatTextView view);
    }

    private OnLayoutListener mOnLayoutListener;

    public void setOnLayoutListener(OnLayoutListener listener) {
        mOnLayoutListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mOnLayoutListener != null) {
            mOnLayoutListener.onLayouted(this);
        }
    }
}
