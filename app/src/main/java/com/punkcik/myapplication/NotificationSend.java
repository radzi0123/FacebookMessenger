package com.punkcik.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashMap;
import java.util.Map;

public class NotificationSend {

    private static final String CHANNEL_ID = "my_channel";
    private static final String GROUP_KEY_MESSAGES = "group_key_messages";

    private final Context mContext;
    private final Map<String, NotificationCompat.Builder> mNotificationBuilders;

    public NotificationSend(Context context) {
        mContext = context;
        mNotificationBuilders = new HashMap<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void sendNotification(StatusBarNotification sbn) {

        Notification notification = sbn.getNotification();
        String tag = sbn.getTag();

        // Check if the notification is a message from the Messenger app
        if (tag != null && tag.contains("com.facebook.orca")) {

            // Get the notification extras
            String title = notification.extras.getString(NotificationCompat.EXTRA_TITLE);
            String text = notification.extras.getString(NotificationCompat.EXTRA_TEXT);
            Bitmap largeIcon = notification.extras.getParcelable(NotificationCompat.EXTRA_LARGE_ICON);
            int notificationId = sbn.getId();

            // Create a new notification builder or update an existing one
            NotificationCompat.Builder builder;
            if (mNotificationBuilders.containsKey(tag)) {
                builder = mNotificationBuilders.get(tag);
            } else {
                builder = createNotificationBuilder(title, text, largeIcon);
                mNotificationBuilders.put(tag, builder);
            }

            // Add the new message to the notification
            NotificationCompat.MessagingStyle messagingStyle = createMessagingStyle(title, text);
            messagingStyle.addMessage(text, System.currentTimeMillis(), "Sender");
            builder.setStyle(messagingStyle);

            // Update the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            notificationManager.notify(tag, notificationId, builder.build());
        }
    }

    private NotificationCompat.Builder createNotificationBuilder(String title, String text, Bitmap largeIcon) {

        // Create the notification intent
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY_MESSAGES)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // Set the messaging style for the notification
        NotificationCompat.MessagingStyle messagingStyle = createMessagingStyle(title, text);
        builder.setStyle(messagingStyle);

        // Return the builder
        return builder;
    }

    private NotificationCompat.MessagingStyle createMessagingStyle(String title, String text) {

        // Create the messaging style
        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle("Sender");

        // Add the message to the messaging style
        NotificationCompat.MessagingStyle.Message message =
                new NotificationCompat.MessagingStyle.Message(
                        text,
                        System.currentTimeMillis(),
                        "Sender");
        messagingStyle.addMessage(message);

        // Set the conversation title
        messagingStyle.setConversationTitle(title);

        return messagingStyle;
    }
}
