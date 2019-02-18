package com.enjoywater.activiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.model.User;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.view.RippleView;
import com.enjoywater.view.dialog.DialogActiveAccount;
import com.enjoywater.view.dialog.DialogRegisterAccount;
import com.enjoywater.view.dialog.DialogRetrivePassword;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final int RESULT_CODE_LOGIN_SUCCESS = 200;
    public static final int RESULT_CODE_LOGIN_CANCEL = 201;
    private static final int REQUEST_CODE_SIGN_IN_BY_GOOGLE = 100;
    private static final int RESULT_CODE_SIGN_IN_BY_GOOGLE_SUCCESS = 101;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.ripple_forget_password)
    RippleView rippleForgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.ripple_register)
    RippleView rippleRegister;
    @BindView(R.id.btn_login_by_google)
    Button btnLoginByGoogle;
    @BindView(R.id.btn_login_by_facebook)
    Button btnLoginByFacebook;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    private MainService mainService;
    private User mUser;
    private Gson gson = new Gson();
    private GoogleSignInClient mGoogleSignInClient;
    private boolean isLoading;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                //.requestServerAuthCode("269078269723-7lqj6of20rs7603239f0jupneslk91fd.apps.googleusercontent.com")
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        if (accessToken != null && !accessToken.isExpired())
                            loginBySocialAccessToken(Constants.Value.FACEBOOK, accessToken.getToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
                    }
                });

        initUI();
        String jsonUser = Utils.getString(this, Constants.Key.USER, "");
        if (!jsonUser.isEmpty()) {
            mUser = gson.fromJson(jsonUser, User.class);
        }
    }

    private void initUI() {
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(progress -> {
            if (progress == 0) {
                progressLoading.setProgress(1.0f);
            } else if (progress == 1.0f) {
                progressLoading.setProgress(0.0f);
            }
        });
        layoutLoading.setVisibility(View.GONE);
        btnLogin.setOnClickListener(view -> {
            if (!isLoading) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.isEmpty())
                    Toast.makeText(this, R.string.empty_email, Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                    Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
                else loginByAccount(email, password);
            }
        });
        btnLoginByGoogle.setOnClickListener(view -> {
            if (!isLoading) loginByGoogle();
        });
        btnLoginByFacebook.setOnClickListener(view -> {
            if (!isLoading) loginByFacebook();
        });
        rippleRegister.setOnRippleCompleteListener(rippleView -> {
            if (!isLoading) register();
        });
        rippleForgetPassword.setOnRippleCompleteListener(rippleView -> {
            if (!isLoading) retrivePassword();
        });
    }

    private void loginByAccount(String email, String password) {
        isLoading = true;
        layoutLoading.setVisibility(View.VISIBLE);
        Call<BaseResponse> login = mainService.login(email, password);
        login.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                layoutLoading.setVisibility(View.GONE);
                BaseResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                        if (loginResponse.getData().isJsonObject()) {
                            mUser = gson.fromJson(loginResponse.getData().getAsJsonObject(), User.class);
                            if (mUser != null && mUser.getToken() != null && !mUser.getToken().isEmpty() && mUser.getUserInfo() != null) {
                                Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                                if (mUser.getUserInfo().isActivated()) {
                                    finishLogin(RESULT_CODE_LOGIN_SUCCESS);
                                } else {
                                    activeAccount();
                                }
                            } else
                                Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = getResources().getString(R.string.data_error);
                        if (loginResponse.getError() != null && loginResponse.getError().getMessage() != null && !loginResponse.getError().getMessage().isEmpty()) {
                            message = loginResponse.getError().getMessage();
                            if (message.equalsIgnoreCase("Invalid email"))
                                message = getResources().getString(R.string.email_invalid);
                            if (message.equalsIgnoreCase("User invalid"))
                                message = getResources().getString(R.string.user_invalid);
                            if (message.equalsIgnoreCase("Password invalid"))
                                message = getResources().getString(R.string.password_wrong);
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    private void loginBySocialAccessToken(String type, String token) {
        isLoading = true;
        layoutLoading.setVisibility(View.VISIBLE);
        Call<BaseResponse> loginWithSocialAccessToken = mainService.loginBySocialAccessToken(type, token);
        loginWithSocialAccessToken.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                layoutLoading.setVisibility(View.GONE);
                BaseResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                        if (loginResponse.getData().isJsonObject()) {
                            mUser = gson.fromJson(loginResponse.getData().getAsJsonObject(), User.class);
                            if (mUser != null && mUser.getToken() != null && !mUser.getToken().isEmpty() && mUser.getUserInfo() != null) {
                                Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                                if (mUser.getUserInfo().isActivated()) {
                                    finishLogin(RESULT_CODE_LOGIN_SUCCESS);
                                } else {
                                    activeAccount();
                                }
                            } else
                                Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = getResources().getString(R.string.data_error);
                        if (loginResponse.getError() != null && loginResponse.getError().getMessage() != null && !loginResponse.getError().getMessage().isEmpty()) {
                            message = loginResponse.getError().getMessage();
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    private void loginByGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN_BY_GOOGLE);
        } else loginBySocialAccessToken(Constants.Value.GOOGLE, account.getIdToken());
    }

    private void loginByFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            loginBySocialAccessToken(Constants.Value.FACEBOOK, accessToken.getToken());
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }

    @SuppressLint("HandlerLeak")
    private void register() {
        new DialogRegisterAccount(this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mUser = (User) msg.obj;
                    Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                    if (mUser.getUserInfo().isActivated()) {
                        finishLogin(RESULT_CODE_LOGIN_SUCCESS);
                    } else {
                        activeAccount();
                    }
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void activeAccount() {
        new DialogActiveAccount(LoginActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mUser.getUserInfo().setActivated(true);
                    Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                }
                finishLogin(RESULT_CODE_LOGIN_SUCCESS);
            }
        }, 0);
    }

    private CountDownTimer countDownDelaySending;
    private long delaySendingActiveCode = 0;

    @SuppressLint("HandlerLeak")
    private void retrivePassword() {
        new DialogRetrivePassword(LoginActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mUser = (User) msg.obj;
                    Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                    if (mUser.getUserInfo().isActivated()) {
                        finishLogin(RESULT_CODE_LOGIN_SUCCESS);
                    } else {
                        activeAccount();
                    }
                } else if (msg.what == Constants.Value.ACTION_CLOSE) {
                    delaySendingActiveCode = (long) msg.obj;
                    countDownDelaySending = new CountDownTimer(delaySendingActiveCode, 1000) {
                        public void onTick(long millisUntilFinished) {
                            delaySendingActiveCode = millisUntilFinished;
                        }

                        public void onFinish() {
                            delaySendingActiveCode = 0;
                        }
                    };
                    countDownDelaySending.start();
                }
            }
        }, delaySendingActiveCode);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginBySocialAccessToken(Constants.Value.GOOGLE, account.getIdToken());
        } catch (ApiException e) {
            Log.e("Goole loginByAccount", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN_BY_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLogin(RESULT_CODE_LOGIN_CANCEL);
    }

    private void finishLogin(int resultCode) {
        setResult(resultCode);
        finish();
        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
    }
}
