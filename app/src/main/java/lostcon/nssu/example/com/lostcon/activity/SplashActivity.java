package lostcon.nssu.example.com.lostcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import lostcon.nssu.example.com.lostcon.R;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mHandler.postDelayed(mStartRunnable, 1000);
    }

    private Runnable mStartRunnable = () -> {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    };
}
