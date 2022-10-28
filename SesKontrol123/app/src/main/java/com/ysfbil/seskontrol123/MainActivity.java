package com.ysfbil.seskontrol123;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView asistanResmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asistanResmi=findViewById(R.id.imageView); //resmi buluyoruz
        final MediaPlayer mp1 = MediaPlayer.create(this, R.raw.hosgeldiniz); //mediaplayer nesneleri oluşturduk
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.komutlar);



        mp1.start(); //hoşgeldiniz sesini oynatıyoruz
        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp1.release(); //bitince hafızdan silip
                mp2.start(); //komutlar sesini oynatıyoruz
            }
        });

        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp2.release();
            } //bitince media nesnesini kaldırıyoruz.
        });


        asistanResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceAutomation();
            } //tıklayınca ses tanıma procedürü çağrılıyor.
        });
    }

    private void voiceAutomation() {
        Intent voice=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); //ses tanıma bileşeni oluşturuldu.
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, //dil modelini web araması yerine serbest model olarak belirledik
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, //dil olarak aygıt varsayılan dilini seçiyoruz
                Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, //tanıma penceresinde çıkmasını istedğimiz mesajları giriyoruz.
                "Kamerayı aç, flaşı aç, çıkış");
        startActivityForResult(voice,1); //ses tanıma penceresini açıyoruz

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK && data!=null) //eğer komut alındıysa
        {
            ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); //sonuçlar alınıyor
            if (arrayList.get(0).toString().toLowerCase().equals("kamerayı aç")) //eğer kamerayı aç komutu alındıysa
            {
                final MediaPlayer mp3 = MediaPlayer.create(this, R.raw.kamera); //media nesnesi oluşturuluyor.
                mp3.start(); //ses dosyası oynatılıyor.

                mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp3.release();
                    } //ses bitince siliniyor
                });

                Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //kamera nesnesi oluştşuruluyor.
                startActivity(camera); //kamera açılıyor.

            }
            else if (arrayList.get(0).toString().toLowerCase(Locale.ROOT).equals("flaşı aç")) //flaşı aç komutu alındıysa
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //eğer android sürümü uyumlu ise
                    CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE); //kamera yönetim nesnesi  oluşturuluyor
                    String cameraId = null;
                    try {
                        cameraId = camManager.getCameraIdList()[0]; //kamera listesi alınıyon
                        camManager.setTorchMode(cameraId, true);   //faş açılıyor
                    } catch (Exception e) {
                        e.printStackTrace(); //hata alınırsa çıktı sağlanıyor
                    }
                    finally {
                        final MediaPlayer mp5 = MediaPlayer.create(this, R.raw.flashac); //ilgli ses yürütme nesnesi oluşturuluyor
                        mp5.start(); //ses oynatılıyor

                        mp5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp5.release();
                            } //ses bitince nesne siliniyor
                        });
                    }


                }
            }
            else if (arrayList.get(0).toString().toLowerCase().equals("flaşı kapat")) //flaşı kapat komutu alınıdysa
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    String cameraId = null;
                    try {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, false);   //flash kapatılıyor
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        final MediaPlayer mp6 = MediaPlayer.create(this, R.raw.flashkapat); //kapatma sesi çalınıyor
                        mp6.start();

                        mp6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp6.release();
                            }
                        });

                    }
                }
            }
            else if (arrayList.get(0).toString().toLowerCase(Locale.ROOT).equals("çıkış")) //çıkış komutu alındıysa
            {
                QuitApp();
            }
            else
            {
                final MediaPlayer mp4 = MediaPlayer.create(this, R.raw.anlasilmadi); //bilinmeyen bir komut ise anlaşılmadı sesi oynatılıyor
                mp4.start();

                mp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp4.release();
                    }
                });
            }
        }
    }

    public void QuitApp() { //uygulamadan çıkılıyor
        MainActivity.this.finish();
        System.exit(0);
    }
}