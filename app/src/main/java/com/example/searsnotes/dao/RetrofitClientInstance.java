package com.example.searsnotes.dao;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static final String BASE_URL = "https://wordsapiv1.p.mashape.com/";
    private static Retrofit retrofitInstance;
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request originalRequest =chain.request();
            Request requestWithKey = originalRequest.newBuilder().header("X-Mashape-Key","c2b4fe5d00msh2cbc510d6d62e14p170844jsn0a0e97e3ff7e").build();
            return chain.proceed(requestWithKey);
        }
    }).addInterceptor(loggingInterceptor).build();

    public static Retrofit getRetrofitInstance(){
        if(retrofitInstance==null){
            synchronized (RetrofitClientInstance.class){
                retrofitInstance =new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient). build();
            }
        }
        return retrofitInstance;
    }
}
