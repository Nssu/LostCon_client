package lostcon.nssu.example.com.lostcon.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.Collections;
import java.util.List;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.activity.MainActivity;
import lostcon.nssu.example.com.lostcon.common.Constants;

public class BeaconService extends Service {

    private RemoteViews remoteViews;
    private Notification notification;
    private MinewBeaconManager beaconManager;
    private UserRssi userRssi;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        Log.d("beacon_serv","onCreate()");
        userRssi = new UserRssi();
        beaconManager = MinewBeaconManager.getInstance(this);
        initListener();
    }


    private void initListener()
    {
        beaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {

            /**
             *   if the manager find some new beacon, it will call back this method.
             *
             *  @param list  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<MinewBeacon> list) {
                Log.d("beacon_serv","새로운 비콘 받음");
            }

            /**
             *  if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
             *
             *  @param list beacons out of range
             */
            @Override
            public void onDisappearBeacons(List<MinewBeacon> list) {
                Log.d("beacon_serv","비콘이 감지가 안 될 때");
            }

            /**
             *  the manager calls back this method every 1 seconds, you can get all scanned beacons.
             *
             *  @param list all scanned beacons
             */
            @Override
            public void onRangeBeacons(List<MinewBeacon> list) {
                Log.d("beacon_serv","onRange()");
                Collections.sort(list, userRssi);
                int cnt = 0;
                if(!list.isEmpty())
                {
                    for(MinewBeacon m : list)
                    {
                       if(m.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue()
                               .equals(Constants.UUID))
                       {
                           cnt = 1;
                           break;
                       }
                    }
                }
                if(cnt == 0)
                {
                    Intent intent = new Intent("service");
                    intent.putExtra(Constants.BEC_ITEM,"벗어남");
                    sendBroadcast(intent);
                }
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!list.isEmpty())
                        {
                            Log.d("main_at","Name : " +list.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue());
                            Log.d("main_at","Name : " +list.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue());
                            Log.d("main_at","Name : " +list.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue());
                        }
                    }
                });*/

            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {

            }
        });
        beaconManager.startScan();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        Intent serviceIntent = new Intent(this, MainActivity.class);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,serviceIntent
        ,PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_channel_id);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notification = new NotificationCompat.Builder(this, channelId)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setWhen(0)
                        .build();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelName = getString(R.string.default_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE); //알림의 중요도
            notificationManager.createNotificationChannel(channel);
        }

        startForeground(1,notification);
        return START_NOT_STICKY;
    }


}
