package app.repository.mapper;

import app.model.Song;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongMapper implements RowMapper<Song> {

    public Song map(ResultSet r, StatementContext ctx) throws SQLException {
        String id = r.getString("SONG.id");
        Integer position = r.getInt("SONG.position");
        String trackName = r.getString("SONG.trackname");
        String artist = r.getString("SONG.artist");
        Integer streams = r.getInt("SONG.streams");
        String url = r.getString("SONG.url");
        BigDecimal price = r.getBigDecimal("SONG.price");
        String date = r.getString("SONG.date");

        return new Song(id, position, trackName, artist, streams, url, price, date);
    }

}
