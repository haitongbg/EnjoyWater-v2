package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.enjoywater.R;
import com.enjoywater.view.RippleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.iv_email)
    ImageView ivEmail;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.ripple_retrieve_password)
    RippleView rippleRetrievePassword;
    @BindView(R.id.ripple_login)
    RippleView rippleLogin;
    @BindView(R.id.ripple_register)
    RippleView rippleRegister;
    @BindView(R.id.ripple_skip)
    RippleView rippleSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        rippleLogin.setOnRippleCompleteListener(rippleView -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
        rippleRegister.setOnRippleCompleteListener(rippleView -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}
