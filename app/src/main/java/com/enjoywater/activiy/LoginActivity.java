package com.enjoywater.activiy;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mainService = MyApplication.getInstance().getMainService();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                //.requestServerAuthCode("269078269723-7lqj6of20rs7603239f0jupneslk91fd.apps.googleusercontent.com")
                .requestIdToken("269078269723-7lqj6of20rs7603239f0jupneslk91fd.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
            if (!isLoading) {
                loginByGoogle();
            }
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
                        if (loginResponse.getData().isJsonObject()
                                && loginResponse.getData().getAsJsonObject().has("token")
                                && loginResponse.getData().getAsJsonObject().has("userInfo")) {
                            String token = loginResponse.getData().getAsJsonObject().get("token").getAsString();
                            mUser = gson.fromJson(loginResponse.getData().getAsJsonObject().get("userInfo").getAsJsonObject().toString(), User.class);
                            if (token != null && !token.isEmpty() && mUser != null && mUser.getId() != null && !mUser.getId().isEmpty()) {
                                Utils.saveString(LoginActivity.this, Constants.Key.TOKEN, "Bearer " + token);
                                Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                                setResult(RESULT_CODE_LOGIN_SUCCESS);
                                finish();
                                overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
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
                                message = getResources().getString(R.string.password_invalid);
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

    private void loginByToken(String token) {
        isLoading = true;
        layoutLoading.setVisibility(View.VISIBLE);
        Call<BaseResponse> login = mainService.loginByToken(token);
        login.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                isLoading = false;
                layoutLoading.setVisibility(View.GONE);
                BaseResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                        if (loginResponse.getData().isJsonObject()
                                && loginResponse.getData().getAsJsonObject().has("token")
                                && loginResponse.getData().getAsJsonObject().has("userInfo")) {
                            String token = loginResponse.getData().getAsJsonObject().get("token").getAsString();
                            mUser = gson.fromJson(loginResponse.getData().getAsJsonObject().get("userInfo").getAsJsonObject().toString(), User.class);
                            if (token != null && !token.isEmpty() && mUser != null && mUser.getId() != null && !mUser.getId().isEmpty()) {
                                Utils.saveString(LoginActivity.this, Constants.Key.TOKEN, "Bearer " + token);
                                Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                                setResult(RESULT_CODE_LOGIN_SUCCESS);
                                finish();
                                overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
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
                                message = getResources().getString(R.string.password_invalid);
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
                        if (loginResponse.getData().isJsonObject()
                                && loginResponse.getData().getAsJsonObject().has("token")
                                && loginResponse.getData().getAsJsonObject().has("userInfo")) {
                            String token = loginResponse.getData().getAsJsonObject().get("token").getAsString();
                            mUser = gson.fromJson(loginResponse.getData().getAsJsonObject().get("userInfo").getAsJsonObject().toString(), User.class);
                            if (token != null && !token.isEmpty() && mUser != null && mUser.getId() != null && !mUser.getId().isEmpty()) {
                                Utils.saveString(LoginActivity.this, Constants.Key.TOKEN, "Bearer " + token);
                                Utils.saveString(LoginActivity.this, Constants.Key.USER, gson.toJson(mUser));
                                setResult(RESULT_CODE_LOGIN_SUCCESS);
                                finish();
                                overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
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
                                message = getResources().getString(R.string.password_invalid);
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
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN_BY_GOOGLE);
        } else loginBySocialAccessToken(Constants.Value.GOOGLE, account.getIdToken());
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginBySocialAccessToken(Constants.Value.GOOGLE, account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Goole loginByAccount", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, xin thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN_BY_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CODE_LOGIN_CANCEL);
        overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_300);
    }
}
