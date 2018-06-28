package app.model;

import java.math.BigDecimal;

public class Song {

    private String id;
    private Integer position;
    private String trackName;
    private String artist;
    private Integer streams;
    private String url;
    private BigDecimal price;
    private String date;

    public Song(String id, Integer position, String trackName, String artist, Integer streams, String url, BigDecimal price, String date) {
        this.id = id;
        this.position = position;
        this.trackName = trackName;
        this.artist = artist;
        this.streams = streams;
        this.url = url;
        this.price = price;
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Song() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getStreams() {
        return streams;
    }

    public void setStreams(Integer streams) {
        this.streams = streams;
    }

    @Override
    public String toString(){
        return "Position="+position+",Track Name="+trackName+",Artist="+artist+",Streams="+streams+",URL="+url+"\n";
    }

}
