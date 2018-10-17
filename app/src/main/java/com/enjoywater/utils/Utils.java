package com.enjoywater.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {
    public static String convertDateTimeToDateTime(String date, int old_format, int new_format) {
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
        try {
            String time = newFormat.format(oldFormat.parse(date));
            dateTimeReturn = time.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeReturn;
    }

    public static long convertDateTimeToTimeStamp(String date, int type_format) {
        long dateTimeReturn = 0;
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
            default:
                break;
        }
        try {
            Date past = format.parse(date);
            dateTimeReturn = past.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeReturn;
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
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static void saveString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

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
}
