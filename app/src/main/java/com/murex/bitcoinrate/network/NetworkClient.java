package com.murex.bitcoinrate.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://blockchain.info/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Call<Map<String, BitcoinRate>> getLatestRates() {
        return retrofit.create(BlockchainService.class).fetchLatestRates();
    }
}
