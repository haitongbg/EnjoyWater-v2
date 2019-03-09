package com.enjoywater.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.enjoywater.R;
import com.enjoywater.activiy.BonusHistoryActivity;
import com.enjoywater.activiy.NewsDetailsActivity;
import com.enjoywater.activiy.OrderDetailsActivity;
import com.enjoywater.activiy.SplashActivity;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.Notify;
import com.enjoywater.utils.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFCMs";
    private Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "onMessageReceived " + remoteMessage.getData().get(Constants.Key.DATA));
            String data = remoteMessage.getData().get(Constants.Key.DATA);
            if (data != null && !data.isEmpty()) {
                Notify notification = gson.fromJson(data, Notify.class);
                if (notification != null) {
                    createNotification(notification);
                    switch (notification.getType()) {
                        case Constants.Value.ORDER: {
                            EventBus.getDefault().post(new EventBusMessage(Constants.Key.ORDER_UPDATED, notification));
                            break;
                        }
                        case Constants.Value.BONUS: {
                            EventBus.getDefault().post(new EventBusMessage(Constants.Key.BONUS_UPDATED, notification));
                            break;
                        }
                        case Constants.Value.NEWS: {
                            EventBus.getDefault().post(new EventBusMessage(Constants.Key.NEWS_UPDATED, notification));
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void createNotification(Notify notification) {
        Intent intent;
        switch (notification.getType()) {
            case Constants.Value.ORDER: {
                intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(Constants.Key.ORDER_ID, Integer.parseInt(notification.getContent()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            }
            case Constants.Value.NEWS: {
                intent = new Intent(this, NewsDetailsActivity.class);
                intent.putExtra(Constants.Key.NEWS_ID, Integer.parseInt(notification.getContent()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            }
            case Constants.Value.BONUS: {
                intent = new Intent(this, BonusHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            }
            default: {
                intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            }
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notification.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.my_notif_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notification.getId(), builder.build());
    }
}
