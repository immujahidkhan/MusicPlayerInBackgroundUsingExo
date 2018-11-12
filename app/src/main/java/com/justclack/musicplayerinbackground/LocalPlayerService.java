package com.justclack.musicplayerinbackground;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;


public class LocalPlayerService extends Service {
    MediaPlayer mp = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("start foreground")) {
            Intent notificationIntent = new Intent(this, LastingService.class);
            notificationIntent.setAction("notification intent");
            //??
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //the intent we use to go back to mainactivity
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.timg);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Now Playing")
                    .setContentText("I don't know the song name...")
                    .setSmallIcon(R.drawable.timg)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true).build();
            startForeground(101, notification);

            Uri uriSound = intent.getData();
            try {
                mp = new MediaPlayer();
                mp.setDataSource(this, uriSound);
                mp.prepare();
                mp.start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("foreground service", "In onDestroy");
        if(mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}
