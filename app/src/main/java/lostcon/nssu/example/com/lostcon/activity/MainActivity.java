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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout layoutMain;
    ImageView menu_button;
    RecyclerView.Adapter mAdapter;
    RecyclerView recycler_item;
    ArrayList<Item> item_list;
    ImageView add_item;
    private int menu_select = 0;
    LinearLayout[] menu;
    LinearLayout[] menu_check ;
    ImageView search_button;
    public PopupWindow popupWindow_search;
    public View popupView_search;
    private MinewBeaconManager beaconManager;
    private Realm realm;
    private int port;
    private Socket socket;
    private BroadcastReceiver receiver;
    private Emitter.Listener onMessageHandler = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object = (JSONObject) args[0];
//            String data = (String) args[0];
            try {
                Log.d("main_at","msg : " + object.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*data = "{" + data + "}";
            Gson gson = new Gson();
            Message msg = gson.fromJson(data, Message.class);
            Log.d("main_at","msg : " + msg.getMsg());*/

        }
    };
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

        popupView_search = getLayoutInflater().inflate(R.layout.popup_search, null);
        popupWindow_search = new PopupWindow(popupView_search, RelativeLayout.LayoutParams.MATCH_PARENT
                ,RelativeLayout.LayoutParams.MATCH_PARENT,true);

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
        port = getIntent().getIntExtra(Constants.DATA_PORT,0);
        if(port != 0 )
        {
            Log.d("main_at","fcm port : " +port );
            if(port != -1)
            {
                try {
                    socket = IO.socket("http://192.168.0.200:3001");
                } catch (URISyntaxException e) {
                    Log.d("main_at","socket error" + e.getMessage());
                    e.printStackTrace();
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
            else
            {
                Location loc = new Location();
                loc.setLatitude(12);
                loc.setLongitude(23);
                ApiUtil.postLocation(loc)
                        .subscribe(value -> Log.d("main_at","성공")
                        , err -> Log.e("main_at","실패 : "+ err.getMessage()) );
            }

        }
        checkBluetooth();
    }
    public void setClickListener(){
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
                startActivity(intent);
            }
        });
        menu[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_select != 1) {
                    menu[1].setBackgroundColor(getResources().getColor(R.color.blue_trans));
                    menu[menu_select].setBackgroundColor(getResources().getColor(R.color.White));
                    menu_check[1].setBackgroundColor(getResources().getColor(R.color.check_blue));
                    menu_check[menu_select].setBackgroundColor(getResources().getColor(R.color.White));
                    menu_select = 1;
                }
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        menu[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_select != 2) {
                    menu[2].setBackgroundColor(getResources().getColor(R.color.blue_trans));
                    menu[menu_select].setBackgroundColor(getResources().getColor(R.color.White));
                    menu_check[2].setBackgroundColor(getResources().getColor(R.color.check_blue));
                    menu_check[menu_select].setBackgroundColor(getResources().getColor(R.color.White));
                    menu_select = 2;
                }
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setItem_key(1);
                item.setUser_key(1);
                item.setUuid("uu");
                /*ApiUtil.requestLocation(item)
                        .subscribe(loc -> Log.d("main_at", loc.getLatitude() + " / " + loc.getLongitude()),
                                err ->Log.d("main_at", "error : " + err.getMessage() ));*/

                ApiUtil.requestTalk(item)
                        .subscribe(value ->{
                                    port = value;
                                    soc();
                                } ,
                                err -> Log.d("main_at","error : " + err.getMessage()));
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
            socket = IO.socket("http://192.168.0.200:3001");
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

    }

    private void setDummy(){
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "500M이내"));
        item_list.add(new Item("http://post.phinf.naver.net/MjAxNzA3MDJfMjA0/MDAxNDk4OTkxMDIzMjgw.KTPwdiVyAK7VDbgwjdu6TMe4JVhof5tSrawNDzPeUlog.ov-nN0ZYty7CR1gpHyFfNXCWH-l69wxbA1-tYkgOn5cg.JPEG/IWBKiEAFMyMxG_9BKjaOc3XcPvMg.jpg",
                "지갑", "500M이내"));
        item_list.add(new Item("http://post.phinf.naver.net/MjAxNzA3MDJfMjA0/MDAxNDk4OTkxMDIzMjgw.KTPwdiVyAK7VDbgwjdu6TMe4JVhof5tSrawNDzPeUlog.ov-nN0ZYty7CR1gpHyFfNXCWH-l69wxbA1-tYkgOn5cg.JPEG/IWBKiEAFMyMxG_9BKjaOc3XcPvMg.jpg",
                "지갑", "500M이내"));
        item_list.add(new Item("http://post.phinf.naver.net/MjAxNzA3MDJfMjA0/MDAxNDk4OTkxMDIzMjgw.KTPwdiVyAK7VDbgwjdu6TMe4JVhof5tSrawNDzPeUlog.ov-nN0ZYty7CR1gpHyFfNXCWH-l69wxbA1-tYkgOn5cg.JPEG/IWBKiEAFMyMxG_9BKjaOc3XcPvMg.jpg",
                "지갑", "500M이내"));
        item_list.add(new Item("http://post.phinf.naver.net/MjAxNzA3MDJfMjA0/MDAxNDk4OTkxMDIzMjgw.KTPwdiVyAK7VDbgwjdu6TMe4JVhof5tSrawNDzPeUlog.ov-nN0ZYty7CR1gpHyFfNXCWH-l69wxbA1-tYkgOn5cg.JPEG/IWBKiEAFMyMxG_9BKjaOc3XcPvMg.jpg",
                "지갑", "500M이내"));
        item_list.add(new Item("http://post.phinf.naver.net/MjAxNzA3MDJfMjA0/MDAxNDk4OTkxMDIzMjgw.KTPwdiVyAK7VDbgwjdu6TMe4JVhof5tSrawNDzPeUlog.ov-nN0ZYty7CR1gpHyFfNXCWH-l69wxbA1-tYkgOn5cg.JPEG/IWBKiEAFMyMxG_9BKjaOc3XcPvMg.jpg",
                "지갑", "500M이내"));
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
        }
        unregisterReceiver(receiver);
        finish();
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
