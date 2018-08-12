package app.repository.mapper;

import app.model.Song;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceLogMapper implements RowMapper<Song> {

    public Song map(ResultSet r, StatementContext ctx) throws SQLException {
        String id = r.getString("PRICE_LOG.id");
        String trackName = r.getString("PRICE_LOG.track_name");
        String artist = r.getString("PRICE_LOG.artist");
        BigDecimal price = r.getBigDecimal("PRICE_LOG.price");
        String date = r.getString("PRICE_LOG.date_time");

        return new Song(id, null, trackName, artist, null, null, null, price, null, null, date);
    }

}
