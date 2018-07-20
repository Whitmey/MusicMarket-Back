package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Trade {

    private String id;
    private String userId;
    private String shareLotId;
    private String trackName;
    private String artist;
    private String type;
    private BigDecimal price;
    private Integer quantity;
    private String dateTime;

}