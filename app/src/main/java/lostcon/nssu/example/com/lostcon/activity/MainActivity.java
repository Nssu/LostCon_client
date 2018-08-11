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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.adapter.ItemAdapter;
import lostcon.nssu.example.com.lostcon.model.Item;

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
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
                finish();
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
                if(!popupWindow_search.isShowing()){
                    popupWindow_search.showAtLocation(popupView_search, Gravity.CENTER, 0, 0);
                }
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
                "지갑", "23108817","범위를 벗어났습니다","1"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","5M이내","0"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","5M이내","1"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","범위를 벗어났습니다","0"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","50M이내","1"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","70m이내","0"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","벗어났습니다","1"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","50M이내","1"));
        item_list.add(new Item("http://post.phinf.naver.net/20150430_258/seul_9_1430363945075fV5VL_JPEG/mug_obj_201504301219056204.jpg",
                "지갑", "23108817","50M이내","1"));

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(popupWindow_search.isShowing()){
            popupWindow_search.dismiss();
        }
    }

}
