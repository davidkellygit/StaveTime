/*
* Comment explaining the role of MainActivity.java
* */

package com.example.stavetime;

import static com.example.stavetime.API_Client.client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.os.FileUtils;
import android.os.Environment;
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

import org.apache.commons.io.FileUtils;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // The attributes are defined here.
    Button selectScore, next;
    TextView selectedPDF;
    private ActivityResultLauncher<Intent> launcher;
    String pdfName;
    Uri pdfUri;


    // The onCreate function is executed like a 'main' function.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) Log.d("LOADED", "success");
        else Log.d("LOADED", "error");

        selectScore = findViewById(R.id.selectScore);
        next = (Button) findViewById(R.id.next);
        selectedPDF = findViewById(R.id.selectedPDF);

        // The launcher allows a user to select a PDF from the phone's storage.
        // This is run when the 'selectScore' button is pressed.
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            pdfUri = data.getData();
                            pdfName = getFileName(pdfUri);
                            selectedPDF.setText(pdfName);

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No music selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Listens for the 'selectScore' button.
        selectScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectScoreIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                selectScoreIntent.setType("application/pdf"); // Limits user's choice to a PDF only.
                launcher.launch(selectScoreIntent);
            }
        }); // End selectScore.setOnClickListener

        // Listens for the 'next' button.
        // This will switch to the next screen if a PDF has been selected.
        next.setOnClickListener(v -> {

            if (pdfName != null) {

                // Store the actual PDF file in the variable 'pdfUri'.
                File file = new File(pdfUri.getPath());

                // For testing: Print the name of the file path to the logs.
                Log.v("File Path", pdfUri.getPath());

                // Create RequestBody instance from file.
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(pdfUri)),
                                file
                        );

                // MultipartBody.Part is used to send the actual file name.
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "pdf",
                        file.getName(),
                        requestFile
                );

                // add another part within the multipart request
                String descriptionString = "This is the description :)";
                RequestBody description = RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        descriptionString
                );

                // finally, execute the request
                Call<ResponseBody> call = client.uploadFile(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        Log.v("Upload", "success");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                }); // End call.enqueue


                Intent nextButtonIntent = new Intent(MainActivity.this, Activity2.class);
                startActivity(nextButtonIntent);
            }
            else {
                Toast.makeText(MainActivity.this, "No music selected", Toast.LENGTH_SHORT).show();
            }
        }); // End next.setOnClickListener

    } // End OnCreate()


    // This function returns the file name of a PDF.
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

