package com.example.a2020fantasyfootallsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
        };

        final Spinner s = (Spinner) findViewById(R.id.numTeams);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setDropDownVerticalOffset(150);


        Button btn = (Button) findViewById(R.id.startButton);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                String selection = s.getSelectedItem().toString();
                intent.putExtra("sel", selection);
                startActivity(intent);
                finish();
            }
        });
    }
}