package com.example.notificationapp.RetroFitBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitBuilderClass {
    private static Retrofit instance;

    private RetroFitBuilderClass() {

    }

    public static Retrofit getInstance() {
        if(instance==null)
            synchronized (RetroFitBuilderClass.class)
            {
                if (instance == null) {
                    instance = new Retrofit.Builder().baseUrl("http://10.20.4.72:8084")
                            .addConverterFactory(GsonConverterFactory.create()).client(new OkHttpClient()).build();
                }
            }
        return instance;
    }
}
