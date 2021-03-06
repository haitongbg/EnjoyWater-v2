package com.enjoywater.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.enjoywater.R;
import com.enjoywater.activiy.MyApplication;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;
import com.enjoywater.model.User;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {
    private static Gson gson = new Gson();

    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo topProcessInfo = runningProcesses.get(0);
        if (topProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            if (topProcessInfo.processName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static String convertDateTimeToDateTime(String date, String timeZone, int old_format, int new_format) {
        String dateTimeReturn = "";
        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (old_format) {
            case 1: {
                oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                break;
            }
            case 2: {
                oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            }
            case 3: {
                oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                break;
            }
            case 4: {
                oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                break;
            }
            case 5: {
                oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                break;
            }
            case 6: {
                oldFormat = new SimpleDateFormat("HH:mm:ss.SS");
                break;
            }
            default:
                break;
        }
        oldFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (new_format) {
            case 1: {
                newFormat = new SimpleDateFormat("hh:mm a");
                break;
            }
            case 2: {
                newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            }
            case 3: {
                newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                break;
            }
            case 4: {
                newFormat = new SimpleDateFormat("dd/MM/yyyy KK:mm a");
                break;
            }
            case 5: {
                newFormat = new SimpleDateFormat("dd/MM/yyyy");
                break;
            }
            case 6: {
                newFormat = new SimpleDateFormat("dd.MM.yyyy");
                break;
            }
            case 7: {
                newFormat = new SimpleDateFormat("HH:mm:ss");
                break;
            }
            case 8: {
                newFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                break;
            }
            default:
                break;
        }
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String time = newFormat.format(oldFormat.parse(date));
            dateTimeReturn = time.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeReturn;
    }

    public static long convertDateTimeToTimeMillis(String date, int type_format) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (type_format) {
            case 1: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                break;
            }
            case 2: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            }
            case 3: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                break;
            }
            case 4: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                break;
            }
            case 5: {
                format = new SimpleDateFormat("yyyy-MM-dd");
                break;
            }
            default:
                break;
        }
        return format.parse(date).getTime();
    }

    public static String convertTimeMillisToDateTime(long timeMillis, int type_format) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (type_format) {
            case 1: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                break;
            }
            case 2: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            }
            case 3: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                break;
            }
            case 4: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                break;
            }
            case 5: {
                format = new SimpleDateFormat("yyyy-MM-dd");
                break;
            }
            case 6: {
                format = new SimpleDateFormat("dd/MM/yyyy");
                break;
            }
            default:
                break;
        }
        return format.format(new Date(timeMillis));
    }

    public static String converDateTimeToTimeAgo(String date, int type_format) {
        String dateTimeReturn = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (type_format) {
            case 1: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                break;
            }
            case 2: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            }
            case 3: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                break;
            }
            case 4: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                break;
            }
            case 5: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                break;
            }
            case 6: {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
                break;
            }
            default:
                break;
        }
        try {
            Date past = format.parse(date);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (seconds < 30) {
                dateTimeReturn = "Vừa xong";
            } else if (seconds < 60) {
                dateTimeReturn = seconds + " giây trước";

            } else if (minutes < 60) {
                dateTimeReturn = minutes + " phút  trước";
            } else if (hours < 24) {
                dateTimeReturn = hours + " giờ trước";
            } else {
                dateTimeReturn = days + " ngày trước";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeReturn;
    }

    public static String convertCountNumberToString(int number, String suffix) {
        String countCounverted = "";
        if (number < 1000) {
            countCounverted = number + "";
        } else if (number >= 1000 && number < 1000000) {
            countCounverted = number / 1000 + (number % 1000) / 100 + "K";
        } else if (number >= 1000000 && number < 1000000000) {
            countCounverted = number / 1000000 + (number % 1000000) / 1000 + "Tr";
        } else if (number >= 1000000000) {
            countCounverted = number / 1000000000 + "Tỉ";
        }
        if (!suffix.isEmpty()) countCounverted += " " + suffix;
        return countCounverted;
    }

    public static String convertVideoDurationToString(int milisecond) {
        int hour = (int) TimeUnit.MILLISECONDS.toHours(milisecond);
        int minute = (int) TimeUnit.MILLISECONDS.toMinutes(milisecond) % 60;
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(milisecond) % 60);
        if (hour > 0) {
            return String.format("%d:%02d:%02d", hour, minute, seconds);
        } else return String.format("%02d:%02d", minute, seconds);
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connec != null) {
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                return true;
            } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                return false;
            }
        }
        return false;
    }

    public static void saveString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    //Login
    public static void saveUser(Context context, User user) {
        EventBus.getDefault().post(new EventBusMessage(Constants.Key.PROFILE_UPDATED, user));
        saveString(context, Constants.Key.USER, gson.toJson(user));
    }

    public static User getUser(Context context) {
        String stringUser = getString(context, Constants.Key.USER, "");
        if (!stringUser.isEmpty()) {
            User user = gson.fromJson(stringUser, User.class);
            if (user != null && user.getToken() != null && !user.getToken().isEmpty() && user.getUserInfo() != null) {
                return user;
            }
        }
        return null;
    }

    public static String getToken(Context context) {
        String stringUser = getString(context, Constants.Key.USER, "");
        if (!stringUser.isEmpty()) {
            User user = gson.fromJson(stringUser, User.class);
            if (user != null && user.getToken() != null) return ("Bearer " + user.getToken());
        }
        return "";
    }

    public static String getRefreshToken(Context context) {
        return getString(context, Constants.Key.REFRESH_TOKEN, "");
    }

    public static void saveRefreshToken(Context context, String token) {
        saveString(context, Constants.Key.REFRESH_TOKEN, token);
    }

    public static void clearUser(Context context) {
        removeString(context, Constants.Key.USER);
        removeString(context, Constants.Key.REFRESH_TOKEN);
        MyApplication.getInstance().getGoogleSignInClient().signOut();
        LoginManager.getInstance().logOut();
    }
    //

    public static void removeString(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).commit();
    }

    public static void copyTextToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Đã lưu vào bộ nhớ.", Toast.LENGTH_SHORT).show();
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static void hideSoftKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidPhone(String phone) {
        String Name_PATTERN = "^(\\+84|0)[0-9]{9}$";
        Pattern pattern = Pattern.compile(Name_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidPassword(String password) {
        String ePattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isValidPasswordSimple(String password) {
        String ePattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static String genMD5Hash(String str) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.reset();
        m.update(str.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }

    public static boolean isWifiConnecting(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (manager != null && manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            return true;
        else return false;
    }

    public static void shareUrl(Context context, String application, String title, String url) {
        if (application != null && !application.isEmpty()) {
            /*if (application.equals(Constants.FACEBOOK_APP)) {
                ShareDialog shareDialog = new ShareDialog((Activity) context);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(url))
                            .build();
                    shareDialog.show(linkContent);
                }
            }*/
        } else {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, title != null ? title : "");
            share.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(share, "Chia sẻ"));
        }
    }

    public static ArrayList<City> getCities(Context context) {
        try {
            InputStream is = context.getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String data = new String(buffer, "UTF-8");
            if (!data.isEmpty()) {
                JSONArray arrayCity = new JSONArray(data);
                if (arrayCity.length() > 0) {
                    ArrayList<City> cities = new ArrayList<>();
                    for (int i = 0, z = arrayCity.length(); i < z; i++) {
                        if (arrayCity.get(i) instanceof JSONObject) {
                            JSONObject objectCity = arrayCity.getJSONObject(i);
                            City city = gson.fromJson(objectCity.toString(), City.class);
                            if (city != null && city.getName() != null && !city.getName().isEmpty())
                                cities.add(city);
                        }
                    }
                    return cities;
                }
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    public static String getFullAddress(String cityId, String districtId, String wardId, String addressDetail) {
        ArrayList<City> cities = MyApplication.getInstance().getCities();
        String fullAddress = "";
        fullAddress += addressDetail;
        if (cityId != null && !cityId.isEmpty()) {
            for (City city : cities) {
                if (city.getId() != null && city.getId().equals(cityId) && city.getName() != null && !city.getName().isEmpty()) {
                    ArrayList<District> districts = city.getDistricts();
                    if (districts != null && !districts.isEmpty() && districtId != null && !districtId.isEmpty()) {
                        for (District district : districts) {
                            if (district.getId() != null && district.getId().equals(districtId) && district.getName() != null && !district.getName().isEmpty()) {
                                ArrayList<Ward> wards = district.getWards();
                                if (wards != null && !wards.isEmpty() && wardId != null && !wardId.isEmpty()) {
                                    for (Ward ward : wards) {
                                        if (ward.getId() != null && !ward.getId().equals(wardId) && ward.getName() != null && !ward.getName().isEmpty()) {
                                            if (ward.getType() != null && !ward.getType().isEmpty())
                                                fullAddress += (", " + ward.getType() + " " + ward.getName());
                                            else fullAddress += (", " + ward.getName());
                                            break;
                                        }
                                    }
                                }
                                if (district.getType() != null && !district.getType().isEmpty())
                                    fullAddress += (", " + district.getType() + " " + district.getName());
                                else fullAddress += (", " + district.getName());
                                break;
                            }
                        }
                    }
                    if (city.getType() != null && !city.getType().isEmpty())
                        fullAddress += (", " + city.getType() + " " + city.getName());
                    else fullAddress += (", " + city.getName());
                    break;
                }
            }
        }
        return fullAddress;
    }

    public static String getDeviceUuid(Context context) {
        String id = Utils.getString(context, Constants.Key.DEVICE_ID, "");
        if (id.isEmpty()) {
            UUID uuid;
            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            // Use the Android ID unless it's broken, in which case
            // fallback on deviceId,
            // unless it's not available, then fallback on a random
            // number which we store to a prefs file
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                } else {
                    @SuppressLint({"MissingPermission", "HardwareIds"}) String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            // Write the value out to the prefs file
            Utils.saveString(context, Constants.Key.DEVICE_ID, uuid.toString());
            return uuid.toString();
        } else {
            return id;
        }
    }

    public static String getDeviceToken(Context context) {
        String deviceToken = Utils.getString(context, Constants.Key.DEVICE_TOKEN, "");
        if (deviceToken.isEmpty()) {
            deviceToken = FirebaseInstanceId.getInstance().getToken();
            Utils.saveString(context, Constants.Key.DEVICE_TOKEN, deviceToken);
            return deviceToken;
        }
        return deviceToken;
    }

    public static void loadImage(Context context, ImageView imageView, String url) {
        if (url != null && url.length() > 0) {
            Glide.with(context).load(url)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(RequestOptions.errorOf(R.drawable.logo_app))
                    .apply(RequestOptions.placeholderOf(R.drawable.bg_placeholder))
                    .thumbnail(0.1f)
                    .into(imageView);

        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.logo_app));
        }
    }
}
