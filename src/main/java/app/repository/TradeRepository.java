package app.repository;

import app.model.Share;
import app.model.Song;
import app.model.Trade;
import app.model.User;
import app.repository.mapper.ShareMapper;
import app.repository.mapper.SongMapper;
import app.repository.mapper.TradeMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TradeRepository {

    public TradeRepository() {}

    public Song getLatestSongByName(String name) { // and artist to be sure, use star more often to get everything?
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        Song query = h.createQuery("SELECT id, position, trackname, artist, streams, url, price, date " +
                "FROM `MUSIC_MARKET`.`SONG` " +
                "WHERE trackname=:trackname " +
                "ORDER BY date ASC " +
                "LIMIT 1")
                .bind("trackname", name)
                .map(new SongMapper()).findOnly();

        h.close();

        return query;
    }

    public BigDecimal findUserBalanceById(String userId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        BigDecimal query = h.createQuery("SELECT balance FROM `MUSIC_MARKET`.`USER` WHERE id=:id")
                .bind("id", userId)
                .mapTo(BigDecimal.class)
                .findOnly();

        h.close();

        return query;
    }

    public List<Trade> findUserTradesByTrackName(String userId, String trackName, String artist) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Trade> trades = h.createQuery("SELECT * FROM `MUSIC_MARKET`.`TRADE` WHERE id=:id AND track_name=:trackName AND artist=:artist")
                .bind("id", userId)
                .bind("trackName", trackName)
                .bind("artist", artist)
                .map(new TradeMapper())
                .list();

        h.close();

        return trades;
    }

    public void logTrade(String userId, String shareId, Trade trade, Song song) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String uniqueID = UUID.randomUUID().toString();

        h.execute("INSERT INTO `MUSIC_MARKET`.`TRADE` (`trade_id`, `share_id`, `track_name`, `artist`, `price`, `quantity`, `date_time`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                uniqueID,
                shareId,
                song.getTrackName(),
                song.getArtist(),
                song.getPrice(),
                trade.getQuantity(), // only this from Trade trade
                dateTime);

        h.close();
    }

    public void buyShares(String userId, String shareId, Trade trade, Song song) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        h.execute("INSERT INTO `MUSIC_MARKET`.`SHARE` " +
                        "(`share_id`, `user_id`, `track_name`, `artist`, `quantity`) " +
                        "VALUES (?, ?, ?, ?, ?)",
                shareId,
                userId,
                song.getTrackName(),
                song.getArtist(),
                trade.getQuantity());

        h.close();
    }

    public void buyAdditionalShares(String userId, Song song, Integer quantity) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        h.execute("UPDATE `MUSIC_MARKET`.`SHARE`\n" +
                        "SET quantity=?\n" +
                        "WHERE userId=? AND track_name=? AND artist=?;",
                quantity,
                userId,
                song.getTrackName(),
                song.getArtist());

        h.close();
    }

    public void updateUserBalance(String userId, BigDecimal newBalance) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        h.execute("UPDATE `MUSIC_MARKET`.`USER`\n" +
                "SET balance=?\n" +
                "WHERE id=?",
                newBalance,
                userId);

        h.close();
    }

    public List<Share> findSharesByUserId(String userId, String trackName, String artist) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Share> shares = h.createQuery("SELECT * FROM `MUSIC_MARKET`.`SHARE` WHERE user_id=:user_id AND track_name=:trackName AND artist=:artist")
                .bind("user_id", userId)
                .bind("trackName", trackName)
                .bind("artist", artist)
                .map(new ShareMapper())
                .list();

        h.close();

        return shares;
    }

}
