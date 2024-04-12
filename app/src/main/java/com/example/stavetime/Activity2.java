/*
Activity2.java is the second screen in the app.
It shows where to find the MP3.

The commented out code in this Java class is an attempt
to implement a PDF viewer and MP3 player within the application.
Regrettably this is something I neglected to put enough time into
but for future work, this is something to work on further.

Author: David Kelly
Date: 12th April 2024
*/

package com.example.stavetime;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.icu.util.TimeUnit;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Activity2 extends AppCompatActivity{

//    Button play, pause;
//    private PDFView pdfView;
//    MediaPlayer mediaPlayer;
//    String pathToMP3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

//        MainActivity main = new MainActivity();

//        pathToMP3 = "/storage/emulated/0/Android/data/com.example.stavetime/files/"
//                + main.changeFileExtension(main.pdfName, "mp3").toString();
//
//        play = findViewById(R.id.play);
//        pause = findViewById(R.id.pause);
//
//        File file = new File(pathToMP3);
//
//        while (file.exists() == false) {
//            Toast.makeText(Activity2.this, "Hang tight", Toast.LENGTH_SHORT).show();
//            Toast.makeText(Activity2.this, "This takes 30-40 seconds", Toast.LENGTH_SHORT).show();
//        }
//        setPlayer();
//
//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mediaPlayer == null) {
//                    setPlayer();
//                }
//                else {
//                    mediaPlayer.start();
//                }
//            }
//        });
//
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mediaPlayer == null) {
//                    Toast.makeText(getApplicationContext(), "Media Player Null", Toast.LENGTH_SHORT).show();
//                }
//                mediaPlayer.pause();
//
//            }
//        });

//        pdfView = findViewById(R.id.pdfView);

//        // Load PDF
//        File file = new File(getIntent().getStringExtra("pdfPath"));
//        displayPdf(file);

    }


//    private void displayPdf(File file) {
//        pdfView.fromFile(file)
//                .defaultPage(0)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .spacing(10) // in dp
//                .pageFitPolicy(FitPolicy.BOTH)
//                .load();
//    }

//    public void setPlayer() {
//
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioAttributes(
//                    new AudioAttributes.Builder()
//                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                            .setUsage(AudioAttributes.USAGE_MEDIA)
//                            .build());
//
//
//            try {
//                mediaPlayer.setDataSource(pathToMP3);
//                mediaPlayer.prepare();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//        }
//    }
}
