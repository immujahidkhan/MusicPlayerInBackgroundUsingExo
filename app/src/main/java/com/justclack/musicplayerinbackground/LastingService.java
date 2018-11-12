package com.justclack.musicplayerinbackground;


import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class LastingService extends AppCompatActivity {
    MediaPlayer mp = null;
    private Uri uriSound = null;
    static int fin = 0;
    static int finfore = 0;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 10) {
            uriSound = data.getData();
        }
    }

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.lastingservice);
        final Button mlocalButton = (Button)findViewById(R.id.button4);
        final Button mForegroundButton = (Button)findViewById(R.id.button5);
        switch(fin % 3) {
            case 0:
                mlocalButton.setText("Select local music");
                break;
            case 1:
                mlocalButton.setText("Start playing local music");
                break;
            case 2:
                mlocalButton.setText("Stop play local music");
        }

        mlocalButton.setOnClickListener(new View.OnClickListener() {
            int local = 0;
            @Override
            public void onClick(View v) {
                Intent serviceIntent = null;
                switch(fin++ % 3) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 10);
                        mlocalButton.setText("Start playing local music");
                        break;
                    case 1:
                        try {
                            serviceIntent = new Intent(getApplicationContext(),
                                    LocalPlayerService2.class);
                        }
                        catch(NullPointerException e) {}
                        // add infos for the service which file to download and where to store
                        serviceIntent.setData(uriSound);
                        startService(serviceIntent);
                        Log.d("Service started?", "Yes!");
                        mlocalButton.setText("Stop play local music");
                        break;
                    case 2:
                        try {
                            stopService(new Intent(getApplicationContext(), LocalPlayerService2.class));
                        } catch(Exception e) {}
                        mlocalButton.setText("Select local music");
                }
            }
        });

        mForegroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = null;
                switch(finfore++ % 3) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 10);
                        mForegroundButton.setText("Start playing local music with service");
                        break;
                    case 1:
                        try {
                            serviceIntent = new Intent(getApplicationContext(),
                                    LocalPlayerService.class);
                        }
                        catch(NullPointerException e) {}
                        // add infos for the service which file to download and where to store
                        serviceIntent.setData(uriSound);
                        serviceIntent.setAction("start foreground");
                        startService(serviceIntent);
                        Log.e("Foreground started?", "Yes!");
                        mForegroundButton.setText("Stop play local music with service");
                        break;
                    case 2:
                        try {
                            stopService(new Intent(getApplicationContext(), LocalPlayerService.class));
                        } catch(Exception e) {}
                        mForegroundButton.setText("Select local music with service");
                }
            }
        });
    }
}
