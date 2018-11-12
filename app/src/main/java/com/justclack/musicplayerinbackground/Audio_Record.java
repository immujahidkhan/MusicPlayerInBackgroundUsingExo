package com.justclack.musicplayerinbackground;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Bob-JIANG on 2016/3/16.
 */
public class Audio_Record extends AppCompatActivity {
    /*
        To capture an audio, here are the steps:
        1 Create a new instance of android.media.MediaRecorder.
        2 Set the audio source using MediaRecorder.setAudioSource(). You will probably want to use
        MediaRecorder.AudioSource.MIC.
        3 Set output file format using MediaRecorder.setOutputFormat().
        4 Set output file name using MediaRecorder.setOutputFile().
        5 Set the audio encoder using MediaRecorder.setAudioEncoder().
        6 Call MediaRecorder.prepare() on the MediaRecorder instance.
        7 To start audio capture, call MediaRecorder.start().
        8 To stop audio capture, call MediaRecorder.stop().
        9 When you are done with the MediaRecorder instance, call MediaRecorder.release() on it.
        Calling MediaRecorder.release() is always recommended to free the resource immediately.
     */

    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/audiorecordtest.3gp";
    ;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;


    //the operation for mediarecorder
    private void onRecord(boolean start) {
        if(start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    //the operation for mediaplayer
    private void onPlay(boolean start) {
        if(start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {}
    };

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {}
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    //a convenient way to achieve the result, notice the use of boolean variable here
    class RecordButton extends AppCompatButton {
        boolean mStartRecording = true;
        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if(mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends AppCompatButton {
        boolean mStartPlaying = true;
        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if(mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //Here is what you have to do for Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}, 29);
            } else {
                Log.d("Home", "Already granted access");
            }
        }
        LinearLayout ll = new LinearLayout(this);
        RecordButton mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        PlayButton mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        setContentView(ll);
    }

    public void onPause() {
        super.onPause();
        if(mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 29: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Home", "Permission Granted");
                } else {
                    Log.d("Home", "Permission Failed");
                    Toast.makeText(this.getBaseContext(), "You must allow permission record " +
                            "audio to your mobile device.", Toast.LENGTH_SHORT).show();
                }
            }
            // Add additional cases for other permissions you may have asked for
        }
    }
}