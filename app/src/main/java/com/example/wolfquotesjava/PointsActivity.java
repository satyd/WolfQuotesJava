package com.example.wolfquotesjava;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PointsActivity extends AppCompatActivity {
    TextView log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        Bundle arguments = getIntent().getExtras();
        ArrayList<Integer> points =(ArrayList<Integer>) arguments.get("points");
        log = findViewById(R.id.textView_points_content);
        StringBuilder res = new StringBuilder("\n");
        for(int i=0;i<points.size();i++)
        {
            res.append(i+": "+points.get(i));
            if(i%2==1)
                res.append("\n");
            else
                for(int j=0;j<10-(points.get(i).toString()).length(); j++)
                    res.append(" ");
        }

        log.setText(res.toString());

    }
}
