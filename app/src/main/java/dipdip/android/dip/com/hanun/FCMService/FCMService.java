package dipdip.android.dip.com.hanun.FCMService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import dipdip.android.dip.com.hanun.MainActivity;
import dipdip.android.dip.com.hanun.NotifikasiActivity;
import dipdip.android.dip.com.hanun.R;

public class FCMService extends FirebaseMessagingService {
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("from_user_id");
        message=remoteMessage.getData().get("message");

        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("from_user_id");
            message = remoteMessage.getData().get("message");
        }

        String click_action = remoteMessage.getNotification().getClickAction();
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), title, message, click_action);


    }

    private void sendNotification(String messageBody, String messageTitle, String message, String title, String click_action) {
        Intent intent = new Intent(click_action);
        intent.putExtra("from_user_id", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.iconsepeda)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}