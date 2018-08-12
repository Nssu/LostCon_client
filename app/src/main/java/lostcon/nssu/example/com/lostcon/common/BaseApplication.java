package lostcon.nssu.example.com.lostcon.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    public static SharedPreferences preferences;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("dd_","Base onCreate()");
        preferences = getSharedPreferences(Constants.PREF_KEY, MODE_PRIVATE);
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);


    }
}
