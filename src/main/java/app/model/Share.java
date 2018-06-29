package app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class Share {

    private String shareId;
    private String userId;
    private String trackName;
    private String artist;
    private Integer quantity;
    private BigDecimal value;

    public Share(String shareId, String userId, String trackName, String artist, Integer quantity) {
        this.shareId = shareId;
        this.userId = userId;
        this.trackName = trackName;
        this.artist = artist;
        this.quantity = quantity;
    }

    public Share(String shareId, String userId, String trackName, String artist, Integer quantity, BigDecimal value) {
        this.shareId = shareId;
        this.userId = userId;
        this.trackName = trackName;
        this.artist = artist;
        this.quantity = quantity;
    }

}
