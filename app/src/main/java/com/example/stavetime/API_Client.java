/*
This class provides the app with the necessary information
to send files to the server (hosted on my laptop).

It uses Retrofit in order to communicate with the API,
and to use CRUD operations.

Author: David Kelly
Date: 12th April 2024
*/

package com.example.stavetime;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class API_Client {

    public class ApiResponse {

        public ApiResponse() {
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
