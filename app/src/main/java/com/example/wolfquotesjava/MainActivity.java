package com.example.wolfquotesjava;

import com.example.wolfquotesjava.data.FavoriteContract.Favorite;
import com.example.wolfquotesjava.data.FavoriteDbHelper;
import com.example.wolfquotesjava.data.HistoryContract.History;
import com.example.wolfquotesjava.data.HistoryDbHelper;
import com.example.wolfquotesjava.data.TemplatesContract.Templates;
import com.example.wolfquotesjava.data.TemplatesDbHelper;
import com.example.wolfquotesjava.data.WordDbHelper;
import com.example.wolfquotesjava.data.WordsContract.WordStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;


class Word {
    private int weight;
    private final String value;
    public Word(String val,int in)
    {
        value = val;
        weight = in;
    }
    int getWeight()
    {
        return weight;
    }
    String getValue()
    {
        return value;
    }

    void changeWeight(int chng)
    {
        this.weight+=chng;
    }

}

class WordList
{

    private int overWeight=0;
    private ArrayList<Word> list=new ArrayList<>();
    public static ArrayList<Integer> lastUsed;

    public WordList(ArrayList<Word> init)
    {
        this.overWeight = this.calcWeight();
        this.list=init;
    }
    public WordList(ArrayList<Integer> weights, ArrayList<String> init)
    {
        this.overWeight = 0;
        if (weights.size() == init.size()) {
            for(int i=0;i<weights.size();i++)
            {
                Word tmp=new Word(init.get(i),weights.get(i));
                this.overWeight+=weights.get(i);
                this.list.add(tmp);

            }
            //this.calcWeight();
        } else
            System.err.println("Неправильные размеры массивов!!!");

    }
    public void addWord(Word a)
    {
        this.list.add(a);
        this.overWeight+=a.getWeight();
    }
    public void addWord(String str,int weight)
    {
        this.list.add(new Word(str,weight));
        this.overWeight+=weight;
    }
    public void addStr(String str,int weight)
    {
        Word a=new Word(str,weight);
        this.list.add(a);
        this.overWeight+=weight;
    }

    public int calcWeight(){
        this.overWeight=0;
        for(int i=0;i<list.size();i++)
            this.overWeight+=list.get(i).getWeight();
        return this.overWeight;
    }
    public int getSize()
    {
        return this.list.size();
    }
    public String getWord()
    {
        //this.calcWeight();
        Random rand=new Random();
        int randomize = rand.nextInt(this.overWeight+1);
        int start=0;
        String result="<это баг>";
        //System.out.println("Рандом: "+randomize+"\nОбщий вес: "+this.overWeight);
        for(int i=0;i<this.list.size();i++)
        {
            //System.out.println(this.list.get(i).getWeight());
            start+=this.list.get(i).getWeight();
            if(start>=randomize)
            {

                String tmp=this.list.get(i).getValue();
                result = ( tmp != null) ? tmp:"<это баг>";
                //lastUsed.add(i);
                break;
            }
        }
        return result;
    }
    public static void clearLatest(){
        lastUsed.clear();
    }
    public void reWeight(ArrayList<Integer> change)
    {
        if(change.size() == lastUsed.size())
        {
            for(int i=0;i<change.size();i++)
                list.get(lastUsed.get(i)).changeWeight(change.get(i));
        }
        else
            System.err.println("---->Неправильный размер массива весов !!!");

    }
    @Override
    public int hashCode(){
        int res = overWeight;
        res = res * 31 + list.hashCode();

        return res;
    }

}


public class MainActivity extends AppCompatActivity {
    ArrayList<String> Presets= new ArrayList<String>(Arrays.asList(
            "Брат уйдёт от брата только если звонок на урок.",
            "Брат, бей первым. И не важно, кто есть кто, кто был кем и ким чен ын. Бог рассудит каждого из тех, кто был никто, " +
                    "а некто, кем был ты, не будет быть, но будет ждать. Ты сам себе бог. Сам себе брат. Сам себе сам. Сам себе режиссёр" +
                    " скачать все сезоны бесплатно. Главное, помнить - освежители воздуха с ароматом сирени лучше аромата океанского бриза...",
            "Брат, помни, если тебя съели, у тебя есть два выхода.",
            "В жизни есть две дороги: первая и вторая.",
            "В этой жизни ты либо волк, либо не волк.",
            "Волк может отгрызть себе жопу чтобы не стать петухом. А может не отгрызть.",
            "Волк не сдаёт русский, потому что русские не сдаются.",
            "Волк не тот кто волк, а тот кто волк.",
            "Волк слабее льва и тигра, но волк в цирке не выступает.",
            "Даже если ты в заднице, не обязательно быть фекалиями.",
            "Девушки, совет: выбирайте парней помладше, потому что породистых псов забирают ещё щенками.",
            "Делай как надо – как не надо не делай.",
            "Брат запомни, если весь мир встанет против тебя, у меня встанет на тебя.",
            "Делай как надо – как не надо не делай.",
            "Деньги не сделают тебя беднее, они лишь сделают тебя богаче.",
            "Если вашего друга сбила машина, то он вам не друг, потому что настоящие друзья на дороге не валяются.",
            "Если волк дышит - он жив, если не дышит - мёртв.",
            "Если волк молчит, то лучше его не перебивать.",
            "Если тебе тяжело идти, значит ты жирный.",
            "Если волк молчит, то лучше его не перебивать.",
            "Если тебе тяжело идти, значит ты жирный.",
            "Запомните твари, а то забудете.",
            "Запомните твари, ко второму же спряженью\n" +
                    "Отнесем мы без сомненья\n" +
                    "Все глаголы, что на -ить,\n" +
                    "Исключая брить, стелить.\n" +
                    "А еще: смотреть, обидеть,\n" +
                    "слышать, видеть, ненавидеть,\n" +
                    "гнать, дышать, держать, терпеть,\n" +
                    "И зависеть, и вертеть.\n",

            "Иногда нужно пукнуть, чтобы тебя заметили, и нагадить, чтобы тебя нашли.",
            "Каждый думает что умеет, но я не думаю, я не каждый, я не шакал.",
            "Каждый может кинуть камень в волка, но не каждый может кинуть волка в камень.",
            "Кем бы ты ни был, кем бы ты не стал, помни, где ты был и кем ты стал.",
            "Красиво идёт по жизни тот, кто умеет ходить.",
            "Лучше быть последним в стае волков, чем первым в стае шакалов.",
            "Лучше быть последним среди волков, чем последним в очереди к толчку.",
            "Лучше долбить друга, чем в дверь, где тебя не ждут.",
            "Лучше задница в тепле, чем тепло в заднице.",
            "Лучше иметь друга, чем друг друга.",
            "Лучше иметь друга, чем друг друга.",
            "Лучше с кентами на велике, чем с чертями на гелике.",
            "Майонез не хлеб, на него масло не намажешь. А хлеб не масло.",
            "Меня зовут не по имени, а бухать.",
            "Мёртвый снаружи погиб внутри однажды.",
            "На случай если буду нужен, то я там же, где и был, когда был не нужен.",
            "На случай если я буду нужен – я там, когда был не нужен.",
            "Неважно кто напротив, важно кто насрал.",
            "Не губы красят девушку, а девушка красит губы.",
            "Не нужно бояться смерти, ведь пока ты жив – её нет.",
            "Не трогай своих, а то останешься без моих.",
            "Падение это не провал, провал это углубление, а падение это процесс.",
            "Побеждает всегда победитель, проигравший – проигрывает",
            "Пройдет время и жизнь покажет, что время прошло.",
            "Работа не волк, работа это ворк а волк это ходить.",
            "Спасибо прадеду за деда.",
            "Ты можешь сто раз опоздать из-за того что срал, но тебя всё равно запомнят потому что ты обосрался, прийдя вовремя.",
            "Тяжелее всего проснуться, когда ты спал.",
            "У батареек есть один плюс. И один минус.",
            "У каждого волка есть шкура, но не у каждой шкуры есть волк.",
            "Чем сложнее задача, тем проще её не решать.",
            "Чем сложнее задача, тем проще на неё забить.",
            "Я может и не может, но хотя бы не я."));
    //String ~== group id, WordList == группа слов

