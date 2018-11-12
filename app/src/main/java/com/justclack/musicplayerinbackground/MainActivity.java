package com.justclack.musicplayerinbackground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = new ListView(this);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getData()));
        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Intent it0 = new Intent(MainActivity.this, Audio_Record.class);
                                startActivity(it0);
                                break;
                            case 1:
                                Intent it1 = new Intent(MainActivity.this, AudioPlay.class);
                                startActivity(it1);
                                break;
                            case 2:
                                Intent it2 = new Intent(MainActivity.this, LastingService.class);
                                startActivity(it2);
                                break;
                        }
                    }
                }
        );
        setContentView(mListView);
    }



    public ArrayList<String> getData() {
        ArrayList<String> ALS = new ArrayList<>();
        ALS.add("Audio Record");
        ALS.add("Audio Play");
        ALS.add("Audio with service");
        return ALS;
    }
}