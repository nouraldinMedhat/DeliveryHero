package com.app.sealteamdelivery.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


        retrofit = new Retrofit.Builder()
                //.baseUrl("http://api.sealteamdelivery.tk/")
                .baseUrl("https://api.sealhero.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        return retrofit;
    }

}
