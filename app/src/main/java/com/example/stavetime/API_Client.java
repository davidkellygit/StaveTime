package com.example.stavetime;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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
        @GET("/users/{user}")
        Call<ApiResponse> testApi(@Path("user") String user);
    }

    static String API_BASE_URL = "http://10.0.2.2:8080";

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

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
