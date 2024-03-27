package com.example.stavetime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
    ImageView imageView;
    Bitmap bitmap;
    Mat mat;
    private ActivityResultLauncher<Intent> launcher;

    // The onCreate function is executed as the app opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (OpenCVLoader.initDebug()) Log.d("LOADED", "success");
        else Log.d("LOADED", "error");


        files = findViewById(R.id.files);
        imageView = findViewById(R.id.imageView);
        next = (Button) findViewById(R.id.next);


        // Updated way of using 'startActivityForResult'
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        try {
                            Intent data = result.getData();
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                            imageView.setImageBitmap(bitmap);

                            mat = new Mat();
                            Utils.bitmapToMat(bitmap, mat);

                            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

                            Utils.matToBitmap(mat, bitmap);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        // Listens for the 'files' button.
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                // This is same as we go one activity to another
                //Intent intent = new Intent(MainActivity.this,SecondActivity.class);

                // Rather than calling startActivity(intent) we use this code
                launcher.launch(intent);
            }
        });
//                // !! registerForActivityResult() !!
//                // See below:
//                // https://www.geeksforgeeks.org/how-to-use-activityforresultluncher-as-startactivityforresult-is-deprecated-in-android/


        // Listens for the 'next' button, which switches to the next screen.
        next.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            startActivity(intent);
        });


    }


}