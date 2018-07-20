package app.repository;

import app.model.Share;
import app.model.Song;
import app.model.Trade;
import app.repository.mapper.ShareMapper;
import app.repository.mapper.SongMapper;
import app.repository.mapper.TradeMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TradeRepository {

    public TradeRepository() {}

    public Song getLatestSongByName(String name) { // and artist to be sure, use star more often to get everything?
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        Optional<Song> query = h.createQuery("SELECT id, position, trackname, artist, streams, url, price, date " +
                "FROM `MUSIC_MARKET`.`SONG` " +
                "WHERE trackname=:trackname " +
                "ORDER BY date DESC " +
                "LIMIT 1")
                .bind("trackname", name)
                .map(new SongMapper()).findFirst();

        h.close();

        if (query.isPresent()) {
            return query.get();
        }

        return new Song();
    }

    public BigDecimal findUserBalanceById(String userId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        BigDecimal query = h.createQuery("SELECT balance FROM `MUSIC_MARKET`.`USER` WHERE user_id=:id")
                .bind("id", userId)
                .mapTo(BigDecimal.class)
                .findOnly();

        h.close();

        return query;
    }

    public void logTrade(String userId, String shareId, Trade trade, Song song, String type) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String uniqueID = UUID.randomUUID().toString();

        h.execute("INSERT INTO `MUSIC_MARKET`.`TRADE_LOG` (`trade_log_id`, `user_id`, `share_lot_id`, `track_name`, `artist`, `price`, `quantity`, `type`, `date_time`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                uniqueID,
                userId,
                shareId,
                song.getTrackName(),
                song.getArtist(),
                song.getPrice(),
                trade.getQuantity(),
                type,
                dateTime);

        h.close();
    }

    public void buyShares(String userId, String shareLotId, Trade trade, Song song) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        h.execute("INSERT INTO `MUSIC_MARKET`.`SHARE_LOT` " +
                        "(`share_lot_id`, `user_id`, `track_name`, `artist`, `quantity`) " +
                        "VALUES (?, ?, ?, ?, ?)",
                shareLotId,
                userId,
                song.getTrackName(),
                song.getArtist(),
                trade.getQuantity());

        h.close();
    }

    public void updateShareLot(String shareLotId, Integer quantity) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        h.execute("UPDATE `MUSIC_MARKET`.`SHARE_LOT`\n" +
                        "SET quantity=?\n" +
                        "WHERE share_lot_id=?",
                quantity,
                shareLotId);

        h.close();
    }

    public void updateUserBalance(String userId, BigDecimal newBalance) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        h.execute("UPDATE `MUSIC_MARKET`.`USER`\n" +
                "SET balance=?\n" +
                "WHERE user_id=?",
                newBalance,
                userId);

        h.close();
    }

    public Share findShareLotById(String shareLotId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        Share share = h.createQuery("SELECT * FROM `MUSIC_MARKET`.`SHARE_LOT` WHERE share_lot_id=:share_lot_id")
                .bind("share_lot_id", shareLotId)
                .map(new ShareMapper()).findOnly();

        h.close();

        return share;
    }

    public List<Share> findShareLotsByNameAndUserId(String trackName, String artist, String userId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Share> shares = h.createQuery("SELECT * FROM `MUSIC_MARKET`.`SHARE_LOT` " +
                "WHERE user_id=:user_id " +
                "AND track_name=:trackName " +
                "AND artist=:artist")
                .bind("user_id", userId)
                .bind("trackName", trackName)
                .bind("artist", artist)
                .map(new ShareMapper())
                .list();

        h.close();

        return shares;
    }

}
