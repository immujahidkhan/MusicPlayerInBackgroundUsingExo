package com.justclack.musicplayerinbackground;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import java.io.IOException;

public class LocalPlayerService2 extends Service {
    MediaPlayer mp = null;

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "Is Started");
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
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        if(mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
