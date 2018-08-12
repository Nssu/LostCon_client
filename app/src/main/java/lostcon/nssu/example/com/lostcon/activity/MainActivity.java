package lostcon.nssu.example.com.lostcon.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeaconManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.realm.Realm;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.adapter.ItemAdapter;
import lostcon.nssu.example.com.lostcon.common.Constants;
import lostcon.nssu.example.com.lostcon.model.Item;
import lostcon.nssu.example.com.lostcon.model.Location;
import lostcon.nssu.example.com.lostcon.util.ApiUtil;
import lostcon.nssu.example.com.lostcon.util.BeaconService;
import lostcon.nssu.example.com.lostcon.util.PreferenceHelper;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView user_name;
    DrawerLayout layoutMain;
    ImageView menu_button;
    ImageView search_button;
    ImageView add_item;
    TextView text_count;

    RecyclerView.Adapter mAdapter;
    RecyclerView recycler_item;
    ArrayList<Item> item_list;
    private int menu_select = 0;
    LinearLayout[] menu;
    LinearLayout[] menu_check ;

    //찾기 요청 첫번째팝업
    public PopupWindow popupWindow_search;
    public View popupView_search;
    Button request_loc;
    Button request_chat;
    Button cancel_button;
    private MinewBeaconManager beaconManager;
    private Realm realm;
    private int port;
    private Socket socket;
    private BroadcastReceiver receiver;
    private int money;
    private String uuid;

    private Emitter.Listener onMessageHandler = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object = (JSONObject) args[0];
