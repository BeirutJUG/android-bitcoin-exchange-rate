package com.murex.bitcoinrate.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BlockchainService {
    @GET("/ticker")
    Call<Map<String, BitcoinRate>> fetchLatestRates();
}
