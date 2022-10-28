package com.ysfbil.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ=1;
    public  static final String SHARED_PREFS="sharedPrefs";
    public static final String KEY_HIGHSCORE="keyHighscore";

    private TextView textViewHighScore;
    private  TextView textTurkce,textEnglish;
    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Locale locale = Locale.getDefault(); // Sayfayı yüklemeden önce default locale alıyoruz ve sayfayı ona göre yüklüyoruz.
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());



        setContentView(R.layout.activity_main);

        textTurkce = findViewById(R.id.textTurkce);  //Türkçe Buton
        textEnglish = findViewById(R.id.textEnglish);  //Arapça Buton

        textEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("en");  //locale en yaptık. Artık değişkenler values-en paketinden alınacak
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Active Language is English", Toast.LENGTH_LONG).show();
            }
        });

        textTurkce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale(""); //locali default locale yani türkçe yaptık. Artık değişkenler values paketinden alınacak
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Aktif dil Türkçe", Toast.LENGTH_LONG).show();

            }
        });

        textViewHighScore=findViewById(R.id.textView4);
        loadHighScore();

        Button buttonStartQuiz =findViewById(R.id.button);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }

    private void startQuiz() {
        Intent intent=new Intent(MainActivity.this,QuizActivity.class);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

   @Override
    protected  void onActivityResult(int requestCode,int resultCode ,Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if (requestCode==REQUEST_CODE_QUIZ){
           if (resultCode==RESULT_OK){
               int score=data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
               if (score>highScore){
                   updateHighScore(score);
               }
           }
       }
   }

   private  void loadHighScore(){
        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highScore=prefs.getInt(KEY_HIGHSCORE,0);
        textViewHighScore.setText(getResources().getString(R.string.Main_textView4)+highScore);
   }

    private void updateHighScore(int score) {
        highScore=score;
        textViewHighScore.setText(getResources().getString(R.string.Main_textView4)+highScore);

        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor =prefs.edit();
        editor.putInt(KEY_HIGHSCORE,highScore);
        editor.apply();
    }


}