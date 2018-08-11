package lostcon.nssu.example.com.lostcon.model;

public class Item {

    private String im_url;
    private String name;
    private String uuid;
    private String distance;
    private String alarm;

    public Item(String im_url, String name, String uuid, String distance,String alarm) {
        this.im_url = im_url;
        this.name = name;
        this.uuid = uuid;
        this.distance = distance;
        this.alarm = alarm;
    }

    public String getIm_url() {
        return im_url;
    }

    public String getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setIm_url(String url){ im_url = url;}

    public void setDistance(String d){ distance = d;}

    public void setName(String n){ name = n;}

    public void setUuid(String u){ uuid = u;}

    public void setAlarm(String a){ alarm = a;}

}
