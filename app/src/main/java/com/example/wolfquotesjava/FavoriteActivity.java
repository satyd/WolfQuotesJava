package com.example.wolfquotesjava;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wolfquotesjava.data.FavoriteContract;
import com.example.wolfquotesjava.data.FavoriteContract.Favorite;
import com.example.wolfquotesjava.data.FavoriteDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FavoriteActivity extends AppCompatActivity {

    private final FavoriteDbHelper favoriteDbHelper = new FavoriteDbHelper(this);
    LinearLayout favoriteView;
    ArrayList<String> favorite = new ArrayList<>(0);
    StringBuilder favoriteQuotes = new StringBuilder();
    boolean block = true;
    public final static int RESULT_DELETED = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        favoriteView = findViewById(R.id.main_favorite);
        setSupportActionBar(toolbar);
        setBg();

        //altUpdateFavorite();
    }
    @Override
    public void onStart(){
        super.onStart();
        altUpdateFavorite();
    }

    //Здесь RelativeLayout, т.к. хочу добавить кнопку "добавить в избранное", но пока не знаю как разместить
    public void updateFavorite()
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
    public void altUpdateFavorite()
    {
        Bundle arguments = getIntent().getExtras();
        ArrayList<String> buf = (ArrayList<String>) arguments.get("currentFavorite");
        if(buf.size()>favorite.size())
        {
            favorite = buf;
/*
            int limit = (int) arguments.get("limit");
            int till = Math.min(favorite.size(), limit);*/
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            for(int i=0; i<favorite.size(); i++) {

                LinearLayout viewNew = new LinearLayout(this);
                String tmp = favorite.get(i);
                viewNew.setOrientation(LinearLayout.VERTICAL);
                viewNew.setBackgroundResource(R.drawable.rounded_corner_history);

                String quote = tmp.substring(0, tmp.indexOf('\n'));
                favoriteQuotes.append("\"").append(quote).append("\"\n");

                TextView text = new TextView(this);
                text.setTextColor(Color.BLACK);
                text.setTextSize(2, 18);
                text.setText(quote);
                text.setPadding(3, 3, 3, 3);

                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //textParams.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
                text.setLayoutParams(textParams);

                TextView date = new TextView(this);
                date.setTextColor(Color.DKGRAY);
                date.setTextSize(2, 11);
                date.setText(tmp.substring(tmp.indexOf('\n') + 1, tmp.length() - 1));
                date.setPadding(1, 1, 1, 1);
                RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //dateParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                //dateParams.addRule(RelativeLayout.BELOW,text.getId());

                date.setLayoutParams(dateParams);

                viewNew.addView(text);
                viewNew.addView(date);
                Space spc = new Space(this);
                //spc.setMinimumHeight(1dp'');

                favoriteView.addView(viewNew, layoutParams);
            }
            //setResult(RESULT_DELETED, intent);
        }
    }
    public void elementOnClick(View view)
    {

        String toast="u picked this";
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        LinearLayout bg = findViewById(R.id.background);
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_share_favorites:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, favoriteQuotes.toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.action_remove_favorites:

                SQLiteDatabase db = favoriteDbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ FavoriteContract.Favorite.TABLE_NAME+";");
                db.execSQL("REINDEX "+ FavoriteContract.Favorite.TABLE_NAME+";");
                db.execSQL("VACUUM;");
                Date currentDate = new Date();
                // Форматирование времени как "день.месяц.год"
                //DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                //String dateText = dateFormat.format(currentDate);
                //db.execSQL("INSERT INTO "+ Favorite.TABLE_NAME+" ("+Favorite.COLUMN_VALUE+","+Favorite.COLUMN_DATE+") VALUES ('костыль','2021');");
                //writeToFavorites("<костыль>");

                favoriteView.removeAllViews();
                Toast.makeText(this, "Избранное удалено", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                intent.putExtra("favoritesRemoved", true);
                intent.putExtra("result", 6);

                setResult(RESULT_DELETED, intent);
                //startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
