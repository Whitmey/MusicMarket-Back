package app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class Share {

    private String id;
    private String userId;
    private String trackName;
    private String artist;
    private Integer quantity;
    private BigDecimal value;
    private BigDecimal price;
    private BigDecimal currentPrice;
    private BigDecimal profitLoss;

    public Share(String shareId, String userId, String trackName, String artist, Integer quantity, BigDecimal price) {
        this.id = shareId;
        this.userId = userId;
        this.trackName = trackName;
        this.artist = artist;
        this.quantity = quantity;
        this.price = price;
    }

}
