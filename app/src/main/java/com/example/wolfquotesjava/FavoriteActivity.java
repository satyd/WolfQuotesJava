package com.example.wolfquotesjava;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    LinearLayout favoriteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        favoriteView = findViewById(R.id.main_favorite);
        setSupportActionBar(toolbar);
        setBg();
        //altUpdatefavorite();
    }
    @Override
    public void onStart(){
        super.onStart();
        altUpdatefavorite();
    }

    //Здесь RelativeLayout, т.к. хочу добавить кнопку "добавить в избранное", но пока не знаю как разместить
    public void updatefavorite()
    {
        Bundle arguments = getIntent().getExtras();
        ArrayList<String> favorite = (ArrayList<String>) arguments.get("currentfavorite");

        /*for(int i=0;i<favorite.size();i++){
            favoriteView.append(favorite.get(i));
        }*/
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i=0; i<favorite.size(); i++) {
            RelativeLayout viewNew = new RelativeLayout(this);
            String tmp=favorite.get(i);


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

            favoriteView.addView(viewNew,layoutParams);
        }
    }
    public void altUpdatefavorite()
    {
        Bundle arguments = getIntent().getExtras();
        ArrayList<String> favorite = (ArrayList<String>) arguments.get("currentFavorite");

/*
        int limit = (int) arguments.get("limit");
        int till = Math.min(favorite.size(), limit);*/
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<favorite.size(); i++) {

            LinearLayout viewNew = new LinearLayout(this);
            String tmp=favorite.get(i);
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
            Space spc = new Space(this);
            //spc.setMinimumHeight(1dp'');

            favoriteView.addView(viewNew,layoutParams);
        }
    }
    public void elementOnClick(View view)
    {

        String toast="u picked this";
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
    //Если установить бг для линер лэйаута то волка растянет нахрен
    public void setBg()
    {
        Bundle arguments = getIntent().getExtras();
        int currentBgId = (int)arguments.get("currentBgId");
        ScrollView bg=findViewById(R.id.favorite_scroll);
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