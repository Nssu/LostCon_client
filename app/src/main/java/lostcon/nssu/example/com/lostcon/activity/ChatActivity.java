package lostcon.nssu.example.com.lostcon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import lostcon.nssu.example.com.lostcon.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
