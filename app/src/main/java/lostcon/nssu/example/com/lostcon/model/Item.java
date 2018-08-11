package lostcon.nssu.example.com.lostcon.model;

public class Item {

    private String im_url;
    private String name;
    private String uuid;
    private String range;
    private String distance;
    private String alarm;

    public Item(String im_url, String name, String uuid, String distance,String alarm,String range) {
        this.im_url = im_url;
        this.name = name;
        this.uuid = uuid;
        this.range = range;
        this.distance = distance;
        this.alarm = alarm;
        this.range = range;
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

    public String getRange(){return range;}

    public void setIm_url(String url){ im_url = url;}

    public void setDistance(String d){ distance = d;}

    public void setName(String n){ name = n;}

    public void setUuid(String u){ uuid = u;}

    public void setAlarm(String a){ alarm = a;}

    public void setRange(String r){range = r;}
}
