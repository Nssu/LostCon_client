package lostcon.nssu.example.com.lostcon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import lombok.Data;

@Data
public class Item extends RealmObject {

    private String im_url;
    private String name;
    private String distance;
    @Expose()
    private int user_key;
    @Expose()
    private int item_key;
    @SerializedName("item_uuid")
    @Expose()
    private String uuid;
    private int money;

    public Item()
    {}

    public Item(String im_url, String name, String distance) {
        this.im_url = im_url;
        this.name = name;
        this.distance = distance;
    }
}
