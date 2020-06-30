package com.example.a2020fantasyfootallsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent getSel = getIntent();
        String selection = getSel.getExtras().getString("sel");
        final Integer num = Integer.valueOf(selection);

        ArrayList<Team> teamList = new ArrayList<Team>();

        Random r = new Random();
        int playerNum = r.nextInt((num - 1) + 1) + 1;

        for(int i = 1; i < playerNum; i++) {
            String n = "Team" + Integer.toString(i);
            Team t = new Team(n);
            teamList.add(t);
        }

        Team p = new Team("Player");
        teamList.add(p);

        for(int i = playerNum; i < num; i++) {
            String n = "Team" + Integer.toString(i);
            Team t = new Team(n);
            teamList.add(t);
        }

        for(int i = 0; i < teamList.size(); i++) {
            for(int j = 0; j < 13; j+=2) {
                teamList.get(i).getPicks().add(j * num + (i+1));
                teamList.get(i).getPicks().add(j * num + 2 * num - (i));
            }
        }

        ArrayList<Player> playerList = new ArrayList<Player>();

        InputStream is = getResources().openRawResource(R.raw.ff_data);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        try {
            for (String line; (line = br.readLine()) != null; ) {
                String [] info = line.split(",");
                int rank = Integer.valueOf(info[0]);
                String pos = info[1];
                if(pos.substring(0,1).equals("k")) {
                    pos = pos.substring(0,1);
                }
                else if(pos.substring(0,1).equals("d") || pos.substring(0,1).equals("i")) {
                    pos = pos.substring(0,3);
                }
                else
                    pos = pos.substring(0,2);
                String name = info[2];
                double proj = Double.valueOf(info[3]);
                String team = info[4];
                int bye = Integer.valueOf(info[5]);
                playerList.add(new Player(name, team, pos, proj, bye, rank));
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }


    }
}