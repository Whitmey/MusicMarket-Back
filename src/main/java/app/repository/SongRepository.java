package app.repository;

import app.model.Song;
import app.model.User;
import app.repository.mapper.PriceLogMapper;
import app.repository.mapper.SongMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SongRepository {

    public SongRepository() {}

    public void updateSongs(List<Song> songs) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        PreparedBatch batch = h.prepareBatch("INSERT INTO `MUSIC_MARKET`.`SONG`(id, position, trackname, artist, streams, url, start_price, price, date) VALUES(:id, :position, :trackname, :artist, :streams, :url, :start_price, :price, :date)");
        for (int i = 0; i < songs.size(); i++) {
            batch
                    .bind("id", UUID.randomUUID().toString()) //random uuid
                    .bind("position", songs.get(i).getPosition())
                    .bind("trackname", songs.get(i).getTrackName())
                    .bind("artist", songs.get(i).getArtist())
                    .bind("streams", songs.get(i).getStreams())
                    .bind("url", songs.get(i).getUrl())
                    .bind("start_price", songs.get(i).getPrice())
                    .bind("price", songs.get(i).getPrice())
                    .bind("date", songs.get(i).getDate())
                    .add();
        }

        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        PreparedBatch priceBatch = h.prepareBatch("INSERT INTO `MUSIC_MARKET`.`PRICE_LOG` " +
                "(id, track_name, artist, price, date_time) " +
                "VALUES(:id, :track_name, :artist, :price, :date_time)");
        for (int i = 0; i < songs.size(); i++) {
            priceBatch
                    .bind("id", UUID.randomUUID().toString()) //random uuid
                    .bind("track_name", songs.get(i).getTrackName())
                    .bind("artist", songs.get(i).getArtist())
                    .bind("price", songs.get(i).getPrice())
                    .bind("date_time", dateTime)
                    .add();
        }

        batch.execute();
        priceBatch.execute();
    }

    public List<Song> getSongs(String date) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Song> songs = h.createQuery("SELECT id, position, trackname, artist, streams, url, start_price, price, date " +
                "FROM `MUSIC_MARKET`.`SONG` " +
                "WHERE date=:date " +
                "ORDER BY position ASC")
                .bind("date", date)
                .map(new SongMapper())
                .list();

        h.close();

        return songs;
    }

    public List<Song> getSongByName(String name) { // and artist to be sure
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Song> songs = h.createQuery("SELECT id, position, trackname, artist, streams, url, price, date " +
                "FROM `MUSIC_MARKET`.`SONG` " +
                "WHERE trackname=:trackname")
                .bind("trackname", name)
                .map(new SongMapper())
                .list();

        h.close();

        return songs;
    }

}
