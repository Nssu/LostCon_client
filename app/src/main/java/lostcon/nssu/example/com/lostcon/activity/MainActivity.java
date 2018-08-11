package lostcon.nssu.example.com.lostcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.adapter.ItemAdapter;
import lostcon.nssu.example.com.lostcon.model.Item;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView user_name;
    DrawerLayout layoutMain;
    ImageView menu_button;
    ImageView search_button;
    ImageView add_item;

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

    //1:1찾기 팝업(사례금)
    public PopupWindow popupWindow_chat;
    public View popupView_chat;
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
        popupView_chat = getLayoutInflater().inflate(R.layout.popup_request_chat, null);
        popupWindow_chat = new PopupWindow(popupView_chat, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT,true);

        request_loc = (Button)(popupView_chat.findViewById(R.id.request_loc));
        request_chat = (Button)(popupView_chat.findViewById(R.id.request_chat));
        cancel_button = (Button)(popupView_chat.findViewById(R.id.cancel_button));

        user_name = (TextView)findViewById(R.id.user_name);
        popupView_search = getLayoutInflater().inflate(R.layout.popup_search, null);
        popupWindow_search = new PopupWindow(popupView_search, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT,true);

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

    }
    public void setClickListener(){
        /*
        request_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //위치정보만 요청 통신
                popupWindow_search.dismiss();
            }
        });
        request_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_search.dismiss();
                if(!popupWindow_search.isShowing())
                    popupWindow_search.showAtLocation(popupView_search, Gravity.CENTER, 0, 0);
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_search.dismiss();
            }
        });

        request_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_search.dismiss();
            }
        });
        */
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(popupWindow_search.isShowing()){
            popupWindow_search.dismiss();
        }else if(layoutMain.isDrawerOpen(Gravity.LEFT)){
            layoutMain.closeDrawer(Gravity.LEFT);
        }else{
            finish();
        }

    }

}
