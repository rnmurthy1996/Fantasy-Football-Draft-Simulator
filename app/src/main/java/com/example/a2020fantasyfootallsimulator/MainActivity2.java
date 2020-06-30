package com.example.a2020fantasyfootallsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

    public static ArrayList<Team> teamList = new ArrayList<Team>();
    public static ArrayList<Player> playerList = new ArrayList<Player>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent getSel = getIntent();
        String selection = getSel.getExtras().getString("sel");
        final Integer num = Integer.valueOf(selection);



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

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 1;
            public void run() {

                if (count < num * 14 + 1) {
                    String[] arraySpinner = new String[playerList.size()];
                    for(int i = 0; i < playerList.size(); i++) {
                        arraySpinner[i] = playerList.get(i).getName() + "---" + playerList.get(i).getPosition() + "---" + Double.toString(playerList.get(i).getProjPoints());
                    }
                    final Spinner s = (Spinner) findViewById(R.id.availPlayers);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity2.this,
                            android.R.layout.simple_spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(adapter);

                    TableLayout draft = (TableLayout)findViewById(R.id.tableLayout);
                    TableRow tr =  new TableRow(MainActivity2.this);
                    tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    TextView c1 = new TextView(MainActivity2.this);
                    c1.setText("Pick #" + Integer.toString(count) + "   ");
                    c1.setTextSize(20);
                    TextView c2 = new TextView(MainActivity2.this);
                    String team = "";
                    for(int k = 0; k < teamList.size(); k++) {
                        if(teamList.get(k).getPicks().contains(count))
                            team = teamList.get(k).getName();
                    }
                    c2.setText(team + "   ");
                    c2.setTextSize(20);

                    TextView c3 = new TextView(MainActivity2.this);
                    Random rn = new Random();
                    int rand = rn.nextInt(2 - 0 + 1) + 0;
                    Player pl = playerList.remove(rand);
                    c3.setText(pl.getName());
                    c3.setTextSize(20);

                    tr.addView(c1);
                    tr.addView(c2);
                    tr.addView(c3);
                    draft.addView(tr);
                    count++;

                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

}

