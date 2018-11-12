package com.justclack.musicplayerinbackground;


import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

/**
 * Created by Bob-JIANG on 2016/3/17.
 */
public class AudioPlay extends AppCompatActivity {
    private MediaPlayer mp = null;
    private Uri uriSound = null;
    static int fin = 0;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 10) {
            uriSound = data.getData();
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = { MediaStore.Audio.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.audioplay);
        final Button mrawButton = (Button)findViewById(R.id.button);
        final Button mlocalButton = (Button)findViewById(R.id.button2);
        final Button mremoteButton = (Button)findViewById(R.id.button3);
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
        mrawButton.setOnClickListener(new View.OnClickListener() {
            boolean doPlay = false;
            @Override
            public void onClick(View v) {
                doPlay = !doPlay;
                if(doPlay) {
                    mrawButton.setText("Stop play built-in music");
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.audio);
                    mp.start();
                }
                else {
                    mrawButton.setText("Play bulit-in music");
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }
        });
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
                        mp = new MediaPlayer();
                        //mp = MediaPlayer.create()
                        try {
                            mp.setDataSource(getApplicationContext(), uriSound);
                            mp.prepare();
                        } catch(IOException e) {}
                        mp.start();
                        mlocalButton.setText("Stop play local music");
                        break;
                    case 2:
                        try {
                            stopService(new Intent(getApplicationContext(), LocalPlayerService2.class));
                        } catch(Exception e) {}
                        /*mp.stop();
                        mp.release();
                        mp = null; */
                        mlocalButton.setText("Select local music");
                }
            }
        });
        mremoteButton.setOnClickListener(new View.OnClickListener() {
            boolean doPlay = false;

            @Override
            public void onClick(View v) {
                doPlay = !doPlay;
                if (doPlay) {
                    mremoteButton.setText("Stop play remote content");
                    String url = "dl.last" +
                            ".fm/static/1458290724/131211148/79a9b943269236bd086103112718a4ac843214427acbc66b3568d72d8f8394d7" +
                            "/Death+Grips+-+Get+Got.mp3"; // your URL here
                    mp = new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        //mp = MediaPlayer.create()
                        mp.setDataSource(url);
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    mremoteButton.setText("Play remote content");
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }
        });
    }

}