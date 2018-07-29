package app.repository;

import app.model.Share;
import app.model.Song;
import app.model.Trade;
import app.model.User;
import app.repository.mapper.ShareMapper;
import app.repository.mapper.SongMapper;
import app.repository.mapper.TradeMapper;
import app.repository.mapper.UserMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

    private final UserMapper userMapper;

    public UserRepository() {
        this.userMapper = new UserMapper();
    }

    public User createUser(User user) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);

        h.execute("INSERT INTO `MUSIC_MARKET`.`USER` (`user_id`, `username`, `password`, `balance`) VALUES (?, ?, ?, ?)",
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getBalance());

        h.close();

        return user;
    }

    public String checkCredentials(User user) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        Optional<String> query = h.createQuery("SELECT user_id FROM `MUSIC_MARKET`.`USER` WHERE username=:username AND password=:password")
                .bind("username", user.getUsername())
                .bind("password", user.getPassword())
                .mapTo(String.class).findFirst();

        h.close();

        if (query.isPresent()) {
            return query.get();
        }

        return null;
    }

    public User findById(String userId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        User query = h.createQuery("SELECT user_id, username, balance FROM `MUSIC_MARKET`.`USER` WHERE user_id=:id")
                .bind("id", userId)
                .map(userMapper).findOnly();

        h.close();

        return query;
    }

    public User findUserByUsername(User user) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        User query = h.createQuery("SELECT * FROM `MUSIC_MARKET`.`USER` WHERE username=:username")
                .bind("username", user.getUsername())
                .map(userMapper).findOnly();

        h.close();

        return query;
    }

    public List<Share> findSharesByUserId(String userId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<Share> shares = h.createQuery("SELECT * FROM `MUSIC_MARKET`.`SHARE_LOT` WHERE user_id=:user_id")
                .bind("user_id", userId)
                .map(new ShareMapper())
                .list();

        h.close();

        return shares;
    }

    public Song getLatestSongByName(String name) { // and artist to be sure, use star more often to get everything?
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        Song query = h.createQuery("SELECT id, position, trackname, artist, streams, url, price, date " +
                "FROM `MUSIC_MARKET`.`SONG` " +
                "WHERE trackname=:trackname " +
                "ORDER BY date DESC " +
                "LIMIT 1")
                .bind("trackname", name)
                .map(new SongMapper()).findOnly();

        h.close();

        return query;
    }

    public Trade findFirstTradeLog(String shareLotId) { // and artist to be sure, use star more often to get everything?
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        Trade query = h.createQuery("SELECT * " +
                "FROM `MUSIC_MARKET`.`TRADE_LOG` " +
                "WHERE share_lot_id=:share_lot_id " +
                "ORDER BY date_time DESC " +
                "LIMIT 1")
                .bind("share_lot_id", shareLotId)
                .map(new TradeMapper()).findOnly();

        h.close();

        return query;
    }

    public List<User> getAllUsers() {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        List<User> query = h.createQuery("SELECT user_id, username, balance FROM `MUSIC_MARKET`.`USER`")
                .map(userMapper)
                .list();

        h.close();

        return query;
    }

}
