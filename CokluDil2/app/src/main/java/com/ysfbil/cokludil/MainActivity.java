package com.ysfbil.cokludil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView b1,b2,b3;

    TextView textSecim, textDil, textAciklama, dialog_lang;
    int lang_selected;
    RelativeLayout showLangDialog;

    Context context;
    Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        dialog_lang=findViewById(R.id.dialog_language);
        textSecim =findViewById(R.id.textView);
        textDil =findViewById(R.id.textView2);
        textAciklama =findViewById(R.id.textView3);
        showLangDialog=findViewById(R.id.showlangdialog);
        b1=findViewById(R.id.imageView);
        b2=findViewById(R.id.imageView2);
        b3=findViewById(R.id.imageView3);

        if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en"))
        {
            context=LocaleHelper.setLocale(MainActivity.this,"en");
            lang_selected=0;
            b1.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.VISIBLE);
        }
        else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("ar"))
        {
            context=LocaleHelper.setLocale(MainActivity.this,"ar");
            lang_selected=1;
            b1.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.VISIBLE);
            b3.setVisibility(View.INVISIBLE);
        }
        else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("tr"))
        {
            context=LocaleHelper.setLocale(MainActivity.this,"tr");
            lang_selected=2;
            b1.setVisibility(View.VISIBLE);
            b2.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);
        }

        resources=context.getResources();
        dialog_lang.setText(R.string.dil);
        textDil.setText(resources.getString(R.string.dil_degistir));
        textAciklama.setText(resources.getString(R.string.aciklama));
        textSecim.setText(resources.getString(R.string.dil_seciniz));
        setTitle(resources.getString(R.string.app_name));

        showLangDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] Language={"ENGLISH","ARABIC","TÜRKÇE"};
                final int checkItem;
                Log.d("Clicked","Clicked");

                final AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Select a language")
                        .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog_lang.setText(Language[i]);

                                if (Language[i].equals("ENGLISH")){
                                    context=LocaleHelper.setLocale(MainActivity.this,"en");
                                    resources=context.getResources();
                                    lang_selected=0;
                                    b1.setVisibility(View.INVISIBLE);
                                    b2.setVisibility(View.INVISIBLE);
                                    b3.setVisibility(View.VISIBLE);

                                }
                                if (Language[i].equals("ARABIC")){
                                    context=LocaleHelper.setLocale(MainActivity.this,"ar");
                                    resources=context.getResources();
                                    lang_selected=1;
                                    b1.setVisibility(View.INVISIBLE);
                                    b2.setVisibility(View.VISIBLE);
                                    b3.setVisibility(View.INVISIBLE);

                                }
                                if (Language[i].equals("TÜRKÇE")){
                                    context=LocaleHelper.setLocale(MainActivity.this,"tr");
                                    resources=context.getResources();
                                    lang_selected=2;
                                    b1.setVisibility(View.VISIBLE);
                                    b2.setVisibility(View.INVISIBLE);
                                    b3.setVisibility(View.INVISIBLE);

                                }

                                dialog_lang.setText(R.string.dil);
                                textDil.setText(resources.getString(R.string.dil_degistir));
                                textAciklama.setText(resources.getString(R.string.aciklama));
                                textSecim.setText(resources.getString(R.string.dil_seciniz));
                                setTitle(resources.getString(R.string.app_name));
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                    dialogBuilder.create().show();
            }
        });


    }
}