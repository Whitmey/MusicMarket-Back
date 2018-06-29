package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Trade {

    private String tradeId;
    private String shareId;
    private String trackName;
    private String artist;
    private BigDecimal price;
    private Integer quantity;
    private Long dateTime;

}