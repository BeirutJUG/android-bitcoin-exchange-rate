package com.murex.bitcoinrate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.murex.bitcoinrate.network.BitcoinRate;
import com.murex.bitcoinrate.network.NetworkClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mExchangeRateTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExchangeRateTextView = findViewById(R.id.textView);
        mProgressBar = findViewById(R.id.progressBar);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatestBitcoinRates();
            }
        });
    }

    private void setCurrentExchangeRate(double rateInUsd) {
        mExchangeRateTextView.setText("1 Bitcoin = $" + rateInUsd);
    }

    /**
     * Send an api call to fetch the latest bitcoin rates
     */
    private void getLatestBitcoinRates() {
        showLoader();

        NetworkClient.getLatestRates().enqueue(new Callback<Map<String, BitcoinRate>>() {
            @Override
            public void onResponse(Call<Map<String, BitcoinRate>> call, Response<Map<String, BitcoinRate>> response) {
                Log.d(TAG, "Successful!");
                Map<String, BitcoinRate> rates = response.body();
                BitcoinRate rateInUsd = rates.get("USD");

                setCurrentExchangeRate(rateInUsd.getLast());
                hideLoader();
            }

            @Override
            public void onFailure(Call<Map<String, BitcoinRate>> call, Throwable t) {
                Log.d(TAG, "Failed", t);
                hideLoader();
            }
        });
    }

    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
    }
}
