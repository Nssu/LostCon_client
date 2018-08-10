package lostcon.nssu.example.com.lostcon.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

import lostcon.nssu.example.com.lostcon.R;

public class SearchActivity extends NMapActivity {

    private ViewGroup mapLayout;
    private NMapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initMap();
    }

    public void initMap(){
        mapLayout = findViewById(R.id.mapLayout);

        mMapView = new NMapView(this);
        mMapView.setClientId(getResources().getString(R.string.NAVER_API_KEY)); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.setScalingFactor(1.5f);
        mMapView.requestFocus();

        mapLayout.addView(mMapView);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
