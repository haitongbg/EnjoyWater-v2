package com.enjoywater.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.enjoywater.R;
import com.enjoywater.activiy.OrderDetailsActivity;
import com.enjoywater.activiy.SplashActivity;
import com.enjoywater.model.EventBusMessage;
import com.enjoywater.model.MyNotification;
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
                MyNotification notification = gson.fromJson(data, MyNotification.class);
                if (notification != null) {
                    createNotification(notification);
                    switch (notification.getType()) {
                        case Constants.Value.ORDER: {
                            EventBus.getDefault().post(new EventBusMessage(Constants.Key.ORDER_UPDATED, notification.getContent()));
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void createNotification(MyNotification notification) {
        Intent intent;
        switch (notification.getType()) {
            case Constants.Value.ORDER: {
                intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra("order_id", notification.getContent());
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
