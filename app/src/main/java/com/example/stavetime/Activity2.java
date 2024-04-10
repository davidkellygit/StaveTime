/*
 * Comment explaining the role of Activity2.java
 * */

package com.example.stavetime;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class Activity2 extends AppCompatActivity {

//    Button pause, play;
//    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

//        pdfView = findViewById(R.id.pdfView);

//        // Load PDF
//        File file = new File(getIntent().getStringExtra("pdfPath"));
//        displayPdf(file);

        // Play button Listener:

        // Pause button Listener:
    }


//    // *see ChatGPT
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

};
