package com.enjoywater.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TvSegoeuiRegular extends AppCompatTextView {
    private static Typeface mTypeface;

    public TvSegoeuiRegular(final Context context) {
        this(context, null);
    }

    public TvSegoeuiRegular(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvSegoeuiRegular(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "segoeui.ttf");
        }
        setTypeface(mTypeface);
    }

    public interface OnLayoutListener {
        void onLayouted(AppCompatTextView view);
    }

    private TvSegoeuiSemiBold.OnLayoutListener mOnLayoutListener;

    public void setOnLayoutListener(TvSegoeuiSemiBold.OnLayoutListener listener) {
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
