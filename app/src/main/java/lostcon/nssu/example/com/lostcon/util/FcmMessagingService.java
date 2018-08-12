package lostcon.nssu.example.com.lostcon.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.activity.MainActivity;
import lostcon.nssu.example.com.lostcon.common.Constants;

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        if(remoteMessage.getNotification() != null)
        {

            Map<String, String> pushData = remoteMessage.getData();
            JSONObject json = new JSONObject(pushData);

            Log.d("fcm", json.toString());
            try {
                Log.d("fcm", json.getString("portNum"));
                Log.d("fcm", json.getInt("portNum") + "!!!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*Observable.just(Constants.flag)
                    .subscribe(value -> sendNotification(remoteMessage.getNotification().getBody()) );*/
            try {
                sendNotification(remoteMessage.getNotification().getBody(), json.getInt("portNum"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    private void sendNotification(Map<String, String> data)
    {

    }

    private void sendNotification(String body, int port)
    {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.DATA_PORT, port);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent
                    ,PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_channel_id);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("요기요")
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(uri)
                            .setVibrate(new long[]{1000,1000})
                            .setLights(Color.WHITE,1500,1500)
                            .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelName = getString(R.string.default_channel_name);
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH); //알림의 중요도
                manager.createNotificationChannel(channel);
            }
            manager.notify(0,builder.build());




        /*Log.d("fcm","메세지 옴");
        Intent intent2 = new Intent(this,MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent2);*/
    }

}
