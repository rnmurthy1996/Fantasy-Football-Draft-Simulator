//they go first: they get null errors for players

package com.rnmfantasyapp.a2020fantasyfootallsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    public static ArrayList<Team> teamList = new ArrayList<Team>();
    public static ArrayList<Player> playerList = new ArrayList<Player>();
    //public static ArrayList<Player> possiblePicks = new ArrayList<Player>();
    public static String buttonVal;
    public static int count;
    public static Team currentTeam;
    public static int removeCount;

    public static String[] arraySpinner;
    public static Spinner s;
    public static ScrollView scroll;
    public static int num;
    public static int round = 0;
    public static boolean justPicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent getSel = getIntent();
        String selection = getSel.getExtras().getString("sel");
        num = Integer.valueOf(selection);

        removeCount = 0;

        Random r = new Random();
        int playerNum = r.nextInt((num - 1) + 1) + 1;

        for(int i = 1; i < playerNum; i++) {
            String n = "Team" + Integer.toString(i);
            Team t = new Team(n);
            teamList.add(t);
        }

        Team p = new Team("User");
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
                if(pos.substring(0,1).equals("K")) {
                    pos = pos.substring(0,1);
                }
                else if(pos.substring(0,1).equals("D") || pos.substring(0,1).equals("I")) {
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

        for(int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).getPosition().equals("IDP")) {
                playerList.set(i, null);
            }
        }
        playerList.removeAll(Collections.singleton(null));

        count = 1;
        updateSpinner();

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            public void run() {
                if (count < num * 13 + 1) {
                    Button btn = (Button) findViewById(R.id.draftButton);
                    btn.setEnabled(false);
                    String team = "";
                    for(int k = 0; k < teamList.size(); k++) {
                        if(teamList.get(k).getPicks().contains(count))
                            team = teamList.get(k).getName();
                    }

                    for(int k = 0; k < teamList.size(); k++) {
                        if(teamList.get(k).getName().equals("User"))
                            currentTeam = teamList.get(k);
                    }

                    if(!team.equals("User")) {
                        handler.postDelayed(this, 1000);
                        makePick(team, count);
                        count++;
                        if(count == num * 13 + 1) {
                            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                            startActivity(intent);
                            round = 0;
                            finish();
                        }
                    }

                    else {
                        btn.setEnabled(true);
                        handler.postDelayed(this, 2000);
                    }

                }

            }
        };
        handler.post(runnable);

        Button btn = (Button) findViewById(R.id.draftButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String team = "";
                Team myTeam = currentTeam;
                for(int k = 0; k < teamList.size(); k++) {
                    if(teamList.get(k).getName().equals("User"))
                        team = teamList.get(k).getName();
                }


                buttonVal = s.getSelectedItem().toString();

                TableLayout draft = (TableLayout) findViewById(R.id.tableLayout);
                TableRow tr = new TableRow(MainActivity2.this);
                tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView c1 = new TextView(MainActivity2.this);
                c1.setText("Pick #" + Integer.toString(count) + "   ");
                c1.setTextSize(16);
                TextView c2 = new TextView(MainActivity2.this);

                c2.setText(team + "   ");
                c2.setTextSize(16);

                TextView c3 = new TextView(MainActivity2.this);
                String[] play = buttonVal.split("---");
                String name = play[0];
                Player pl = new Player("null", "null", "null", 0 ,0, 0);
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getName().equals(name)) {
                        pl = playerList.remove(i);
                        removeCount++;
                    }
                }
                setPlayer(myTeam, pl);
                for(int i = 0; i < teamList.size(); i++) {
                    if(teamList.get(i).getName().equals("User")) {
                        teamList.set(i, myTeam);
                    }
                }
                c3.setText(pl.getName() + "   ");
                c3.setTextSize(16);

                TextView c4 = new TextView(MainActivity2.this);
                c4.setText(pl.getPosition());
                c4.setTextSize(16);

                c1.setTextColor(Color.GREEN);
                c2.setTextColor(Color.GREEN);
                c3.setTextColor(Color.GREEN);
                c4.setTextColor(Color.GREEN);

                tr.addView(c1);
                tr.addView(c2);
                tr.addView(c3);
                tr.addView(c4);
                draft.addView(tr);
                count++;

                final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView3);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

                v.setEnabled(false);

                if(count == num * 13 + 1) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                    startActivity(intent);
                    round = 0;
                    finish();
                }
            }
        });

    }

    public void makePick(String t, int c) {

        TableLayout draft = (TableLayout)findViewById(R.id.tableLayout);
        TableRow tr =  new TableRow(MainActivity2.this);
        tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView c1 = new TextView(MainActivity2.this);
        c1.setText("Pick #" + Integer.toString(c) + "   ");
        c1.setTextSize(16);
        TextView c2 = new TextView(MainActivity2.this);

        c2.setText(t + "   ");
        c2.setTextSize(16);

        Team team = null;
        for(int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).getName().equals(t)) {
                team = teamList.get(i);
            }
        }

        TextView c3 = new TextView(MainActivity2.this);
        Player pl = new Player("null", "null", "null", 0 ,0, 0);

        if((double)(c)/(double)(num * 13) < 0.31) {
            Random rn = new Random();
            int rand = rn.nextInt(2 - 0 + 1) + 0;
            pl = playerList.remove(rand);
            removeCount++;
        }
        else if((double)(c)/(double)(num * 13) < 0.39) {
            if(team.getRb1() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("RB")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.465) {
            if(team.getRb2() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("RB")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.54) {
            if(team.getWr1() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("WR")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.62) {
            if(team.getWr2() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("WR")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.695) {
            if(team.getQb() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("QB")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.77) {
            if(team.getWr3() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("WR")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.85) {
            if(team.getTe() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("TE")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else if((double)(c)/(double)(num * 13) < 0.925) {

            if(team.getDst() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("DEF")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }
        else {
            if(team.getK() == null) {
                for(int i = 0; i < playerList.size(); i++) {
                    if(playerList.get(i).getPosition().equals("K")) {
                        pl = playerList.remove(i);
                        removeCount++;
                        break;
                    }
                }
            }
            else {
                Random rn = new Random();
                int rand = rn.nextInt(2 - 0 + 1) + 0;
                pl = playerList.remove(rand);
                removeCount++;
            }
        }

        setPlayer(team, pl);
        for(int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).getName().equals(team.getName())) {
                teamList.set(i, team);
            }
        }
        c3.setText(pl.getName() + "   ");
        c3.setTextSize(16);

        TextView c4 = new TextView(MainActivity2.this);
        c4.setText(pl.getPosition());
        c4.setTextSize(16);

        tr.addView(c1);
        tr.addView(c2);
        tr.addView(c3);
        tr.addView(c4);
        draft.addView(tr);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView3);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void updateSpinner() {
        if(num == 1) {
            if(round == 0) {
                round++;
            }
            else {
                if(round != 1) {
                    TableLayout draft = (TableLayout) findViewById(R.id.tableLayout);
                    TableRow tr1 = new TableRow(MainActivity2.this);
                    tr1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    TextView c2 = new TextView(MainActivity2.this);
                    c2.setText("");
                    c2.setTextSize(16);
                    tr1.addView(c2);
                    draft.addView(tr1);
                }

                TableLayout draft = (TableLayout) findViewById(R.id.tableLayout);
                TableRow tr = new TableRow(MainActivity2.this);
                tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView c1 = new TextView(MainActivity2.this);
                c1.setText("Round " + Integer.toString(round) + ":");
                c1.setTextSize(16);
                tr.addView(c1);
                draft.addView(tr);
                round++;
            }
        }
        else if(count % num == 1) {
            if(round == 0) {
                round++;
            }
            else {
                if(round != 1) {
                    TableLayout draft = (TableLayout) findViewById(R.id.tableLayout);
                    TableRow tr1 = new TableRow(MainActivity2.this);
                    tr1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    TextView c2 = new TextView(MainActivity2.this);
                    c2.setText("");
                    c2.setTextSize(16);
                    tr1.addView(c2);
                    draft.addView(tr1);
                }

                TableLayout draft = (TableLayout) findViewById(R.id.tableLayout);
                TableRow tr = new TableRow(MainActivity2.this);
                tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView c1 = new TextView(MainActivity2.this);
                c1.setText("Round " + Integer.toString(round) + ":");
                c1.setTextSize(16);
                tr.addView(c1);
                draft.addView(tr);
                round++;
            }


        }


        ArrayList <Player> possiblePicks = new ArrayList<Player>(playerList);

        for(int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).getName().equals("User")) {
                if(teamList.get(i).getRb1() != null && teamList.get(i).getRb2() != null  && teamList.get(i).getBn1() != null && teamList.get(i).getBn2() != null
                        && teamList.get(i).getBn3() != null && teamList.get(i).getBn4() != null) {
                    for(int j = 0; j < possiblePicks.size(); j++) {
                        if(possiblePicks.get(j).getPosition().equals("RB")) {
                            possiblePicks.set(j, null);
                        }
                    }

                    possiblePicks.removeAll(Collections.singleton(null));
                }

                if(teamList.get(i).getWr1() != null && teamList.get(i).getWr2() != null && teamList.get(i).getWr3() != null  && teamList.get(i).getBn1() != null
                        && teamList.get(i).getBn2() != null && teamList.get(i).getBn3() != null && teamList.get(i).getBn4() != null) {
                    for(int j = 0; j < possiblePicks.size(); j++) {
                        if(possiblePicks.get(j).getPosition().equals("WR")) {
                            possiblePicks.set(j, null);
                        }
                    }
                    possiblePicks.removeAll(Collections.singleton(null));
                }

                if(teamList.get(i).getQb() != null && teamList.get(i).getBn1() != null
                        && teamList.get(i).getBn2() != null && teamList.get(i).getBn3() != null && teamList.get(i).getBn4() != null) {
                    for(int j = 0; j < possiblePicks.size(); j++) {
                        if(possiblePicks.get(j).getPosition().equals("QB")) {
                            possiblePicks.set(j, null);
                        }
                    }
                    possiblePicks.removeAll(Collections.singleton(null));
                }

                if(teamList.get(i).getTe() != null && teamList.get(i).getBn1() != null
                        && teamList.get(i).getBn2() != null && teamList.get(i).getBn3() != null && teamList.get(i).getBn4() != null) {
                    for(int j = 0; j < possiblePicks.size(); j++) {
                        if(possiblePicks.get(j).getPosition().equals("TE")) {
                            possiblePicks.set(j, null);
                        }
                    }
                    possiblePicks.removeAll(Collections.singleton(null));
                }

                if(teamList.get(i).getDst() != null && teamList.get(i).getBn1() != null
                        && teamList.get(i).getBn2() != null && teamList.get(i).getBn3() != null && teamList.get(i).getBn4() != null) {
                    for(int j = 0; j < possiblePicks.size(); j++) {
                        if(possiblePicks.get(j).getPosition().equals("DEF")) {
                            possiblePicks.set(j, null);
                        }
                    }
                    possiblePicks.removeAll(Collections.singleton(null));
                }

                if(teamList.get(i).getK() != null && teamList.get(i).getBn1() != null
                        && teamList.get(i).getBn2() != null && teamList.get(i).getBn3() != null && teamList.get(i).getBn4() != null) {
                    for(int j = 0; j < possiblePicks.size(); j++) {
                        if(possiblePicks.get(j).getPosition().equals("K")) {
                            possiblePicks.set(j, null);
                        }
                    }
                    possiblePicks.removeAll(Collections.singleton(null));
                }
            }
        }
        arraySpinner = new String[possiblePicks.size()];
        for (int i = 0; i < possiblePicks.size(); i++) {
            arraySpinner[i] = possiblePicks.get(i).getName() + "---" + possiblePicks.get(i).getPosition() + "---" + Double.toString(possiblePicks.get(i).getProjPoints());
        }
        s = (Spinner) findViewById(R.id.availPlayers);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity2.this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

    }

    public void setPlayer(Team t, Player p) {
            if (p.getPosition().equals("RB")) {
                if (t.getRb1() == null) {
                    t.setRb1(p);
                } else if (t.getRb2() == null) {
                    t.setRb2(p);
                } else if (t.getBn1() == null) {
                    t.setBn1(p);
                } else if (t.getBn2() == null) {
                    t.setBn2(p);
                } else if (t.getBn3() == null) {
                    t.setBn3(p);
                } else if (t.getBn4() == null) {
                    t.setBn4(p);
                }
            }

            if (p.getPosition().equals("QB")) {
                if (t.getQb() == null) {
                    t.setQb(p);
                } else if (t.getBn1() == null) {
                    t.setBn1(p);
                } else if (t.getBn2() == null) {
                    t.setBn2(p);
                } else if (t.getBn3() == null) {
                    t.setBn3(p);
                } else if (t.getBn4() == null) {
                    t.setBn4(p);
                }
            }

            if (p.getPosition().equals("WR")) {
                if (t.getWr1() == null) {
                    t.setWr1(p);
                } else if (t.getWr2() == null) {
                    t.setWr2(p);
                } else if (t.getWr3() == null) {
                    t.setWr3(p);
                } else if (t.getBn1() == null) {
                    t.setBn1(p);
                } else if (t.getBn2() == null) {
                    t.setBn2(p);
                } else if (t.getBn3() == null) {
                    t.setBn3(p);
                } else if (t.getBn4() == null) {
                    t.setBn4(p);
                }
            }

            if (p.getPosition().equals("TE")) {
                if (t.getTe() == null) {
                    t.setTe(p);
                } else if (t.getBn1() == null) {
                    t.setBn1(p);
                } else if (t.getBn2() == null) {
                    t.setBn2(p);
                } else if (t.getBn3() == null) {
                    t.setBn3(p);
                } else if (t.getBn4() == null) {
                    t.setBn4(p);
                }
            }

            if (p.getPosition().equals("DEF")) {
                if (t.getDst() == null) {
                    t.setDst(p);
                } else if (t.getBn1() == null) {
                    t.setBn1(p);
                } else if (t.getBn2() == null) {
                    t.setBn2(p);
                } else if (t.getBn3() == null) {
                    t.setBn3(p);
                } else if (t.getBn4() == null) {
                    t.setBn4(p);
                }
            }

            if (p.getPosition().equals("K")) {
                if (t.getK() == null) {
                    t.setK(p);
                } else if (t.getBn1() == null) {
                    t.setBn1(p);
                } else if (t.getBn2() == null) {
                    t.setBn2(p);
                } else if (t.getBn3() == null) {
                    t.setBn3(p);
                } else if (t.getBn4() == null) {
                    t.setBn4(p);
                }
            }

        updateSpinner();
    }
}

