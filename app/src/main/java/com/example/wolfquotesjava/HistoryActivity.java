package com.example.wolfquotesjava;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import  com.example.wolfquotesjava.MainActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    LinearLayout historyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        historyView = findViewById(R.id.main_history);
        setSupportActionBar(toolbar);
        setBg();
        //altUpdateHistory();
    }
    @Override
    public void onStart(){
        super.onStart();
        altUpdateHistory();
    }

    //Здесь RelativeLayout, т.к. хочу добавить кнопку "добавить в избранное", но пока не знаю как разместить
    public void updateHistory()
    {
        Bundle arguments = getIntent().getExtras();
        ArrayList<String> history = (ArrayList<String>) arguments.get("currentHistory");

        /*for(int i=0;i<history.size();i++){
            historyView.append(history.get(i));
        }*/
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i=0; i<history.size(); i++) {
            RelativeLayout viewNew = new RelativeLayout(this);
            String tmp=history.get(i);


            TextView text = new TextView(this);
            text.setTextColor(Color.BLACK);
            text.setTextSize(2,18);
            text.setText(tmp.substring(0,tmp.indexOf('\n')));
            text.setPadding(3,3,3,3);

            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
            text.setLayoutParams(textParams);

            TextView date = new TextView(this);
            date.setTextColor(Color.GRAY);
            date.setTextSize(2,11);
            date.setText(tmp.substring(tmp.indexOf('\n'),tmp.length()-1));
            date.setPadding(1,1,1,1);
            RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dateParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            //dateParams.addRule(RelativeLayout.BELOW,text.getId());

            date.setLayoutParams(dateParams);

            viewNew.addView(text);
            viewNew.addView(date);

            historyView.addView(viewNew,layoutParams);
        }
    }
    public void altUpdateHistory()
    {
        Bundle arguments = getIntent().getExtras();
        ArrayList<String> history = (ArrayList<String>) arguments.get("currentHistory");


        int limit = (int) arguments.get("limit");
        int till = Math.min(history.size(), limit);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<till; i++) {


            LinearLayout viewNew = new LinearLayout(this);
            String tmp=history.get(i);
            viewNew.setOrientation(LinearLayout.VERTICAL);
            viewNew.setBackgroundResource( R.drawable.rounded_corner_history);


            TextView text = new TextView(this);
            text.setTextColor(Color.BLACK);
            text.setTextSize(2,18);
            text.setText(tmp.substring(0,tmp.indexOf('\n')));
            text.setPadding(3,3,3,3);

            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            //textParams.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
            text.setLayoutParams(textParams);

            TextView date = new TextView(this);
            date.setTextColor(Color.DKGRAY);
            date.setTextSize(2,11);
            date.setText(tmp.substring(tmp.indexOf('\n')+1,tmp.length()-1));
            date.setPadding(1,1,1,1);
            RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            //dateParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            //dateParams.addRule(RelativeLayout.BELOW,text.getId());

            date.setLayoutParams(dateParams);

            viewNew.addView(text);
            viewNew.addView(date);

            historyView.addView(viewNew,layoutParams);
        }
    }

    //Если установить бг для линер лэйаута то волка растянет нахрен
    public void setBg()
    {
        Bundle arguments = getIntent().getExtras();
        int currentBgId = (int)arguments.get("currentBgId");
        ScrollView bg=findViewById(R.id.history_scroll);
        switch (currentBgId)
        {
            case 1:
                bg.setBackgroundResource(R.drawable.volk1);
                break;
            case 2:
                bg.setBackgroundResource(R.drawable.volk2);
                break;
            default:
                bg.setBackgroundResource(R.drawable.volk0);
                break;
        }
    }
}