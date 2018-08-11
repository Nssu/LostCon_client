package lostcon.nssu.example.com.lostcon.model;

public class Chat {

    //private String img;
    private String name;
    private String msg;
    private String lat;
    private String lng;

    public Chat(String name, String msg, String lat,String lng,String flag) {
        //this.img = img;
        this.name = name;
        this.msg = msg;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

  //  public String getImg(){ return img; }

}
