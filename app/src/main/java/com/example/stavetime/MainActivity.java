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

import java.io.IOException;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

public class MainActivity extends AppCompatActivity {

    // The attributes are defined here.
    Button files, /*, camera, */next;
    ImageView imageView;
    Bitmap bitmap;
    Mat mat;
    int FILES_CODE = 100;
    //int CAMERA_CODE = 101;
    private ActivityResultLauncher<Intent> launcher;

    // The onCreate function is executed as the app opens.
    // The buttons begin listening for clicks.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(OpenCVLoader.initDebug()) Log.d("LOADED", "success");
        else Log.d("LOADED", "error");

        getPermission();

        //camera = findViewById(R.id.camera);
        files = findViewById(R.id.files);
        imageView = findViewById(R.id.imageView);
        next = (Button) findViewById(R.id.next);

        // Hide the 'next' button on launch.
//        next.setVisibility(View.INVISIBLE);
//        // make 'files' and 'camera' button invisible
//        files.setVisibility(View.VISIBLE);
//        camera.setVisibility((View.VISIBLE));

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
                        }

                        catch (IOException e) {
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

        // Listens for the 'next' button, which switches to the next screen.
        next.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            startActivity(intent);
        });

//        files.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, FILES_CODE);
//
//                // !! registerForActivityResult() !!
//                // See below:
//                // https://www.geeksforgeeks.org/how-to-use-activityforresultluncher-as-startactivityforresult-is-deprecated-in-android/
//            }
//        });

//        camera.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, CAMERA_CODE);
//            }
//        });

    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Check if the 'files' button has been pressed.
//        if(requestCode==FILES_CODE && data!=null){
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                imageView.setImageBitmap(bitmap);
//
//                mat = new Mat();
//                Utils.bitmapToMat(bitmap, mat);
//
//                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
//
//                Utils.matToBitmap(mat, bitmap);
//                imageView.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
////            // The 'Next' button now appears, and the 'Files' and 'Camera' button disappear.
////            files.setVisibility(View.INVISIBLE);
////            camera.setVisibility(View.INVISIBLE);
////            next.setVisibility(View.VISIBLE);
//
//        }
//
////        // Check if the 'camera' button has been pressed.
////        if(requestCode == CAMERA_CODE && data != null){
////            bitmap = (Bitmap) data.getExtras().get("data");
////
////            mat = new Mat();
////            Utils.bitmapToMat(bitmap, mat);
////
////            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
////
////            Utils.matToBitmap(mat, bitmap);
////            imageView.setImageBitmap(bitmap);
////
////            // The 'Next' button now appears, and the 'Files' and 'Camera' button disappear.
////            next.setVisibility(View.VISIBLE);
////            files.setVisibility(View.GONE);
////            camera.setVisibility(View.GONE);
////        }
//
//    }



    void getPermission(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                requestPermissions(new String(){Manifest.permission.CAMERA}, 102);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the camera permission is not granted yet
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request camera permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 102);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 102 && grantResults.length > 0){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getPermission();
            }
        }
    }

//    @SpringBootApplication
//    public class Main {
//        public static void main(String[] args) {
//            SpringApplication.run(Main.class, args);
//        }
//    }

}