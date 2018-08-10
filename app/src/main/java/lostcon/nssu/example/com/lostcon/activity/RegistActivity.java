package lostcon.nssu.example.com.lostcon.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lostcon.nssu.example.com.lostcon.R;

public class RegistActivity extends AppCompatActivity {

    ImageView regist_image;

    // 권한 체크 관련
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS  = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"};

    // 이미지 파일 업로드 관련
    final static int PICK_FROM_ALBUM = 1;
    Bitmap bitmap;
    String base64_image;

    View last_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        setting();

    }

    public void setting(){
        regist_image = (ImageView)findViewById(R.id.regist_image);
    }


    public void onclick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.cancel_button:
                onBackPressed();
                break;
            case R.id.regist_image:
                    i = new Intent();
                    i.setAction(Intent.ACTION_PICK);
                    i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //퍼미션 상태 확인
                        if (!hasPermissions(getApplicationContext(),PERMISSIONS)) {
                            last_clicked = v;
                            //퍼미션 허가 안되어있다면 사용자에게 요청
                            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                        } else{
                            // 권한이 있는 상태
                            startActivityForResult(Intent.createChooser(i,"사진 가져오기"),PICK_FROM_ALBUM);
                        }
                    } else{
                        startActivityForResult(Intent.createChooser(i,"사진 가져오기"),PICK_FROM_ALBUM);
                    }
                    break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case PERMISSIONS_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    onclick(last_clicked);
                } else{
                    Toast.makeText(getApplicationContext(), "권한이 없어 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode){
            case PICK_FROM_ALBUM:
                if(data == null || data.getData() == null)
                    break;

                Uri path = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    base64_image = bitmap_to_string(bitmap);
                    // 비트맵을 얻어온 이후 서버에 업로드 시도하고 성공할 경우에 프로필 이미지로 설정함
                    Glide.with(getApplicationContext()).load(path).into(regist_image);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "이미지 불러오기에 실패했습니다", Toast.LENGTH_SHORT).show();
                    Log.d("upload failed", e.toString());
                }
                break;

            // 화면을 새로고침
            case 100:
                break;

            case 200:
                break;
        }
    }

    String bitmap_to_string(Bitmap bmp){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 10, bos);
        byte[] image_bytes = bos.toByteArray();
        return Base64.encodeToString(image_bytes, Base64.DEFAULT);
    }

    boolean hasPermissions(Context context, String[] permissions) {
        int result;

        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){

            result = ContextCompat.checkSelfPermission(context, perms);

            if (result == PackageManager.PERMISSION_DENIED){
                //허가 안된 퍼미션 발견
                return false;
            }
        }
        //모든 퍼미션이 허가되었음
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
