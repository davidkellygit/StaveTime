/*
This class is the brains of the operation.
In this MainActivity, there are two buttons:
1. Select a score
2. Next

The 'select a score' button allows the user to choose
a music score from their device in PDF format.

The 'next' button triggers a call to the server (hosted
on my laptop). The user's chosen PDF gets sent to the server,
and the optical music recognition takes place there.

Once the 'next button has been hit, the program moves
on to the next screen: Activity2.java

Author: David Kelly
Date: 12th April 2024
*/

package com.example.stavetime;

import static com.example.stavetime.API_Client.client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.OpenableColumns;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Attributes defined here.
    Button selectScore, next;
    TextView selectedPDF;
    ActivityResultLauncher<Intent> launcher;
    static String pdfName;
    Uri pdfUri;

    // The onCreate function is executed like a 'main' function;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectScore = (Button) findViewById(R.id.selectScore);
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
        ); // End launcher


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

            if (pdfUri != null) {

                //
                try {
                    InputStream inputstream = getContentResolver().openInputStream(pdfUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                // Print the name of the file path to the logs.
                Log.v("File Path", pdfUri.getPath());

                // Create RequestBody instance from file.
                RequestBody requestFile =
                        new ContentUriRequestBody(getContentResolver(), pdfUri);

                // MultipartBody.Part is used to send the actual file name.
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "file",
                        pdfName,
                        requestFile
                );

                // add another part within the multipart request
                String descriptionString = "This is the description :)";
                RequestBody description = RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        descriptionString
                );

                // Execute the post request for the PDF.
                Call<ResponseBody> call = client.uploadFile(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.v("Upload", "success");
                        Log.d("Contents", response.body().toString());

                        boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                }); // End call.enqueue

                // Move on to the next screen.
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

    } // End getFileName()

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + changeFileExtension(pdfName, "mp3"));
            Log.d("File Path", futureStudioIconFile.getAbsolutePath());

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("File Download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    } // End writeResponseBodyToDisk()


    // This function changes the file extension of a given file name.
    public static String changeFileExtension(String filename, String newExtension) {

        int lastDotIndex = filename.lastIndexOf(".");

        if (lastDotIndex == -1) {
            // If the filename doesn't contain any '.', add the new extension
            return filename + "." + newExtension;
        }
        else {
            // Otherwise, replace the existing extension with the new one
            return filename.substring(0, lastDotIndex) + "." + newExtension;
        }

    } // End changeFileExtension()

} // End class MainActivity

