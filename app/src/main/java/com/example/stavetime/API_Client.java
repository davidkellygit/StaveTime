/*
* Comment explaining the role of API_Client.java
* */

package com.example.stavetime;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class API_Client {

    public class ApiResponse {
        private String text;

        public ApiResponse() {
        }
        public String getText() {
            return text;
        }
    }

    // Defines the API - Tells the client all the methods in FastAPI.
    public interface ApiClient {
        //Using for testing
        @GET("/users/{user}")
        Call<ApiResponse> testApi(@Path("user") String user);

        // Method to upload a PDF file
        @Multipart
        @POST("/uploadfile/")
        Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

        // Method to download an MP3 file
        @GET("/downloadfile/")
        Call<ResponseBody> downloadFile();

    }

    static String API_BASE_URL = "http://10.0.2.2:8080";

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS);

    static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    );

    static Retrofit retrofit =
            builder
                    .client(
                            httpClient.build()
                    )
                    .build();

    static ApiClient client =  retrofit.create(ApiClient.class);
}
