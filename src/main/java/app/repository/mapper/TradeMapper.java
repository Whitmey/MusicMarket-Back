package app.repository.mapper;

import app.model.Song;
import app.model.Trade;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradeMapper implements RowMapper<Trade> {

    public Trade map(ResultSet r, StatementContext ctx) throws SQLException {
        String tradeId = r.getString("TRADE.trade_id");
        String userId = r.getString("TRADE.user_id");
        String trackName = r.getString("TRADE.track_name");
        String artist = r.getString("TRADE.artist");
        BigDecimal price = r.getBigDecimal("TRADE.price");
        Integer quantity = r.getInt("TRADE.quantity");
        Long dateTime = r.getLong("TRADE.date_time");

        return new Trade(tradeId, userId, trackName, artist, price, quantity, dateTime);
    }

}
