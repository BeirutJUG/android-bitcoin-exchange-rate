package com.murex.bitcoinrate;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
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

    private TextView mLastCheckedTimeTextView;
    private TextView mExchangeRateTextView;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler();

    private long mLastCheckedTime = -1;
    private double mBitcoinRate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExchangeRateTextView = findViewById(R.id.textView);
        mLastCheckedTimeTextView = findViewById(R.id.textView2);
        mProgressBar = findViewById(R.id.progressBar);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatestBitcoinRates();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLastCheckedTime = getPreferences(MODE_PRIVATE).getLong("lastChecked", -1);
        mBitcoinRate = getPreferences(MODE_PRIVATE).getFloat("bitcoinRate", -1);

        if(mLastCheckedTime != -1 && mBitcoinRate != -1) {
            setCurrentExchangeRate(mBitcoinRate);
            startLastCheckedTimeCounter(mLastCheckedTime);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        getPreferences(MODE_PRIVATE)
                .edit()
                .putLong("lastChecked", mLastCheckedTime)
                .putFloat("bitcoinRate", (float) mBitcoinRate)
                .commit();
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
                startLastCheckedTimeCounter(System.currentTimeMillis());

                hideLoader();
            }

            @Override
            public void onFailure(Call<Map<String, BitcoinRate>> call, Throwable t) {
                Log.d(TAG, "Failed", t);
                hideLoader();
            }
        });
    }

    /**
     * Update last checked time counter every second starting startTime
     * @param startTime
     */
    private void startLastCheckedTimeCounter(final long startTime) {
        mLastCheckedTime = startTime;

        // Stop counting if handler has already been counting
        mHandler.removeCallbacksAndMessages(null);

        // Update last checked time every second
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String text = "last checked " + DateUtils.getRelativeTimeSpanString(startTime, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                mLastCheckedTimeTextView.setText(text);

                // Repeat
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void setCurrentExchangeRate(double rateInUsd) {
        mBitcoinRate = rateInUsd;
        mExchangeRateTextView.setText("1 Bitcoin = $" + rateInUsd);
    }


    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
    }
}
