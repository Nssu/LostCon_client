package lostcon.nssu.example.com.lostcon.network;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import lostcon.nssu.example.com.lostcon.model.Item;
import lostcon.nssu.example.com.lostcon.model.Location;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetroService {
    @GET("user")
    Observable<RetroClient> getUser();

/*    @GET("/2.2/users?order=desc&sort=reputation&site=stackoverflow")
    Flowable<UsersResponse> getMostPopularSOusers(@Query("pagesize") int howmany);*/

    @Multipart
    @POST("/items/info-upload")
    Flowable<Item> postItem(@Query("user_key") int user_key, @Query("item_name") String item_name,
                            @Part MultipartBody.Part file, @Query("item_uuid") String uuid);

    @POST("/lost/request/location")
    Flowable<Location> requestLocation(@Body Item item);

    @POST("/lost/request/location/gps")
    Flowable<String> postLocation(@Body Location location);

    @POST("/lost/request/talk")
    Flowable<Integer> requestTalk(@Body Item item);



}
