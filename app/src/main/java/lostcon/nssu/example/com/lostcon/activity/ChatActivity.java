package lostcon.nssu.example.com.lostcon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import lostcon.nssu.example.com.lostcon.R;

public class ChatActivity extends AppCompatActivity {

    RelativeLayout regist_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        regist_layout = (RelativeLayout)findViewById(R.id.regist_layout);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
