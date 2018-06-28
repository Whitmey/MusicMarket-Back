package app.model;

public class Share {

    private String shareId;
    private String userId;
    private String trackName;
    private String artist;
    private Integer quantity;

    public Share(String shareId, String userId, String trackName, String artist, Integer quantity) {

        this.shareId = shareId;
        this.userId = userId;
        this.trackName = trackName;
        this.artist = artist;
        this.quantity = quantity;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
