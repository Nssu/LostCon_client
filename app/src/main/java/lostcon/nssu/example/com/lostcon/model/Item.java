package lostcon.nssu.example.com.lostcon.model;

public class Item {

    private String im_url;
    private String name;
    private String distance;

    public Item(String im_url, String name, String distance) {
        this.im_url = im_url;
        this.name = name;
        this.distance = distance;
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

}
