package lostcon.nssu.example.com.lostcon.util;


import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lostcon.nssu.example.com.lostcon.model.Item;
import lostcon.nssu.example.com.lostcon.model.Location;
import lostcon.nssu.example.com.lostcon.network.RetroClient;
import lostcon.nssu.example.com.lostcon.network.RetroService;
import okhttp3.MultipartBody;

public class ApiUtil {
    /*public static Flowable<List<User>> get(int how_many)
    {
        return service.getMostPopularSOusers(how_many)
                .map(usersResponse -> usersResponse.getUsers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }*/
    public static Flowable<Item> setItem(int user_key, String item_name,
                                         MultipartBody.Part file, String uuid)
    {
        RetroService service = RetroClient.getClient();
        return service.postItem(user_key, item_name, file, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<Location> requestLocation(Item item)
    {
        RetroService service = RetroClient.getClient();
        return service.requestLocation(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<String> postLocation(Location loc)
    {
        RetroService service = RetroClient.getClient();
        return service.postLocation(loc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<Integer> requestTalk(Item item)
    {
        RetroService service = RetroClient.getClient();
        return service.requestTalk(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
