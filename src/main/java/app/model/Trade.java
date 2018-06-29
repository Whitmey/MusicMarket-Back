package app.model;

import java.math.BigDecimal;

public class Trade {

    private String tradeId;
    private String shareId;
    private String trackName;
    private String artist;
    private BigDecimal price;
    private Integer quantity;
    private Long dateTime;

    public Trade(String tradeId, String shareId, String trackName, String artist, BigDecimal price, Integer quantity, Long dateTime) {
        this.tradeId = tradeId;
        this.shareId = shareId;
        this.trackName = trackName;
        this.artist = artist;
        this.price = price;
        this.quantity = quantity;
        this.dateTime = dateTime;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String userId) {
        this.shareId = shareId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}