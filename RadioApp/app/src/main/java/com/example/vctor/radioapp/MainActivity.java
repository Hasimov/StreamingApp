package com.example.vctor.radioapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageButton btn, btnface, btntwitter, btninsta;
    Boolean aBoolean;
    //private String STREAM_URL ="http://radioscoop.hu:80/live.mp3";
    private String STREAM_URL ="http://62.210.209.179:8027";
    //private String STREAM_URL = "http://195.55.74.212/cope/rockfm.mp3?GKID=b803794ee76d11e4b84e00163ea2c744&fspref=aHR0cDovL3BsYXllci5yb2NrZm0uZm0v";
    private MediaPlayer mPlayer;
    //////////////////////////////////////////////////.. Notificacion simple
    int NOTIF_ALERTA_ID = 001 ;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    ///////////////////////////////////////////////////..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (ImageButton)findViewById(R.id.imageButton);
        btnface = (ImageButton)findViewById(R.id.imageButton1);
        btntwitter = (ImageButton)findViewById(R.id.imageButton2);
        btninsta = (ImageButton)findViewById(R.id.imageButton3);


        mPlayer=new MediaPlayer();
        //para establecer el canal del audio (alarmas, musica, llamadas) y asi poder modificar el vol corrcto.
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        if(mPlayer.isPlaying()==false){
            aBoolean = false;
        }else{
            aBoolean = true;
            //btn.setText("Stop");
            btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pausebutton));
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aBoolean==false){

                    //btn.setText("Stop");
                    btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pausebutton));
                    aBoolean = true;


                    try{

                        Toast.makeText(getApplicationContext(),
                                "Conectando con la radio, espere unos segundos...",
                                Toast.LENGTH_LONG).show();

                        //////////////////////////////////.. notificacion simple
                        mBuilder =
                                new NotificationCompat.Builder(MainActivity.this)
                                        .setSmallIcon(R.drawable.difussion)
                                        .setLargeIcon((((BitmapDrawable)getResources()
                                                .getDrawable(R.drawable.radio)).getBitmap()))
                                        .setContentTitle("Formula Mix")
                                        .setContentText("Escuchando radio en vivo..")
                                        //.setTicker("RadioApp")
                                        ;
                        //2 parte -prueba comentando toda esta parte para que al pulsar no haga nada

//                        Intent notIntent =
//                                new Intent(MainActivity.this, MainActivity.class);
//
//                        //prueba para ver si cuando pulsas en notificicaciones no se reinicia el activity (no funciona)
//                        notIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        //notIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
//
//                        PendingIntent contIntent =
//                                PendingIntent.getActivity(
//                                        MainActivity.this, 0, notIntent, 0);
//
//                        mBuilder.setContentIntent(contIntent);
//                        //prueba para ver si no se puede eliminar la notificacion (funciona)
//                        mBuilder.setOngoing(true);

                        //3 parte

                        mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
                        /////////////////////////////////

                        mPlayer.reset();
                        //para que siga reproduciendo con la pantalla apagada
                        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                        mPlayer.setDataSource(STREAM_URL);
                        mPlayer.prepareAsync();

                        mPlayer.setOnPreparedListener(new MediaPlayer.
                                OnPreparedListener(){
                            @Override
                            public void onPrepared(MediaPlayer mp){
                                mp.start();

                            }
                        });

                    } catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error al conectar con la radio", Toast.LENGTH_LONG).show();

                    }



                }else {
                    //btn.setText("Start");
                    btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.playbutton));
                    aBoolean = false;

                    mPlayer.stop();

                    /*if(mPlayer!=null) {
                        mPlayer.release();
                        mPlayer = null;
                    }*/

                    ///////////////////////////////////////////..notificacion simple
                    mNotificationManager.cancel(NOTIF_ALERTA_ID);
                    ////////////////////////////////////////////


                }




            }
        });

        btnface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btntwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // mediaLink is something like "https://instagram.com/p/6GgFE9JKzm/" or
                    // "https://instagram.com/_u/sembozdemir"
                    Uri uri = Uri.parse("https://twitter.com/MixFormula");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    intent.setPackage("com.twitter.android");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error al conectar con Twitter", Toast.LENGTH_LONG).show();
                }

            }
        });

        btninsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // mediaLink is something like "https://instagram.com/p/6GgFE9JKzm/" or
                    // "https://instagram.com/_u/sembozdemir"
                    Uri uri = Uri.parse("http://instagram.com/_u/formulamixfm");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error al conectar con Instagram", Toast.LENGTH_LONG).show();
                }



            }
        });
    }

    @Override
    protected void onDestroy() {
        mNotificationManager.cancel(NOTIF_ALERTA_ID);
        super.onDestroy();
    }
}
