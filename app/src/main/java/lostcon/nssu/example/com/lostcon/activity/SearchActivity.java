package lostcon.nssu.example.com.lostcon.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.map.NMapPOIflagType;
import lostcon.nssu.example.com.lostcon.map.NMapViewerResourceProvider;

public class SearchActivity extends NMapActivity {

    private LinearLayout mapLayout;
    private NMapView mMapView;
    private NMapController mMapController;
    private NMapResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initMap();
    }

    public void initMap(){
        //네이버 지도를 넣기 위한 LinearLayout 컴포넌트
        mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
        //네이버 지도 객체 생성(xml에 없이)
        mMapView = new NMapView(this);
        //네이버 지도 객체에 APIKEY 지정
        mMapView.setClientId(getResources().getString(R.string.NAVER_API_KEY));
        //지도를 터치할 수 있도록 옵션 활성화
        mMapView.setClickable(true);
        //확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
        mMapView.setBuiltInZoomControls(true, null);

        //지도 객체로부터 컨트롤러 추출
        mMapController = mMapView.getMapController();


        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.setScalingFactor(1.5f);
        mMapView.requestFocus();
        //생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
        mapLayout.addView(mMapView);

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
        poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);


        // show all POI data
       // poiDataOverlay.showAllPOIdata(0);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
