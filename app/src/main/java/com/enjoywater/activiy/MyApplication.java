package com.enjoywater.activiy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.enjoywater.R;
import com.enjoywater.model.Location.City;
import com.enjoywater.retrofit.MainService;
import com.enjoywater.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MyApplication extends MultiDexApplication {
    private MainService mainService;
    private static MyApplication mInstance;
    private ArrayList<City> mCities;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mFaceBookCallbackManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        createNotificationChannel();
        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.EMAIL))
                .requestServerAuthCode(getResources().getString(R.string.google_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Facebook
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        mFaceBookCallbackManager = CallbackManager.Factory.create();
        //Log.e("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));
        mCities = Utils.getCities(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MainService getMainService() {
        if (mainService == null) mainService = MainService.Factory.create();
        return mainService;
    }

    public ArrayList<City> getCities() {
        if (mCities == null) mCities = Utils.getCities(this);
        return mCities;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public CallbackManager getFaceBookCallbackManager() {
        return mFaceBookCallbackManager;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.my_notif_channel_name);
            String description = getString(R.string.my_notif_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.my_notif_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
