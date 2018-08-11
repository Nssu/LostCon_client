package lostcon.nssu.example.com.lostcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
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
    private NMapView.OnMapStateChangeListener onMapViewStateChangeListener;
    private static final String LOG_TAG = "SearchActivity";

    private DrawerLayout layout_drawer;
    private ImageView menu_button;

    private int menu_select = 1;
    LinearLayout[] menu;
    LinearLayout[] menu_check ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        initMap();
        setClickListener();


    }
    public void init(){
        layout_drawer = (DrawerLayout)findViewById(R.id.layout_drawer);
        menu = new LinearLayout[]{
                (LinearLayout)findViewById(R.id.menu_home),
                (LinearLayout)findViewById(R.id.menu_search),
                (LinearLayout)findViewById(R.id.menu_chat)
        };
        menu_check = new LinearLayout[]{
                (LinearLayout)findViewById(R.id.menu_home_check),
                (LinearLayout)findViewById(R.id.menu_search_check),
                (LinearLayout)findViewById(R.id.menu_chat_check)
        };
        menu_button = (ImageView)findViewById(R.id.menu_button);

        menu[1].setBackgroundColor(getResources().getColor(R.color.blue_trans));
        menu_check[1].setBackgroundColor(getResources().getColor(R.color.check_blue));
        menu[0].setBackgroundColor(getResources().getColor(R.color.White));
        menu_check[0].setBackgroundColor(getResources().getColor(R.color.White));
    }


    public void setClickListener(){
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_drawer.openDrawer(Gravity.LEFT);
            }
        });
        menu[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_select != 0) {
                    layout_drawer.closeDrawer(Gravity.LEFT);
                }
                finish();

            }
        });
        menu[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_select != 2) {
                    layout_drawer.closeDrawer(Gravity.LEFT);
                }
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        //생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
        mapLayout.addView(mMapView);






                // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        //맵 중심 위치 지정
        mMapController.setMapCenter(new NGeoPoint(127.0630205, 37.5091300), 11);

        // show all POI data
       // poiDataOverlay.showAllPOIdata(0);
    }

    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success
            mMapController.setMapCenter(new NGeoPoint(127.0630205, 37.5091300), 11);
        } else { // fail
            Log.e(LOG_TAG, "onMapInitHandler: error=" + errorInfo.toString());
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
