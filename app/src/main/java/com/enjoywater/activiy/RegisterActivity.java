package com.enjoywater.activiy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.enjoywater.R;
import com.enjoywater.view.RippleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_promote_code)
    EditText edtPromoteCode;
    @BindView(R.id.ripple_register)
    RippleView rippleRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
}
