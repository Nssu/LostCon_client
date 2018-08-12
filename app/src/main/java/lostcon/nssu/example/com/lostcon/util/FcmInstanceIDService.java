package lostcon.nssu.example.com.lostcon.util;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        sendResgistrationToServer(token);
        Log.d("fcm",token);
    }
    private void sendResgistrationToServer(String token)
    {

    }

}