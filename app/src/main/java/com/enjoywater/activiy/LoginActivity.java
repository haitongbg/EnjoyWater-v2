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
import com.enjoywater.retrofit.response.GoogleOAuthResponse;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;

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
    private CallbackManager mFaceBookCallbackManager;
    private CountDownTimer countDownDelaySending;
    private long delaySendingActiveCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        // Google
        mGoogleSignInClient = MyApplication.getInstance().getGoogleSignInClient();
        // Facebook
        mFaceBookCallbackManager = MyApplication.getInstance().getFaceBookCallbackManager();
        LoginManager.getInstance().registerCallback(mFaceBookCallbackManager, new FacebookCallback<LoginResult>() {
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
        mUser = Utils.getUser(this);
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
            if (!isLoading) loginGoogle();
        });
        btnLoginByFacebook.setOnClickListener(view -> {
            if (!isLoading) loginFacebook();
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
                                Utils.saveUser(LoginActivity.this, mUser);
                                registeDevice(mUser.getUserInfo().getId());
                            } else
                                Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
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
                    Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
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
                                Utils.saveUser(LoginActivity.this, mUser);
                                registeDevice(mUser.getUserInfo().getId());
                            } else
                                Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = Constants.DataNotify.DATA_ERROR_TRY_AGAIN;
                        if (loginResponse.getError() != null && loginResponse.getError().getMessage() != null && !loginResponse.getError().getMessage().isEmpty()) {
                            message = loginResponse.getError().getMessage();
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    //Google
    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN_BY_GOOGLE);
        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN_BY_GOOGLE);
        } else loginBySocialAccessToken(Constants.Value.GOOGLE, account.getServerAuthCode());*/
    }

    private void handleGoogleLoginResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            getGoogleAccessToken(account.getServerAuthCode());
        } catch (ApiException e) {
            Log.e("Goole loginByAccount", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getGoogleAccessToken(String authCode) {
        isLoading = true;
        layoutLoading.setVisibility(View.VISIBLE);
        String clientId = getString(R.string.google_client_id);
        String clientSecret = getString(R.string.google_client_secret);
        String grant_type = Constants.Value.AUTHORIZATION_CODE;
        String refreshToken = Utils.getRefreshToken(this);
        if (!refreshToken.isEmpty()) {
            grant_type = Constants.Value.REFRESH_TOKEN;
            authCode = "";
        }
        Call<GoogleOAuthResponse> getAccessToken = mainService.getAccessToken(clientId, clientSecret, grant_type, authCode, refreshToken);
        getAccessToken.enqueue(new Callback<GoogleOAuthResponse>() {
            @Override
            public void onResponse(Call<GoogleOAuthResponse> call, Response<GoogleOAuthResponse> response) {
                GoogleOAuthResponse googleOAuthResponse = response.body();
                if (googleOAuthResponse != null) {
                    String refreshToken = googleOAuthResponse.getRefresh_token();
                    if (refreshToken != null && !refreshToken.isEmpty()) Utils.saveRefreshToken(LoginActivity.this, refreshToken);
                    String accessToken = googleOAuthResponse.getAccess_token();
                    loginBySocialAccessToken(Constants.Value.GOOGLE, accessToken);
                } else {
                    isLoading = false;
                    layoutLoading.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoogleOAuthResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, Constants.DataNotify.DATA_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    //Facebook
    private void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile email"));
        /*AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            loginBySocialAccessToken(Constants.Value.FACEBOOK, accessToken.getToken());
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile email"));
        }*/
    }

    @SuppressLint("HandlerLeak")
    private void register() {
        new DialogRegisterAccount(this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    mUser = (User) msg.obj;
                    Utils.saveUser(LoginActivity.this, mUser);
                    registeDevice(mUser.getUserInfo().getId());
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
                    Utils.saveUser(LoginActivity.this, mUser);
                }
                finishLogin(RESULT_CODE_LOGIN_SUCCESS);
            }
        }, 0);
    }

    @SuppressLint("HandlerLeak")
    private void retrivePassword() {
        String email = edtEmail.getText().toString();
        new DialogRetrivePassword(LoginActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.Value.ACTION_SUCCESS) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
                    String email = hashMap.get(Constants.Key.EMAIL);
                    String password = hashMap.get(Constants.Key.PASSWORD);
                    edtEmail.setText(email);
                    edtPassword.setText(password);
                    loginByAccount(email, password);
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
        }, delaySendingActiveCode, email);
    }

    private void registeDevice(String userId) {
        String deviceId = Utils.getDeviceUuid(this);
        String devicetoken = Utils.getDeviceToken(this);
        Call<BaseResponse> registerDevice = mainService.registerDevice(userId, deviceId, devicetoken , Constants.Value.ANDROID, true);
        registerDevice.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (mUser.getUserInfo().isActivated()) {
                    finishLogin(RESULT_CODE_LOGIN_SUCCESS);
                } else {
                    activeAccount();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
                if (mUser.getUserInfo().isActivated()) {
                    finishLogin(RESULT_CODE_LOGIN_SUCCESS);
                } else {
                    activeAccount();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN_BY_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleLoginResult(task);
        } else {
            mFaceBookCallbackManager.onActivityResult(requestCode, resultCode, data);
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
