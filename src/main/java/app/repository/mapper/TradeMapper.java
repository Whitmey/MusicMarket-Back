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
        String tradeId = r.getString("TRADE_LOG.trade_log_id");
        String userId = r.getString("TRADE_LOG.user_id");
        String shareLotId = r.getString("TRADE_LOG.share_lot_id");
        String trackName = r.getString("TRADE_LOG.track_name");
        String artist = r.getString("TRADE_LOG.artist");
        String type = r.getString("TRADE_LOG.type");
        BigDecimal price = r.getBigDecimal("TRADE_LOG.price");
        Integer quantity = r.getInt("TRADE_LOG.quantity");
        String dateTime = r.getString("TRADE_LOG.date_time");

        return new Trade(tradeId, userId, shareLotId, trackName, artist, type, price, quantity, dateTime);
    }

}
