package com.ysfbil.cokludil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView b1,b2,b3;


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



        b1 = findViewById(R.id.imageView);  //Türkçe Buton
        b2 = findViewById(R.id.imageView2);  //Arapça Buton
        b3 =  findViewById(R.id.imageView3);  //İnglizce Buton

        b1.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getApplicationContext(), R.string.dil_degistir, Toast.LENGTH_LONG).show();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("ar");  //locale de yaptık. Artık değişkenler values-de paketinden alınacak
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "لغتك العربية", Toast.LENGTH_LONG).show();

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                Locale locale = new Locale("en");  //locale en yaptık. Artık değişkenler values-en paketinden alınacak
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "Your language is English", Toast.LENGTH_LONG).show();

            }
        });



    }
}