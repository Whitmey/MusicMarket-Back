package app.repository;

import app.model.Song;
import app.repository.mapper.SongMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.List;
import java.util.UUID;

public class SongRepository {

    public SongRepository() {}

    public void updateSongs(List<Song> songs, String date) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        PreparedBatch batch = h.prepareBatch("INSERT INTO `MUSIC_MARKET`.`SONG`(id, position, trackname, artist, streams, url, date) VALUES(:id, :position, :trackname, :artist, :streams, :url, :date)");
        for (int i = 0; i < songs.size(); i++) {
            batch
                    .bind("id", UUID.randomUUID().toString()) //random uuid
                    .bind("position", songs.get(i).getPosition())
                    .bind("trackname", songs.get(i).getTrackName())
                    .bind("artist", songs.get(i).getArtist())
                    .bind("streams", songs.get(i).getStreams())
                    .bind("url", songs.get(i).getUrl())
                    .bind("date", date)
                    .add();
        }

        batch.execute();
    }

    public List<Song> getSongs(String date) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Song> songs = h.createQuery("SELECT id, position, trackname, artist, streams, url, date " +
                "FROM `MUSIC_MARKET`.`SONG` " +
                "WHERE date=:date " +
                "ORDER BY position ASC")
                .bind("date", date)
                .map(new SongMapper())
                .list();

        h.close();

        return songs;
    }

}
