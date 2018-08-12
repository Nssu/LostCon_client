package lostcon.nssu.example.com.lostcon.activity;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.adapter.ChatAdapter;
import lostcon.nssu.example.com.lostcon.common.Constants;
import lostcon.nssu.example.com.lostcon.map.NMapPOIflagType;
import lostcon.nssu.example.com.lostcon.map.NMapViewerResourceProvider;
import lostcon.nssu.example.com.lostcon.model.Chat;
import lostcon.nssu.example.com.lostcon.util.SoftKeyboard;

public class SearchActivity extends NMapActivity {

    private LinearLayout mapLayout;
    private LinearLayout layout_up;
    private NMapView mMapView;
    private NMapController mMapController;
    private NMapResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;
    private NMapView.OnMapStateChangeListener onMapViewStateChangeListener;
    private static final String LOG_TAG = "SearchActivity";

    private DrawerLayout layout_drawer;
    private ImageView menu_button;
    private Button chat_submit;
    private EditText chat_edit;
    private LinearLayout chat_sliding;

    //recycler
    RecyclerView recycler_chat;
    RecyclerView.Adapter mAdapter;
    ArrayList<Chat> chat_list;

    //gps
    LocationManager locationManager;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    LocationListener locationListener;
    private String lng;
    private String lat;
    private BroadcastReceiver receiver;
    private Socket socket;

    private int menu_select = 1;
    LinearLayout[] menu;
    LinearLayout[] menu_check;
    public Handler mHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        initGps();
        initMap();
        setClickListener();
        initRecyclerVIew();
        setKeyboard();

        Handler mHandler = new Handler();
        mHandler2 = new Handler();
        mHandler.postDelayed(mStartRunnable, 5000);

    }

    public void init() {
        layout_up = (LinearLayout)findViewById(R.id.layout_up);
        //gps사용할 수 있는 환경을 가져옴(null이라면 gps 사용 불가지역)
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //gps 및 네트워크 사용 가능한지 확인
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        recycler_chat = (RecyclerView) findViewById(R.id.recycler_chat);
        chat_sliding = (LinearLayout) findViewById(R.id.chat_sliding);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_submit = (Button) findViewById(R.id.chat_submit);
        layout_drawer = (DrawerLayout) findViewById(R.id.layout_drawer);

        menu = new LinearLayout[]{
                (LinearLayout) findViewById(R.id.menu_home),
                (LinearLayout) findViewById(R.id.menu_search),
                (LinearLayout) findViewById(R.id.menu_chat)
        };
        menu_check = new LinearLayout[]{
                (LinearLayout) findViewById(R.id.menu_home_check),
                (LinearLayout) findViewById(R.id.menu_search_check),
                (LinearLayout) findViewById(R.id.menu_chat_check)
        };
        menu_button = (ImageView) findViewById(R.id.menu_button);

        menu[1].setBackgroundColor(getResources().getColor(R.color.blue_trans));
        menu_check[1].setBackgroundColor(getResources().getColor(R.color.check_blue));
        menu[0].setBackgroundColor(getResources().getColor(R.color.White));
        menu_check[0].setBackgroundColor(getResources().getColor(R.color.White));

        receiver = new Receiver();
        IntentFilter filter = new IntentFilter("service");
        registerReceiver(receiver, filter);
    }

    public void initGps() {

        if (isGPSEnabled || isNetworkEnabled) {
            Log.e("GPS Enable", "true");

            final List<String> m_lstProviders = locationManager.getProviders(false);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lng = Double.toString(location.getLongitude());
                    lat = Double.toString(location.getLatitude());
                    Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
                    locationManager.removeUpdates(locationListener);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged", "onStatusChanged");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled", "onProviderEnabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled", "onProviderDisabled");
                }
            };

            // QQQ: 시간, 거리를 0 으로 설정하면 가급적 자주 위치 정보가 갱신되지만 베터리 소모가 많을 수 있다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String name : m_lstProviders) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager.requestLocationUpdates(name, 3000, 2, locationListener);
                    }

                }
            });

        } else {
            Log.e("GPS Enable", "false");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                Toast.makeText(getApplicationContext(),"Gps Enable false", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private Runnable mStartRunnable = () -> {
        //더미데이터 넣기
        setDummy();

    };
    private Runnable mStartRunnable2 = () -> {
        //더미데이터 넣기
        setDummy2();

    };

    public void setClickListener(){
        chat_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chat_edit.getText().toString().equals("")) {
                    Chat temp = new Chat("윤성", chat_edit.getText().toString(), "127.0630488", "37.5092300");
                    chat_edit.setText("");
                    chat_list.add(temp);
                    recycler_chat.scrollToPosition(recycler_chat.getAdapter().getItemCount() - 1);
                    //통신 들어가야 함.

                    mAdapter.notifyDataSetChanged();
                    mHandler2.postDelayed(mStartRunnable2, 2000);
                }
            }
        });
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

    private void initRecyclerVIew() {
        chat_list = new ArrayList<>();
        mAdapter = new ChatAdapter(this, chat_list);
        recycler_chat.setHasFixedSize(true);
        recycler_chat.setLayoutManager(new LinearLayoutManager(this));
        recycler_chat.setAdapter(mAdapter);

    }

    public void setDummy(){
        chat_list.add(new Chat("진수","안녕하세욤!","127.0630205","37.5091300"));
        mAdapter.notifyDataSetChanged();

    }
    public void setDummy2(){
        chat_list.add(new Chat("진수","어떻게 도와드릴까요?","127.0630205","37.5091300"));
        mAdapter.notifyDataSetChanged();
    }


    public void setKeyboard(){
        InputMethodManager controlManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        SoftKeyboard mSoftKeyboard = new SoftKeyboard(chat_sliding, controlManager);
        mSoftKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                // 키보드 내려왔을때
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)(chat_sliding.getLayoutParams());
                                params.height=1000;
                                chat_sliding.setLayoutParams(params);
                            }
                        });
            }



            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                // 키보드 올라왔을때
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)(chat_sliding.getLayoutParams());
                                params.height=1500;
                                chat_sliding.setLayoutParams(params);
                            }
                        });
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
        //lng,lat순서
        poiData.addPOIitem(127.05389, 37.89065, "Pizza 777-111", markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        //맵 중심 위치 지정
        mMapController.setMapCenter(new NGeoPoint(127.05389, 37.89065), 11);

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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class Receiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String serviceData = intent.getStringExtra(Constants.BEC_ITEM);
            Log.d("main_at","serviceData : "+serviceData);
        }

    }
}