//            String data = (String) args[0];
            try {
                Log.d("main_at", "msg : " + object.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //1:1찾기 팝업(사례금)
    public PopupWindow popupWindow_chat;
    public View popupView_chat;
    public EditText edit_money;
    public Button submit_money;
            /*data = "{" + data + "}";
            Gson gson = new Gson();
            Message msg = gson.fromJson(data, Message.class);
            Log.d("main_at","msg : " + msg.getMsg());*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting();
        setClickListener();
        setToolbar();
        initRecyclerVIew();


    }
    public void setting(){
        text_count = (TextView)findViewById(R.id.text_count);
        Log.d("dd_","main onCreate()");
        Item item = new Item();
        item.setUuid(Constants.UUID);
        item.setUser_key(1);
        item.setItem_key(1);
        PreferenceHelper.saveItem(item);

        popupView_chat = getLayoutInflater().inflate(R.layout.popup_request_chat, null);
        popupWindow_chat = new PopupWindow(popupView_chat, RelativeLayout.LayoutParams.MATCH_PARENT
                ,RelativeLayout.LayoutParams.MATCH_PARENT,true);
        edit_money = (EditText)(popupView_chat.findViewById(R.id.edit_money));
        submit_money = (Button)(popupView_chat.findViewById(R.id.submit_money));
        popupView_search = getLayoutInflater().inflate(R.layout.popup_search, null);
        popupWindow_search = new PopupWindow(popupView_search, RelativeLayout.LayoutParams.MATCH_PARENT
                ,RelativeLayout.LayoutParams.MATCH_PARENT,true);

        request_loc = (Button)(popupView_search.findViewById(R.id.request_loc));
        request_chat = (Button)(popupView_search.findViewById(R.id.request_chat));
        cancel_button = (Button)(popupView_search.findViewById(R.id.cancel_button));


        user_name = (TextView)findViewById(R.id.user_name);



        search_button = (ImageView)findViewById(R.id.search_button);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        layoutMain = (DrawerLayout)findViewById(R.id.layout_main);
        recycler_item = (RecyclerView)findViewById(R.id.recycler_item) ;
        menu_button = (ImageView)findViewById(R.id.menu_button);
        add_item = (ImageView)findViewById(R.id.add_item);
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

        beaconManager = MinewBeaconManager.getInstance(this);
        if(getIntent() != null) {
            port = getIntent().getIntExtra(Constants.DATA_PORT, 0);
            money = getIntent().getIntExtra(Constants.DATA_MONEY, 0);
            uuid = getIntent().getStringExtra(Constants.DATA_UUID);
            if (port != 0) {
                Log.d("main_at", "fcm port : " + port);
                if (port != -1) {
                    try {
                        socket = IO.socket(Constants.SOC_URL);
                    } catch (URISyntaxException e) {
                        Log.d("main_at", "socket error" + e.getMessage());
                        e.printStackTrace();
                    }
                    socket.connect();
                    socket.on("toclient", onMessageHandler);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("msg", "sion");
                        socket.emit("fromclient", json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("main_at", "socket error" + e.getMessage());
                    }

                } else {
                    Location loc = new Location();
                    loc.setLatitude(12);
                    loc.setLongitude(23);
                    ApiUtil.postLocation(loc)
                            .subscribe(value -> Log.d("main_at", "성공")
                                    , err -> Log.e("main_at", "실패 : " + err.getMessage()));
                }
            }
        }
        request_loc.setOnClickListener(v ->
        {
            ApiUtil.requestLocation(PreferenceHelper.loadItem())
                    .subscribe(loc -> {
                                Log.d("main_at", loc.getLatitude() + " / " + loc.getLongitude());
                                Intent intent = new Intent(this,SearchActivity.class);
                                intent.putExtra(Constants.DATA_LAT,loc.getLatitude());
                                intent.putExtra(Constants.DATA_LON,loc.getLongitude());
                                startActivity(intent);
                            },
                            err ->Log.d("main_at", "error : " + err.getMessage() ));
        });

        request_chat.setOnClickListener(v ->
        {
            if(popupWindow_search.isShowing()){
                popupWindow_search.dismiss();
            }
            popupWindow_chat.showAtLocation(popupView_search, Gravity.CENTER, 0, 0);
            ApiUtil.requestTalk(item)
                    .subscribe(value ->{
                                Intent intent = new Intent(this,SearchActivity.class);
                                intent.putExtra(Constants.DATA_PORT,port);
                                startActivity(intent);
                            } ,
                            err -> Log.d("main_at","error : " + err.getMessage()));

        });

        submit_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        cancel_button.setOnClickListener(v ->
        {
            if(popupWindow_search.isShowing()){
                popupWindow_search.dismiss();
            }
        });
        checkBluetooth();
    }
    public void setClickListener(){
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });
        menu[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_select != 1) {
                    layoutMain.closeDrawer(Gravity.LEFT);
                }
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        menu[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_select != 2) {
                    layoutMain.closeDrawer(Gravity.LEFT);
                }
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(!popupWindow_search.isShowing()){
                    popupWindow_search.showAtLocation(popupView_search, Gravity.CENTER, 0, 0);
                }*/
            }
        });

    }
    public void soc()
    {
        Log.d("main_at","1:1 port : " + port);
        try {
            socket = IO.socket(Constants.SOC_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d("main_at","socket error" + e.getMessage());
        }
        socket.connect();
        socket.on("toclient", onMessageHandler);
        JSONObject json = new JSONObject();
        try {
            json.put("msg","sion");
            socket.emit("fromclient",json);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("main_at","socket error" + e.getMessage());
        }

    }
    public void setToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.line_menu);

        actionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layoutMain, toolbar, 0, 0);
        layoutMain.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void initRecyclerVIew() {
        item_list = new ArrayList<>();
        mAdapter = new ItemAdapter(this, item_list);
        recycler_item.setHasFixedSize(true);
        recycler_item.setLayoutManager(new LinearLayoutManager(this));
        recycler_item.setAdapter(mAdapter);

        //더미데이터 넣기
        setDummy();
        text_count.setText(""+item_list.size());

    }

    private void setDummy(){
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","범위를 벗어났습니다","1","30"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles7.naver.net%2F20150104_20%2Fgwanle_1420363188089lDBDa_JPEG%2FIMG_2289.JPG&type=b400",
                "노트북", "23108818","50M이내","0","70"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F277%2F2010%2F04%2F04%2F2010040412142902381_1.jpg&type=b400",
                "아이패드", "23108819","범위를 벗어났습니다","0","50"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles5.naver.net%2FMjAxNzA2MjFfMjUy%2FMDAxNDk4MDM0NDUwODIz.Q9YdUO37k6X8dJBcIa5ihQfR7hu1Eh-foyd4EkJ0NjEg.4qSWnJckcOZA_G_zuDotj18refTukvWmPe7WLnZG1m4g.JPEG.znfl2015%2F%25BD%25C3%25B0%25E8_%25BB%25E7%25C0%25CC%25C6%25AE_UPTIME_-_%25B3%25B2%25C0%25DA%25BD%25C3%25B0%25E8%25C3%25DF%25C3%25B5_%25BF%25C0%25B9%25D9%25C4%25ED6.jpg&type=b400",
                "시계", "23108820","30M이내","1","30"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fpost.phinf.naver.net%2FMjAxODAyMjBfMTAw%2FMDAxNTE5MTM0Njg2NDg5.MAIaGW5f-ZBDvGeSZzm-ARjsJtn587p8a9szvSKzZ-Mg.qZV5Yw_bfQR8zOOcEGA0F_rPvMruZUkOjP2l0PXUx7Yg.JPEG%2FIfj2hg99lZbtozod83RpLJh7oN7I.jpg&type=b400",
                "책", "23108822","70m이내","0","70"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles1.naver.net%2F20120811_136%2Fyumizz_1344628522452uChbQ_JPEG%2F%25C1%25DF%25B0%25ED%25BD%25BA%25B8%25B6%25C6%25AE%25C6%25F9%25B8%25C5%25C0%25D4.jpg&type=b400",
                "스마트폰", "23108821",
                "20M이내","1","30"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fshop1.phinf.naver.net%2F20170923_136%2Fnewton2000_1506166439240UgbFm_JPEG%2F37876719312849136_321216785.jpg&type=b400",
                "우산", "23108823","범위를 벗어났습니다","1","50"));
        item_list.add(new Item("https://search.pstatic.net/common/?src=http%3A%2F%2Fpost.phinf.naver.net%2FMjAxNzA0MDNfMTcz%2FMDAxNDkxMjAxMDc5MTQ5.ypZYixi1XiPfGpmfsGyr0tJb1FN20zw8AT2yS7PJ2o0g.agWEF4hYKA7oHM-pdQog5wdydjGCkMmWqh3HoLhwj7kg.JPEG%2FI9kL65pt6q7HBrQsrHDld3m6soQw.jpg&type=b400",
                "캐리어", "23108834","50M이내","1","50"));

        mAdapter.notifyDataSetChanged();

    }

    private void checkBluetooth() {
        Log.d("main_at","checkBluetooth()");
        BluetoothState bluetoothState = beaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                if(beaconManager != null)
                {
                    receiver = new Receiver();
                    Intent intent = new Intent(this, BeaconService.class);
                    IntentFilter filter = new IntentFilter("service");
                    registerReceiver(receiver, filter);
                    startService(intent);
                }
                break;
        }
    }
    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(popupWindow_search.isShowing()){
            popupWindow_search.dismiss();
        }else if(layoutMain.isDrawerOpen(Gravity.LEFT)){
            layoutMain.closeDrawer(Gravity.LEFT);
        }else{
            if(!layoutMain.isDrawerOpen(Gravity.LEFT)) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("main_at","onActivityResult()");
        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:
                checkBluetooth();
                break;
        }
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


