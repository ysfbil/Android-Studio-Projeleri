package com.ysfbil.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE ="extraScore";
    private static final long COUNTDOWN_IN_MILLIS=30000;

    private static final String KEY_SCORE ="keyScore";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount";
    private static final String KEY_MILLIS_LEFT="keyMillisLeft";
    private static final String KEY_ANSWERED="keyAnswered";
    private static final String KEY_QUESTION_LIST="keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private  long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private  boolean answered;

    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion=findViewById(R.id.textView6);
        textViewScore=findViewById(R.id.textView);
        textViewQuestionCount=findViewById(R.id.textView2);
        textViewCountDown=findViewById(R.id.textView5);
        rbGroup=findViewById(R.id.radioGroup);
        rb1=findViewById(R.id.radioButton);
        rb2=findViewById(R.id.radioButton2);
        rb3=findViewById(R.id.radioButton3);
        buttonConfirmNext=findViewById(R.id.button2);

        textColorDefaultRb=rb1.getTextColors();
        textColorDefaultCd =textViewCountDown.getTextColors();

        if (savedInstanceState==null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList); //listeyi karıştırıyoruzki hep aynı soru gelmesin

            showNextQuestion();
        }
        else {
            questionList=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter=savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion=questionList.get(questionCounter-1);
            score=savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis=savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered=savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered){
                startCountDown();
            }else
            {
                updateCountDownText();
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answered){
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        checkAnswer();
                    }
                    else
                    {
                        Toast.makeText(QuizActivity.this,getResources().getString(R.string.SecenekUyari),Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showNextQuestion();
                }
            }
        });
    }

    private void checkAnswer() {
        answered=true;

        countDownTimer.cancel(); //cevabı kontrol edince sayacı iptal ediyoruz aksi halde sonraki soruda iki sayaç çalışıyor

        RadioButton rbSelected=findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected)+1;
        if (answerNr== currentQuestion.getAnswerNr()){
            score++;
            textViewScore.setText(getResources().getString(R.string.Quiz_textView3)+score);
        }

        showSolution();
    }

    private void showSolution() {

        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr())
        {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.DogruCevap)+"A");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.DogruCevap)+"B");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.DogruCevap)+"C");
                break;
        }

        if (questionCounter<questionCountTotal){
            buttonConfirmNext.setText(getResources().getString(R.string.Quiz_button2));
        }else{
            buttonConfirmNext.setText(getResources().getString(R.string.Quize_buton2_2));
        }

    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter< questionCountTotal){
            currentQuestion =questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;

            textViewQuestionCount.setText(getResources().getString(R.string.Quiz_textView2)+questionCounter+ "/" + questionCountTotal);
            answered=false;
            buttonConfirmNext.setText(getResources().getString(R.string.Quiz_button2_3));

            timeLeftInMillis=COUNTDOWN_IN_MILLIS; //her yeni soru da sayac 30'dan başlatılıyor
            startCountDown();

        }else{
            finishQuiz();
        }


    }

    private void startCountDown() {
        countDownTimer=new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis=l; //burada l 30 saniyeden kalan süreyi veriyor
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer(); //süre dolduysa cevabı kontrol ediyoruz.
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes= (int) ((timeLeftInMillis/1000)/60);
        int seconds= (int) ((timeLeftInMillis/1000)%60);
        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis<10000){ //son 10 saniyede yazı rengini kırmızı yapıyoruz
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);

        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime+2000>System.currentTimeMillis()){
            finishQuiz();
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.CikisUyari), Toast.LENGTH_SHORT).show();
        }

        backPressedTime=System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,score);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_MILLIS_LEFT,timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED,answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);
    }
}