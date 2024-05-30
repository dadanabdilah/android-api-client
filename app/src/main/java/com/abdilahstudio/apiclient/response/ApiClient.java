package com.abdilahstudio.apiclient.response;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static SharedPreferences preferences;
    public static String BASE_URL = null;
    private static Retrofit retrofit = null;

    // Metode untuk menginisialisasi SharedPreferences dan BASE_URL
    public static void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("ApiClient", Context.MODE_PRIVATE);
        }
        BASE_URL = preferences.getString("endPointApi", "http://192.168.0.181/"); // Default URL jika belum diatur
    }

    // Metode untuk mendapatkan instance Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {
            if (BASE_URL == null) {
                throw new IllegalStateException("BASE_URL is not initialized. Call ApiClient.init(context) first.");
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}