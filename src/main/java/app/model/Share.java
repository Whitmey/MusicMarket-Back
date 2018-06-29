package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Share {

    private String shareId;
    private String userId;
    private String trackName;
    private String artist;
    private Integer quantity;

}
