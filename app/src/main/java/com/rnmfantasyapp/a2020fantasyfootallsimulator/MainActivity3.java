package com.rnmfantasyapp.a2020fantasyfootallsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //output roster composition off all the teams in the draft
        ArrayList<Team> teamList = MainActivity2.teamList;
        String output = "";
        for(int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).getName().equals("User")) {
                output += teamList.get(i).getName() + "\n";
                output += "QB: " + teamList.get(i).getQb().getName() + "\n";
                output += "RB1: " + teamList.get(i).getRb1().getName() + "\n";
                output += "RB2: " + teamList.get(i).getRb2().getName() + "\n";
                output += "WR1: " + teamList.get(i).getWr1().getName() + "\n";
                output += "WR2: " + teamList.get(i).getWr2().getName() + "\n";
                output += "WR3: " + teamList.get(i).getWr3().getName() + "\n";
                output += "TE: " + teamList.get(i).getTe().getName() + "\n";
                output += "DEF: " + teamList.get(i).getDst().getName() + "\n";
                output += "K: " + teamList.get(i).getK().getName() + "\n";
                output += "BN1: " + teamList.get(i).getBn1().getName() + "\n";
                output += "BN2: " + teamList.get(i).getBn2().getName() + "\n";
                output += "BN3: " + teamList.get(i).getBn3().getName() + "\n";
                output += "BN4: " + teamList.get(i).getBn4().getName() + "\n";
                output += "\n";
            }
        }
        for(int i = 0; i < teamList.size(); i++) {
            if(!(teamList.get(i).getName().equals("User"))) {
                output += teamList.get(i).getName() + "\n";
                output += "QB: " + teamList.get(i).getQb().getName() + "\n";
                output += "RB1: " + teamList.get(i).getRb1().getName() + "\n";
                output += "RB2: " + teamList.get(i).getRb2().getName() + "\n";
                output += "WR1: " + teamList.get(i).getWr1().getName() + "\n";
                output += "WR2: " + teamList.get(i).getWr2().getName() + "\n";
                output += "WR3: " + teamList.get(i).getWr3().getName() + "\n";
                output += "TE: " + teamList.get(i).getTe().getName() + "\n";
                output += "DEF: " + teamList.get(i).getDst().getName() + "\n";
                output += "K: " + teamList.get(i).getK().getName() + "\n";
                output += "BN1: " + teamList.get(i).getBn1().getName() + "\n";
                output += "BN2: " + teamList.get(i).getBn2().getName() + "\n";
                output += "BN3: " + teamList.get(i).getBn3().getName() + "\n";
                output += "BN4: " + teamList.get(i).getBn4().getName() + "\n";
                output += "\n";
            }
        }
        TextView t = (TextView)findViewById(R.id.textView2);
        output = output.trim();
        t.setText(output);
        t.setTextSize(24);
        //t.setMovementMethod(new ScrollingMovementMethod());

        //exit app when you click finish button
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                System.exit(0);
            }
        });
    }
}