package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Song {

    private String id;
    private Integer position;
    private String trackName;
    private String artist;
    private Integer streams;
    private String url;
    private BigDecimal startPrice;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal changeAsPercent;
    private String date;

}
