package com.example.stavetime;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // The attributes are defined here.
    Button files, next;
    TextView selectedPDF;
    ImageView imageView;
    private ActivityResultLauncher<Intent> launcher;
    boolean scoreSelected = false;


    // The onCreate function is executed like a 'main' function.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) Log.d("LOADED", "success");
        else Log.d("LOADED", "error");


        files = findViewById(R.id.files);
        next = (Button) findViewById(R.id.next);
        selectedPDF = findViewById(R.id.selectedPDF);

        // The launcher allows a user to select a PDF from their phone's storage.
        // This is run when the 'files' button is pressed.
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri pdfUri = data.getData();
                            String pdfName = getFileName(pdfUri);
                            selectedPDF.setText(pdfName);

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No PDF selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Listens for the 'files' button.
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filesIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                filesIntent.setType("application/pdf");
                launcher.launch(filesIntent);
            }
        });

        // Listens for the 'next' button, which switches to the next screen.
        next.setOnClickListener(v -> {
            Intent nextIntent = new Intent(MainActivity.this, Activity2.class);
            startActivity(nextIntent);
        });


    }



    // This function reads the file name of the user's chosen PDF.
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}