package app.repository.mapper;

import app.model.Share;
import app.model.Trade;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShareMapper implements RowMapper<Share> {

    public Share map(ResultSet r, StatementContext ctx) throws SQLException {
        String shareId = r.getString("SHARE_LOT.share_lot_id");
        String userId = r.getString("SHARE_LOT.user_id");
        String trackName = r.getString("SHARE_LOT.track_name");
        String artist = r.getString("SHARE_LOT.artist");
        Integer quantity = r.getInt("SHARE_LOT.quantity");
        BigDecimal price = r.getBigDecimal("SHARE_LOT.price");

        return new Share(shareId, userId, trackName, artist, quantity, price);
    }
}
