package com.enjoywater.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import com.enjoywater.R;
import butterknife.ButterKnife;


public class DialogOrderAddress {
    private Context mContext;
    private Dialog mDialog;
    private Handler mCallBackHandler;

    public DialogOrderAddress(Context context, Handler callbackHandler) {
        mContext = context;
        mCallBackHandler = callbackHandler;
        initUI();
    }

    public void initUI() {
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_order_address);
        ButterKnife.bind(DialogOrderAddress.this, mDialog);
        mDialog.show();
    }
}
