package app.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class Portfolio {

    private List<Share> shares;
//    private BigDecimal totalValue;
//    private BigDecimal totalProfitOrLoss;


    public Portfolio(List<Share> shares) {
        this.shares = shares;
    }

}