    private final String spc = " ";
    private final String end = ".";
    private final String cmm = ", ";

    private final boolean isSafeOn = true;

    private int currentBgId = 0;
    private final int backgroundsCount = 3;
    private WordDbHelper wordDbHelper;

    private HistoryDbHelper historyDbHelper;
    public ArrayList<String> history=new ArrayList<>();
    int historyLimit = 150;


    private FavoriteDbHelper favoriteDbHelper;
    public ArrayList<String> favorite=new ArrayList<>();

    private TemplatesDbHelper templatesDbHelper;

    // имя файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_BACKGROUND_ID = "background";
    public static final String APP_PREFERENCES_RUNS = "runs";
    private SharedPreferences mSettings;


    ClipboardManager clipboardManager;
    ClipData clipData;


    String latestTemplate = "АУФ.";
    int overallWeight=0;
    int presetsWeight=10;
    int totalTemplates = 42;
    int template;
    int runs=0;
    ArrayList<Integer> log;
    StringBuilder str;
    String tmp="АУФ.";

    HashMap<String,WordList> wordMap=new HashMap<>();

    private int wolfCounter;
    private TextView main;
    private View subMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordDbHelper = new WordDbHelper(this);
        historyDbHelper = new HistoryDbHelper(this);
        favoriteDbHelper = new FavoriteDbHelper(this);
        templatesDbHelper = new TemplatesDbHelper(this);

        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        wolfCounter = 0;

        insertTemplates();
        log = new ArrayList<>(totalTemplates);
        getTemplates();


        if (mSettings.contains(APP_PREFERENCES_BACKGROUND_ID))
        {
            currentBgId = mSettings.getInt(APP_PREFERENCES_BACKGROUND_ID, 0);
            runs = mSettings.getInt(APP_PREFERENCES_RUNS, 0)+1;
        }


        if(runs<=1)    {
            writeToFavorites("<костыль>");
            writeToHistory("<костыль>");
            fillWordsDB();
        }

        getFavorite();
        setOnStartBg();
        loadWordMap();

        overallWeight = calcOverallWeight();

        main = findViewById(R.id.textAuf);
        main.setMovementMethod(new ScrollingMovementMethod());
        subMain = main;

        subMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!tmp.equals("АУФ."))
                {
                    clipData = ClipData.newPlainText("text",tmp);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(getBaseContext(), " текст скопирован! ", Toast.LENGTH_SHORT).show();


                }
                return false;
            }
        });

        System.out.println("\n>>>>>>>>>>>>   RUNS == "+runs+"   <<<<<<<<<<<<<<<\n");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
        //fillWordsDB();
        //loadWordMap();
        Bundle arguments = getIntent().getExtras();
        if(arguments != null)
        {
            boolean favoritesRemoved = (boolean) arguments.get("favoritesRemoved");

            if(favoritesRemoved)
            {
                favorite = new ArrayList<>();
                writeToFavorites("<костыль>");
                System.out.println("\n\n\nExtrasBeingPut\n\n\n");
            }
        }


    }
    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_COUNTER, wolfCounter);
        editor.putInt(APP_PREFERENCES_BACKGROUND_ID, currentBgId);
        editor.putInt(APP_PREFERENCES_RUNS, runs);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_BACKGROUND_ID))
        {
            currentBgId = mSettings.getInt(APP_PREFERENCES_BACKGROUND_ID, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            // Получаем число из настроек
            wolfCounter = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
        }


    }
    private void loadWordMap() {
        SQLiteDatabase db = wordDbHelper.getReadableDatabase();

        int safeMode=1;
        if(!isSafeOn)
            safeMode=0;

        String query = "SELECT *"
                +" FROM "+WordStorage.TABLE_NAME
                +" WHERE "+WordStorage.COLUMN_IS_SAFE+">="+safeMode
                +" ORDER BY "+WordStorage.COLUMN_TYPE+";";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        String latestType = cursor.getString(cursor.getColumnIndex(WordStorage.COLUMN_TYPE));


        ArrayList<Integer> weights=new ArrayList<Integer>();
        ArrayList<String> values=new ArrayList<String>();
        ArrayList<String> iterater=new ArrayList<String>();

        for(int i=0;i<cursor.getCount();i++)
        {
            String tmpStr=cursor.getString(cursor.getColumnIndex(WordStorage.COLUMN_TYPE));
            if(!latestType.equals(tmpStr))
            {
                //System.out.println(weights.size()+" "+values.size()+" for "+latestType);
                wordMap.put(latestType,new WordList(weights,values));
                weights=new ArrayList<Integer>();
                values=new ArrayList<String>();
                iterater.add(latestType);
                latestType=tmpStr;
            }

            weights.add(cursor.getInt(cursor.getColumnIndex(WordStorage.COLUMN_WEIGHT)));
            values.add(cursor.getString(cursor.getColumnIndex(WordStorage.COLUMN_VALUE)));
            cursor.moveToNext();

        }
        //System.out.println(weights.size()+" "+values.size()+" for "+latestType);
        iterater.add(latestType);
        wordMap.put(latestType,new WordList(weights,values));

        int kek=0;
        //String str=wordMap.get(0).getWord();
        //for(int i=0;i<iterater.size();i++)
        //   kek+=wordMap.get(iterater.get(i)).getSize();
        //Toast.makeText(this, kek+" words has been loaded; it: "+ iterater.size(), Toast.LENGTH_SHORT).show();
        cursor.close();
    }

    private void fillWordsDB(){
        SQLiteDatabase db = wordDbHelper.getWritableDatabase();
        insertWord("волк","nounsSubj",15,1);
        insertWord("шакал","nounsSubj",7,1);

        insertWordListOfType(new ArrayList<String>(Arrays.asList("враг","друг","брат","человек","товарищ")),
                "Subj",
                new ArrayList<Integer>(Arrays.asList(7, 6, 10, 3, 1)),
                new ArrayList<Integer>(Arrays.asList(1, 1,  1, 1, 1)));


        insertWordListOfType(new ArrayList<String>(Arrays.asList("вино", "дерево", "золото", "исключение", "колесо", "мясо", "окно", "основание", "отличие",
                "очко", "сердце", "слово", "стекло", "чувство", "яблоко")),
                "nounsItemA",
                new ArrayList<Integer>(Arrays.asList(2, 5, 3, 2, 4, 7, 3, 2, 2, 4, 2, 6, 3, 2, 1)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("голова", "дверь", "жизнь", "задача", "защита", "клетка", "линия", "лошадь", "машина",
                "молитва", "музыка", "новость", "партия", "пенсия", "перспектива", "практика", "ручка", "связь", "сделка", "серия", "среда", "техника",
                "услуга", "фигура", "цель")),
                "nounsItemF",
                new ArrayList<Integer>(Arrays.asList(7, 6, 4, 5, 4, 6, 1, 3, 4, 1, 1, 2, 3, 3, 2, 3, 4, 2, 3, 4, 3, 2, 3, 2, 5)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("адрес", "актер", "вечер", "день", "дурак", "знак", "источник", "камень", "ключ", "корень",
                "кредит", "крик", "мешок", "номер", "ответ", "отдел", "предмет", "признак", "расход", "результат", "ремонт", "ресурс", "состав", "стол",
                "стул", "сюжет", "телефон", "ум", "участок", "хвост", "черт")),
                "nounsItemM",
                new ArrayList<Integer>(Arrays.asList(1, 1, 2, 2, 3, 2, 1, 5, 2, 1, 1, 2, 3, 4, 2, 4, 2, 1, 5, 2, 1, 3, 4, 4, 2, 4, 8, 2, 5, 1, 3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("величина", "внимание", "воспитание", "вражда", "высота", "грусть", "действительность",
                "дело", "достоинство", "дружба", "интерес", "истина", "кризис", "образование", "основа", "ответственность", "отношение", "печаль",
                "позиция", "реальность", "скука", "сожаление", "состояние", "сравнение", "температура", "тишина", "торговля","жопа")),
                "nounsAmbient",
                new ArrayList<Integer>(Arrays.asList(1, 5, 3, 4, 3, 4, 3, 5, 2, 4, 3, 5, 4, 2, 1, 1, 2, 3, 1, 3, 1, 1, 2, 1, 2, 7, 2)),
                new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("лес","дом","цирк","снег","сарай","окно","рот")),
                "nounsPlace",
                new ArrayList<Integer>(Arrays.asList(9,3,8,2,2,1,1)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("в лесу","дома","в цирке","в снегу","в сарае","в окне","во рту","в семье","внизу","вверху","в луже","в подъезде",
                "на балконе","на крыше","на столе","в огне","на лестнице","на дереве","в жопе","в руках")),
                "nounsContainer",
                new ArrayList<Integer>(Arrays.asList(8,5,12,2,2,1,1,5,2,2,3,4,2,4,4,1,2,2,4,3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1 ,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("был","делал","знал","ломал","бежал","умирал","ел","садился","срал","уходил","забывал")),
                "verbsBeen",
                new ArrayList<Integer>(Arrays.asList(15, 7, 3, 4, 3, 8, 5, 2, 4, 5, 5)),
                new ArrayList<Integer>(Arrays.asList(1,  1, 1, 1, 1, 1, 1, 1, 0, 1, 1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("бежит", "выступает", "делает", "думает", "дышит", "ждёт", "забывает", "знает", "может",
                "молчит", "опаздывает", "просит", "пугает", "сидит", "слушает", "смотрит", "стучит", "существует", "уважает", "срёт")),
                "verbsSubj",
                new ArrayList<Integer>(Arrays.asList(6, 11, 7, 7, 4, 6, 6, 5, 12, 7, 12, 3, 10, 7, 8, 6, 2, 8, 6, 7)),
                new ArrayList<Integer>(Arrays.asList(1,  1, 1, 1, 1, 1, 1, 1,  1, 1,  1, 1,  1, 1, 1, 1, 1, 1, 1, 0)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("забыл", "купил", "поел", "помешал", "прибежал", "принял", "приобрёл", "проиграл", "сделал",
                "сел", "сломал", "узнал", "умер", "ушёл","посрал")),
                "verbsDone",
                new ArrayList<Integer>(Arrays.asList(5, 6, 5, 3, 3, 6, 3, 8, 7, 2, 4, 3, 8, 5, 4)),
                new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("вспомнил", "выявил", "достал", "забыл", "запомнил", "избежал", "изменил", "купил", "напугал",
                "попросил", "посмотрел", "признал", "принял", "съел", "толкнул", "убил", "ударил")),
                "verbsDoneSubj",
                new ArrayList<Integer>(Arrays.asList(4, 1, 6, 5, 5, 4, 2, 5, 7, 3, 3, 1, 2, 8, 3, 6, 6)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("познаётся", "изучается", "забывается", "узнаётся", "происходит","творится")),
                "verbsLearn",
                new ArrayList<Integer>(Arrays.asList(15,3,6,8,4,2)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("бить", "добавлять", "забывать", "замечать", "запоминать", "знать",
                "изменять", "откладывать", "отмечать", "отрывать", "признавать", "принимать", "пугать", "раздражать", "разрабатывать",
                "слушать", "тащить", "толкать", "убивать", "уважать", "увеличивать", "удивлять", "уменьшать")),
                "infinitivesObj",
                new ArrayList<Integer>(Arrays.asList(7, 5, 6, 7, 5, 12, 6, 2, 7, 6, 3, 5, 4, 8, 2, 10, 3, 5, 7, 9, 3, 4, 3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("вытащить", "избежать", "изменить", "купить", "нанести", "одеть",
                "оплатить", "оспорить", "оторвать", "порвать", "признать", "принять", "приобрести", "продать", "сделать", "сломать", "снять", "съесть", "убить")),
                "infinitivesSubj",
                new ArrayList<Integer>(Arrays.asList(3, 7, 5, 6, 1, 2, 5, 4, 5, 6, 4, 5, 3, 5, 7, 5, 6, 8, 4)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("бежать", "быть", "голосовать", "делать", "думать", "жаловаться", "знать",
                "играть", "иметь", "меняться", "мешать", "мыслить", "наступать", "ожидать", "открывать", "переживать", "появляться", "развиваться",
                "руководить", "сидеть", "складывать", "следить", "служить", "слушать")),
                "infinitivesDo",
                new ArrayList<Integer>(Arrays.asList(10, 11, 3, 15, 20, 7, 14, 4, 6, 2, 12, 2, 5, 7, 9, 7, 2, 4, 2, 7, 4, 6, 8, 3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("вскочить", "встать", "вывести", "вытащить", "добавить", "оторвать", "подняться", "полететь",
                "помочь", "прибежать", "привести", "приехать", "прийти", "принести", "принять", "проснуться", "решить", "собраться", "съесть", "убить", "ударить",
                "узнать", "упасть")),
                "infinitivesDone",
                new ArrayList<Integer>(Arrays.asList(4, 9, 3, 5, 5, 5, 6, 5, 5, 7, 5, 6, 7, 6, 6, 9, 4, 6, 12, 10, 7, 10, 5)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("перебивать","слушать","спрашивать","говорить","затыкать","вмешиваться")),
                "infinitivesSpeech",
                new ArrayList<Integer>(Arrays.asList(5,5,3,3,2,3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("активный", "банальный", "бедный", "богатый", "быстрый", "верный", "вертикальный", "внезапный",
                "внутренний", "вредный", "всевозможный", "гордый", "громкий", "древний", "дремучий", "дурной", "душевный", "жирный", "значительный",
                "качественный", "квадратный", "лучший", "надежный", "наивный", "натуральный", "незаконный", "неудачный", "ограниченный", "открытый", "поверхностный",
                "подходящий", "принципиальный", "прохладный", "решительный", "сильный", "случайный", "смелый", "страшный", "террористический", "тревожный", "умный",
                "универсальный", "частый", "явный")),
                "adjectivesHard",
                new ArrayList<Integer>(Arrays.asList(2, 4, 2, 2, 4, 3, 1, 4, 3, 2, 4, 7, 3, 4, 6, 1, 2, 1, 5, 3, 1, 1, 1, 5, 3, 1, 1, 5, 3, 5, 6, 5, 5, 1, 2, 4, 2, 2, 2)),
                new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)));
        
        insertWordListOfType(new ArrayList<String>(Arrays.asList("внутренний", "древний", "дремучий", "лучший", "подходящий"
                )),
                "adjectivesSoft",
                new ArrayList<Integer>(Arrays.asList(1,1,4,5,3)),
                new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("тяжело","трудно","легко","быстро","влажно","противно","невозможно","хорошо","уютно","неудобно","некомфортно","стыдно","плохо","плавно")),
                "adverbsHow",
                new ArrayList<>(Arrays.asList(5, 7, 5, 5, 1, 2,3,4,2,5,2,4,4,1)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("неважно","важно","без разницы","похуй")),
                "adverbsAns",
                new ArrayList<Integer>(Arrays.asList(9,4,2,1)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,0)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("тяжелее","легче","проще","труднее","правильнее","лучше")),
                "adverbsCmp",
                new ArrayList<Integer>(Arrays.asList(12,6,3,4,2,5)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("лучше","сильнее","быстрее","больше","сложнее")),
                "adverbsCmpG",
                new ArrayList<Integer>(Arrays.asList(5,7,7,4,3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("слабее","хуже","короче","меньше","проще","легче")),
                "adverbsCmpL",
                new ArrayList<Integer>(Arrays.asList(7,6,3,4,3,5)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("абсолютно", "весьма", "исключительно", "крайне", "невероятно", "очень", "почти", "слишком", "совсем", "чрезвычайно", "чресчур")),
                "adverbsDeg",
                new ArrayList<Integer>(Arrays.asList(7, 4, 3, 6, 3, 8, 7, 8, 7, 4, 3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("где","как","зачем","когда","для чего","с кем","откуда","сколько")),
                "questions",
                new ArrayList<Integer>(Arrays.asList(5,8,10,5,3,3,4,3)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("первым", "последним", "вторым","главным")),
                "ordinalHow",
                new ArrayList<Integer>(Arrays.asList(10,8,3,7)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("первый", "последний", "второй","главный")),
                "ordinalWhich",
                new ArrayList<Integer>(Arrays.asList(10,8,3,7)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("льва", "тигра", "шакала","волка","всех")),
                "nounsCmp",
                new ArrayList<Integer>(Arrays.asList(8,8,12,3,5)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1,1)));

        insertWordListOfType(new ArrayList<String>(Arrays.asList("каждый", "не каждый","тот", "не тот")),
                "who",
                new ArrayList<Integer>(Arrays.asList(10,14,3,1)),
                new ArrayList<Integer>(Arrays.asList(1,1,1,1)));
        insertWordListOfType(new ArrayList<String>(Arrays.asList("если","когда")),
                "condition",
                new ArrayList<Integer>(Arrays.asList(12,10)),
                new ArrayList<Integer>(Arrays.asList(1,1)));

    }

    private void displayDatabaseInfo() {
        //SQLiteDatabase myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = wordDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                WordStorage._ID,
                WordStorage.COLUMN_VALUE,
                WordStorage.COLUMN_TYPE,
                WordStorage.COLUMN_WEIGHT,
                WordStorage.COLUMN_IS_SAFE };

        // Делаем запрос
        Cursor cursor = db.query(
                WordStorage.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        TextView displayTextView = (TextView) findViewById(R.id.textAuf);

        try {
            displayTextView.setText("Таблица содержит " + cursor.getCount() + " слов.\n\n");
            displayTextView.append(WordStorage._ID + " - " +
                    WordStorage.COLUMN_VALUE + " - " +
                    WordStorage.COLUMN_TYPE + " - " +
                    WordStorage.COLUMN_WEIGHT + " - " +
                    WordStorage.COLUMN_IS_SAFE + "\n");

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(WordStorage._ID);
            int valueColumnIndex = cursor.getColumnIndex(WordStorage.COLUMN_VALUE);
            int typeColumnIndex = cursor.getColumnIndex(WordStorage.COLUMN_TYPE);
            int weightColumnIndex = cursor.getColumnIndex(WordStorage.COLUMN_WEIGHT);
            int is_safeColumnIndex = cursor.getColumnIndex(WordStorage.COLUMN_IS_SAFE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentValue = cursor.getString(valueColumnIndex);
                String currentType = cursor.getString(typeColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);
                int currentIsSafe = cursor.getInt(is_safeColumnIndex);
                // Выводим значения каждого столбца
                displayTextView.append(("\n" + currentID + " - " +
                        currentValue + " - " +
                        currentType + " - " +
                        currentWeight + " - " +
                        currentIsSafe));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

    private void insertWordListOfType(ArrayList<String> value,String type,ArrayList<Integer> weight, ArrayList<Integer> is_safe) {
        SQLiteDatabase db = wordDbHelper.getWritableDatabase();
        while(weight.size()<value.size())
            weight.add(2);
        while(is_safe.size()<value.size())
            is_safe.add(1);
        if(value.size()<=weight.size() && value.size()<=is_safe.size()) {
            for (int i = 0; i <value.size();i++) {

                String query = "SELECT " + WordStorage.COLUMN_VALUE + ", "
                        + WordStorage.COLUMN_TYPE
                        + " FROM " + WordStorage.TABLE_NAME
                        + " WHERE (" + WordStorage.COLUMN_VALUE + " LIKE '" + value.get(i)
                        + "') AND (" + WordStorage.COLUMN_TYPE + " LIKE '" + type + "');";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor.getCount() == 0) {
                    // Создаем объект ContentValues, где имена столбцов ключи,
                    // а информация о слове является значениями ключей
                    ContentValues values = new ContentValues();
                    values.put(WordStorage.COLUMN_VALUE, value.get(i));
                    values.put(WordStorage.COLUMN_TYPE, type);
                    values.put(WordStorage.COLUMN_WEIGHT, weight.get(i));
                    values.put(WordStorage.COLUMN_IS_SAFE, is_safe.get(i));

                    long newRowId = db.insert(WordStorage.TABLE_NAME, null, values);
                } else
                    System.out.println("Такое слово уже есть!!!");
                cursor.close();
            }
        }
        else
            System.out.println("\n>>>>>>>>>>>>   Wrong Size!   <<<<<<<<<<<<<<<\n" + "Caused by: " + type +"\n\n");
    }

    private void insertWord(String value, String type,int weight, int is_safe) {
        SQLiteDatabase db = wordDbHelper.getWritableDatabase();

        String query = "SELECT " + WordStorage.COLUMN_VALUE + ", "
                + WordStorage.COLUMN_TYPE
                +" FROM "+WordStorage.TABLE_NAME
                +" WHERE (" + WordStorage.COLUMN_VALUE + " LIKE '" + value
                +"') AND (" + WordStorage.COLUMN_TYPE + " LIKE '" + type +"');";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()==0) {
            // Создаем объект ContentValues, где имена столбцов ключи,
            // а информация о слове является значениями ключей
            ContentValues values = new ContentValues();
            values.put(WordStorage.COLUMN_VALUE, value);
            values.put(WordStorage.COLUMN_TYPE, type);
            values.put(WordStorage.COLUMN_WEIGHT, weight);
            values.put(WordStorage.COLUMN_IS_SAFE, is_safe);

            long newRowId = db.insert(WordStorage.TABLE_NAME, null, values);
        }
        else
            System.out.println("Такое слово уже есть!!!");
        cursor.close();
    }

    private void writeToHistory(String value) {
        SQLiteDatabase db = historyDbHelper.getWritableDatabase();
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);


        ContentValues values = new ContentValues();
        values.put(History.COLUMN_VALUE, value);
        values.put(History.COLUMN_DATE, dateText+", "+timeText + "  t#"+template);
        long newRowId = db.insert(History.TABLE_NAME, null, values);
    }

    private void getHistory() {
        SQLiteDatabase db = historyDbHelper.getReadableDatabase();

        //TextView historyView = findViewById(R.id.historyBlank);

        String query = "SELECT *"
                +" FROM "+History.TABLE_NAME
                +" ORDER BY "+History.COLUMN_DATE+" DESC;";
        Cursor cursor=db.rawQuery(query,null);
        int total=cursor.getCount();
        cursor.moveToFirst();

        String tost=" History contains "+total+" values\n\n";
        //Toast.makeText(this, tost, Toast.LENGTH_SHORT).show();
        history.clear();
        for(int i=0;i<total-1;i++)
        {

            String result = cursor.getString(cursor.getColumnIndex(History.COLUMN_VALUE)) +
                    "\n" +
                    cursor.getString(cursor.getColumnIndex(History.COLUMN_DATE)) +
                    "\n";
            //System.out.println(result);
            history.add(result);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void reWeightTemplate(int num,int weight) {
        SQLiteDatabase db = templatesDbHelper.getWritableDatabase();
        db.execSQL("UPDATE "+ Templates.TABLE_NAME + " SET "+ Templates.COLUMN_WEIGHT+"="+weight +" WHERE "+Templates.COLUMN_NUMBER+"="+num);
    }

    private void insertTemplates() {
        SQLiteDatabase db = templatesDbHelper.getWritableDatabase();

        String query = "SELECT " + Templates.COLUMN_NUMBER + ", "
                + Templates.COLUMN_WEIGHT
                +" FROM "+Templates.TABLE_NAME + ";";

        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()< totalTemplates) {
            // Создаем объект ContentValues, где имена столбцов ключи,
            // а информация о слове является значениями ключей
            for(int i = cursor.getCount(); i< totalTemplates; i++)
            {
                ContentValues values = new ContentValues();
                values.put(Templates.COLUMN_NUMBER, i);
                values.put(Templates.COLUMN_WEIGHT, 7);
                long newRowId = db.insert(Templates.TABLE_NAME, null, values);
            }

        }
        else
            System.err.println("Такое слово уже есть!!!");
        cursor.close();
    }
    private void getTemplates() {
        SQLiteDatabase db = templatesDbHelper.getReadableDatabase();

        //TextView historyView = findViewById(R.id.historyBlank);

        String query = "SELECT *"
                +" FROM "+ Templates.TABLE_NAME
                +" ORDER BY "+ Templates.COLUMN_NUMBER+" ASC;";
        Cursor cursor=db.rawQuery(query,null);
        int total=cursor.getCount();
        cursor.moveToFirst();

        log.clear();
        for(int i=0;i<total;i++)
        {
            int n = cursor.getInt(cursor.getColumnIndex(Templates.COLUMN_NUMBER));
            int w = cursor.getInt(cursor.getColumnIndex(Templates.COLUMN_WEIGHT));

            log.add(w);
            //System.out.println("Добавлен вес "+w);
            cursor.moveToNext();
        }
        log.add(presetsWeight);
        cursor.close();
    }

    private void writeToFavorites(String value) throws SQLiteConstraintException {
        SQLiteDatabase db = favoriteDbHelper.getWritableDatabase();
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        ContentValues values = new ContentValues();
        values.put(Favorite.COLUMN_VALUE, value);
        values.put(Favorite.COLUMN_DATE, dateText+", "+timeText);
        try{
            long newRowId = db.insertOrThrow(Favorite.TABLE_NAME, null, values);
        } catch(SQLiteConstraintException e){
            System.out.println(e.getMessage());
        }

    }

    private void getFavorite() {
        SQLiteDatabase db = favoriteDbHelper.getReadableDatabase();

        //TextView historyView = findViewById(R.id.historyBlank);

        String query = "SELECT *"
                +" FROM "+Favorite.TABLE_NAME
                +" ORDER BY "+Favorite.COLUMN_DATE+" DESC;";
        Cursor cursor=db.rawQuery(query,null);
        int total=cursor.getCount();
        cursor.moveToFirst();

        favorite.clear();
        for(int i=0;i<total-1;i++)
        {

            String result = cursor.getString(cursor.getColumnIndex(Favorite.COLUMN_VALUE)) +
                    "\n" +
                    cursor.getString(cursor.getColumnIndex(Favorite.COLUMN_DATE)) +
                    "\n";
            //System.out.println(result);
            favorite.add(result);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public String reEndAdj(@NotNull String adjective, @NotNull String change)
    {
        int len = change.length();
        if(len >= 3)
            len=2;
        return adjective.substring(0,adjective.length()-len)+change;
    }

    /**
     * Случайный глагол
     * @param bound
     * @return  0 -- verbsDoneSubj
     *  1 -- verbsDone
     *  2 -- verbsSubj
     */
    public String randVerb(int bound)
    {
        int res = new Random().nextInt(bound);
        switch (res)
        {
            case 0:
                return wordMap.get("verbsDoneSubj").getWord();
            case 1:
                return wordMap.get("verbsDone").getWord();
            default:
                return wordMap.get("verbsSubj").getWord();
        }
    }
    public String randInf(int bound)
    {
        int res = new Random().nextInt(bound);
        switch (res)
        {
            case 0:
                return wordMap.get("infinitivesObj").getWord();
            case 1:
                return wordMap.get("infinitivesDone").getWord();
            case 2:
                return wordMap.get("infinitivesDo").getWord();
            default:
                return wordMap.get("infinitivesSubj").getWord();
        }
    }

    public String randAdj()
    {
        int res = new Random().nextInt(7);
        switch (res)
        {
            case 0:
                return wordMap.get("adjectivesSoft").getWord();
            default:
                return wordMap.get("adjectivesHard").getWord();
        }
    }
    public String randNot()
    {
        int res = new Random().nextInt(2);
        switch (res)
        {
            case 0:
                return " не ";
            default:
                return " ";
        }
    }


    /**
        Функция выбирает случайное существительное
     * @param caser -1 для рандома по параметру bound
     * @param bound ограничение рандома
     * @return 0 -- nounsItemM,
     *         1 -- nounsItemA,
     *         2 -- nounsItemF,
     *         3 -- nounsAmbient
     */
    public String randNoun(int caser, int bound)
    {
        if(caser<0)
            caser =  new Random().nextInt(bound);
        String res;
        switch (caser)
        {
            case 0:
                res = wordMap.get("nounsItemM").getWord();
                break;
            case 1:
                res = wordMap.get("nounsItemA").getWord();
                break;
            case 2:
                res = wordMap.get("nounsItemF").getWord();
                break;
            case 3:
                res = wordMap.get("nounsAmbient").getWord();
                break;
            default:
                res = res = wordMap.get("nounsItemM").getWord();
                break;
        }

        return res;
    }

    public String uppercaseFirstChar(@NotNull String buf)
    {
        if(buf.length()>=2)
        {
            //StringBuilder tmp = new StringBuilder();
            return buf.substring(0,1).toUpperCase() + buf.substring(1);
        }
        else
            return "Нехорошо, нехорошо...";

    }

    int calcOverallWeight()
    {
        int res=0;
        for(int i=0;i<log.size();i++)
            res+=log.get(i);

        return res;
    }
    public void genTemplate(View view)  {

        int random = new Random().nextInt(overallWeight)+1;
        int counter=0;
        wolfCounter++;

        String buf = "ыы, это баг";
        for(int i=0;i<log.size();i++)
        {
            counter+=log.get(i);
            if(counter>=random)
            {
                //System.out.println("counter = " + counter +" random = "+random);
                template = i;
                break;
            }
        }
        String logger = "Сработал шаблон номер " + template +"    Общий вес: "+overallWeight+"\nРандом: "+random +"    Счётчик: "+counter+"\n";
        System.out.println(logger);
        //outputStream.write(logger.getBytes());

        switch (template)
        {
            case 41:
                buf = uppercaseFirstChar(randAdj());
                tmp = buf + " снаружи "+wordMap.get("verbsDone").getWord()+" внутри однажды.";
                break;
            case 40:
                tmp = "Кем бы ты ни "+wordMap.get("verbsBeen").getWord()+", кем бы ты не "+wordMap.get("verbsDone").getWord()+", помни, где ты "+wordMap.get("verbsBeen").getWord()+" и кем ты "+wordMap.get("verbsDone").getWord()+".";
                break;
            case 39:
                tmp = "Лучше "+ randNoun(-1,2)+ " в руках, чем "+randNoun(-1,2)+spc+wordMap.get("nounsContainer").getWord()+end;
                break;
            case 38:
                buf = uppercaseFirstChar(wordMap.get("nounsSubj").getWord());
                tmp = buf+" не "+wordMap.get("Subj").getWord()+cmm+ randNoun(-1,3)+" не "+ randNoun(-1,3)+cmm+randInf(4)+" не "+randInf(4)+" — кто "+randVerb(3)+", тот и "+wordMap.get("verbsSubj").getWord()+end;
                break;
            case 37:
                tmp = "Если "+wordMap.get("nounsSubj").getWord()+"а вовремя не "+wordMap.get("infinitivesSubj").getWord()+", то придётся "+wordMap.get("infinitivesSubj").getWord()+spc+wordMap.get("nounsItemM").getWord()+end;
                break;
            case 36:
                tmp = "Нельзя просто так взять, и " + wordMap.get("infinitivesSubj").getWord()+spc+wordMap.get("nounsItemM").getWord()+end;
                break;
            case 35:
                buf = uppercaseFirstChar(wordMap.get("nounsAmbient").getWord());
                tmp = buf+" — понятие относительное. Особенно, "+wordMap.get("condition").getWord()+" ты "+randVerb(2)+end;
                break;
            case 34:
                buf = uppercaseFirstChar(reEndAdj(wordMap.get("adjectivesHard").getWord(),"ого"));
                tmp = buf+spc+"волка "+wordMap.get("adverbsDeg").getWord()+spc+wordMap.get("adverbsHow").getWord()+spc+wordMap.get("infinitivesDone").getWord()+end;
                break;
            case 33:
                tmp = "Раз в год и "+wordMap.get("nounsItemF").getWord()+" "+wordMap.get("verbsSubj").getWord()+end;
                break;
            case 32:
                tmp = "Даже самая "+ reEndAdj(wordMap.get("adjectivesHard").getWord(),"ая")+spc+wordMap.get("nounsItemF").getWord()+" может "+wordMap.get("infinitivesDone").getWord()+spc+wordMap.get("nounsItemM").getWord()+end;
                break;
            case 31:
                str = new StringBuilder();
                ArrayList<String> kek = new ArrayList<>();
                while(kek.size()<6)
                {
                    String s = (randAdj());
                    if(kek.contains(s))
                        continue;
                    kek.add(s);
                }
                for(int i=0;i<kek.size()-1;i++)
                    str.append(kek.get(i)+", ");
                str.append(kek.get(kek.size()-1));
                tmp = "Неважно кто напротив, важно кто "+str.toString()+end;
                break;
            case 30:
                buf = uppercaseFirstChar(randNoun(-1,2));
                tmp = buf + " на хлеб не намажешь.";
                break;
            case 29:
                buf = uppercaseFirstChar(randAdj());
                tmp = buf+spc+wordMap.get("nounsItemM").getWord()+" — "+wordMap.get("nounsAmbient").getWord()+spc+wordMap.get("nounsContainer").getWord()+end;
                break;
            case 28:
                buf = uppercaseFirstChar(randAdj());
                tmp = buf+spc+wordMap.get("nounsItemM").getWord()+" — горе "+wordMap.get("nounsContainer").getWord()+".";
                break;
            case 27:
                buf = uppercaseFirstChar(wordMap.get("nounsAmbient").getWord());
                tmp = buf+spc+wordMap.get("verbsLearn").getWord()+" "+wordMap.get("nounsContainer").getWord()+end;
                break;
            case 26:
                tmp = "Запомните твари: " + randAdj() + " " + wordMap.get("nounsSubj").getWord() + " " + wordMap.get("nounsContainer").getWord() +spc + wordMap.get("infinitivesDo").getWord() + " не будет.";
                break;
            case 0:
                buf = uppercaseFirstChar(wordMap.get("nounsSubj").getWord());
                tmp = buf + spc + wordMap.get("adverbsCmpL").getWord() + " " + wordMap.get("nounsCmp").getWord() + ", но " + wordMap.get("nounsContainer").getWord() + " не " + wordMap.get("verbsSubj").getWord() + ".";
                break;
            case 1:
                tmp = "Если тебе " + wordMap.get("adverbsHow").getWord() + " " + wordMap.get("infinitivesDo").getWord() + ", значит ты " + wordMap.get("adjectivesHard").getWord() + ".";
                break;
            case 2:
                tmp = "Если " + wordMap.get("nounsSubj").getWord() + " " + wordMap.get("verbsSubj").getWord() + ", то лучше его не " + wordMap.get("infinitivesObj").getWord() + ".";
                break;
            case 3:
                buf = uppercaseFirstChar(wordMap.get("adjectivesHard").getWord());
                tmp = buf + " не тот " + wordMap.get("nounsSubj").getWord() + ", который всех " + wordMap.get("verbsDoneSubj").getWord() + ", а тот, кто хорошо " + wordMap.get("verbsDone").getWord() + ".";
                break;
            case 4:
                String action1 = randVerb(3);
                buf = uppercaseFirstChar(wordMap.get("adjectivesHard").getWord());
                tmp = buf + " " + wordMap.get("nounsSubj").getWord() + " не тот, кто "+randVerb(3)+", а тот, который"+randNot() + action1 + " " + wordMap.get("nounsContainer").getWord() + ".";
                break;
            case 5:
                tmp = "Сколько " + wordMap.get("nounsSubj").getWord() + "а не корми, всё равно " + wordMap.get("nounsContainer").getWord() + " " + wordMap.get("verbsSubj").getWord() + ".";
                break;
            case 6:
                tmp = "Если " + wordMap.get("nounsSubj").getWord() + "у " + wordMap.get("adverbsHow").getWord() + ", это не значит что " + wordMap.get("adverbsHow").getWord() + ", может он просто " + wordMap.get("adjectivesHard").getWord() + ".";
                break;
            case 7:
                tmp = "Неважно кто " +randAdj() + ", важно кто " + randVerb(3) + ".";
                break;
            case 8:
                buf = uppercaseFirstChar(wordMap.get("adverbsAns").getWord());
                tmp =  buf + " что " + wordMap.get("nounsSubj").getWord()+spc+wordMap.get("verbsDoneSubj").getWord() + ", "+wordMap.get("condition").getWord() + " он " + wordMap.get("adjectivesHard").getWord() + ".";
                break;
            case 9:
                buf = uppercaseFirstChar(wordMap.get("adverbsCmp").getWord());
                tmp = buf + " быть " + reEndAdj(wordMap.get("adjectivesHard").getWord(),"м") + " " + wordMap.get("nounsSubj").getWord() + "ом, чем " + reEndAdj(wordMap.get("adjectivesHard").getWord(),"м") + " " + wordMap.get("nounsSubj").getWord() + "ом.";
                break;
            case 10:
                tmp = "Неважно " + wordMap.get("questions").getWord() + ", важно " + wordMap.get("questions").getWord() + ".";
                break;
            case 11:
                tmp = "Неважно " + wordMap.get("questions").getWord() + spc + wordMap.get("nounsSubj").getWord() + " " + wordMap.get("verbsSubj").getWord() + ", важно " + wordMap.get("questions").getWord() + " он " + wordMap.get("verbsSubj").getWord() + ".";
                break;
            case 12:
                tmp = "Запомните твари: " + randAdj() + " " + wordMap.get("nounsSubj").getWord()+ spc + wordMap.get("nounsContainer").getWord() + " не " + wordMap.get("verbsSubj").getWord() + ".";
                break;
            case 13:
                buf = uppercaseFirstChar(wordMap.get("condition").getWord());
                tmp = buf + spc + wordMap.get("nounsSubj").getWord() + "у " + wordMap.get("adverbsHow").getWord() + spc + wordMap.get("infinitivesDo").getWord() +", это не значит, что " + wordMap.get("adverbsHow").getWord() + ", может он просто " + wordMap.get("verbsDone").getWord() + ".";
                break;
            case 14:
                buf = uppercaseFirstChar(wordMap.get("adverbsCmp").getWord());
                tmp = buf + " один раз " + wordMap.get("infinitivesDone").getWord() + ", чем 100 раз " + wordMap.get("infinitivesDone").getWord() + ".";
                break;
            case 15:
                buf = uppercaseFirstChar(wordMap.get("adverbsCmp").getWord());
                tmp = buf + " всего " + wordMap.get("infinitivesDone").getWord() + ", когда ты " + wordMap.get("verbsDone").getWord() + ".";
                break;
            case 16:
                buf = uppercaseFirstChar(wordMap.get("adverbsAns").getWord());
                tmp = buf + " кому " + wordMap.get("adverbsHow").getWord() + ", если никто не " + randVerb(3) + ".";
                break;
            case 17:
                buf = uppercaseFirstChar(wordMap.get("who").getWord());
                tmp = buf + " может " + wordMap.get("infinitivesDone").getWord() + ", когда " + wordMap.get("nounsSubj").getWord() + " " + wordMap.get("verbsSubj").getWord() + " " + wordMap.get("nounsContainer").getWord() + ".";
                break;
            case 18:
                tmp = "Если " + wordMap.get("nounsSubj").getWord() + " хочет " + wordMap.get("infinitivesDone").getWord() + ", то лучше его не " + wordMap.get("infinitivesObj").getWord() + ".";
                break;
            case 19:
                buf = uppercaseFirstChar(wordMap.get("adverbsCmp").getWord());
                tmp = buf + " всего " + wordMap.get("infinitivesDone").getWord() + ", если ты " + wordMap.get("verbsDone").getWord() + ".";
                break;
            case 20:
                tmp = "Лучше один раз " + wordMap.get("infinitivesDone").getWord() + ", чем 100 раз " + wordMap.get("infinitivesDone").getWord() + ".";
                break;
            case 21:
                buf = uppercaseFirstChar(wordMap.get("questions").getWord());
                tmp = buf + spc + wordMap.get("infinitivesDo").getWord() +spc +wordMap.get("nounsItemA").getWord()+ ", если тебе " + wordMap.get("adverbsHow").getWord() + end;
                break;
            case 22:
                buf = uppercaseFirstChar(wordMap.get("infinitivesDo").getWord());
                tmp = buf + " не значит " + wordMap.get("infinitivesDo").getWord() + end;
                break;
            case 23:
                buf = uppercaseFirstChar(wordMap.get("nounsSubj").getWord());
                tmp = buf + spc + wordMap.get("adverbsCmpL").getWord()+spc+ wordMap.get("nounsCmp").getWord()+", но "+ wordMap.get("nounsContainer").getWord() + " не "+wordMap.get("verbsSubj").getWord()+end;
                break;
            case 24:
                tmp = "Чтобы "+wordMap.get("infinitivesDone").getWord()+" — нужно "+wordMap.get("infinitivesDo").getWord()+end;
                break;
            case 25:
                tmp = "Если ты не можешь " + wordMap.get("infinitivesDone").getWord() + ", значит ты " + wordMap.get("adjectivesHard").getWord() + ".";
                break;

            default:
                tmp = Presets.get(new Random().nextInt(Presets.size()));
                break;
        }

        ImageButton btn=findViewById(R.id.toFavorites);
        if(favorite.contains(tmp))
            btn.setActivated(true);
        main.setText(tmp);
        writeToHistory(tmp);
        //history.add(tmp);
    }


    //main.setOnTouchListener(new View.OnTouchListener())

    public void playAUF(View view){
        String toast = " э";
        if(!latestTemplate.equals(tmp) && template != totalTemplates) {
            if (template >= 0 && log.get(template) < 15) {
                log.set(template, log.get(template) + 1);
                reWeightTemplate(template, log.get(template) + 1);
                overallWeight++;
                latestTemplate = tmp;
                toast = "+1 шаблону " + template;
            }
            else
                toast = "Сообщите автору, что этот шаблон топ. А веc больше не увеличится.";

        }
        else
            toast = "не так быстро";
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        //MediaPlayer.create(this,R.raw.auf1).start();
    }

    public void downvote(View view)
    {
        String toast=" э";
        if(!latestTemplate.equals(tmp) && template != totalTemplates) {
            if (template >= 0 && log.get(template) > 3) {
                log.set(template, log.get(template) - 1);
                reWeightTemplate(template, log.get(template) - 1);
                overallWeight--;
                latestTemplate=tmp;
                toast = "-1 шаблону " + template;
            }
            else
                toast = "Сообщите автору, чтобы пофиксил шаблон. А веc больше не увеличится.";
        }
        else
            toast = "не так быстро";
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public void addToFavorites(View view) {
        //Toast.makeText(this,tmp,Toast.LENGTH_LONG).show();
        /*for(int i=0;i<log.size();i++)
            log.set(i,1);
        log.set(template,20);
        overallWeight = 19+log.size();*/
        if(!favorite.contains(tmp) && !tmp.equals("АУФ."))
        {
            if (template >= 0 && log.get(template) < 15 && template != totalTemplates)
            {
                log.set(template, log.get(template) + 1);
                reWeightTemplate(template, log.get(template) + 1);
                overallWeight++;
            }

            writeToFavorites(tmp);
            favorite.add(tmp);
            ImageButton btn=findViewById(R.id.toFavorites);
            String toast="Добавлено в избранное";
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        }
        else{
            String toast="Уже там есть, такие дела";
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        }
    }

    public void openHistory(View view){
        main.setText("Я насчитал " + wolfCounter + " цитат.");
        getHistory();
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        intent.putExtra("currentHistory", history);
        intent.putExtra("currentBgId", currentBgId);
        intent.putExtra("limit", historyLimit);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        LinearLayout bg=findViewById(R.id.background);
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

                startActivity(intent);
                return true;
            case R.id.action_favorite:
                getFavorite();
                Intent intent2 = new Intent(MainActivity.this, FavoriteActivity.class);
                intent2.putExtra("currentFavorite", favorite);
                //intent2.putExtra("database", );
                intent2.putExtra("currentBgId", currentBgId);
                startActivityForResult(intent2,6);
                onActivityResult(6,intent2.getIntExtra("result",0),intent2);
            return true;
            case R.id.action_changeBg:

                currentBgId += 1;
                currentBgId %= backgroundsCount;

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
                return true;
            case R.id.action_info:
                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent_about);
                return true;
            case R.id.action_points:
                Intent intent_points = new Intent(MainActivity.this, PointsActivity.class);
                intent_points.putExtra("points",log);
                startActivity(intent_points);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 6) {

            Bundle arguments = intent.getExtras();
            if(arguments != null)
            {
                boolean favoritesRemoved = (boolean) arguments.get("favoritesRemoved");
                //System.out.println("\n\n\nGOVNO SRABOTALO\n\n\n"+favoritesRemoved+"\n\n\n");
                if(favoritesRemoved)
                {
                    favorite = new ArrayList<>();
                    writeToFavorites("<костыль>");
                   // System.out.println("\n\n\nExtrasBeingPut\n\n\n");
                }
            }
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void setOnStartBg()
    {
        LinearLayout bg=findViewById(R.id.background);
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