package com.ysfbil.apps;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ysfbil.apps.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private  static final int DATABASE_VERSION = 1; //veri tabanında değişiklik yapınca bu sayı arttırılır böylece update fonksiyonu çalışır. Burada drop ile tablo silinip yeni şekle göre yeniden oluşturulmaktadır
    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER)";
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillQuestionTable();
    }

    private void fillQuestionTable() {
        Question q1=new Question("Cevabı A olan sorudasınız. Cevap nedir sizce?","A","B","C",1);
        addQuestion(q1);
        Question q2=new Question("Cevabı B olan sorudasınız. Cevap nedir sizce?","A","B","C",2);
        addQuestion(q2);
        Question q3=new Question("Cevabı C olan sorudasınız. Cevap nedir sizce?","A","B","C",3);
        addQuestion(q3);
        Question q4=new Question("Cevabı A olan sorudasınız. Cevap nedir sizce?","A","B","C",1);
        addQuestion(q4);
        Question q5=new Question("Cevabı B olan sorudasınız. Cevap nedir sizce?","A","B","C",2);
        addQuestion(q5);

    }

    private void addQuestion(Question question){
        ContentValues cv=new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionTable.COLUMN_ANSWER_NR,question.getAnswerNr());
        db.insert(QuestionTable.TABLE_NAME,null,cv);

    }

    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions()
    {
        ArrayList<Question> questionList = new ArrayList<>();
        db=getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM "+QuestionTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do{
                Question question=new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            }while (c.moveToNext());
        }

        c.close();
        return  questionList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+QuestionTable.TABLE_NAME);
        onCreate(db);
    }
}
