package app.repository;

import app.model.User;
import app.repository.mapper.UserMapper;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import java.util.UUID;

public class UserRepository {

    private final UserMapper userMapper;

    public UserRepository() {
        this.userMapper = new UserMapper();
    }

    public User createUser(User user) {
        DBI dbi = new DBI("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = dbi.open();

        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);

        h.execute("INSERT INTO `MUSIC_MARKET`.`USER` (`id`, `username`, `password`) VALUES (?, ?, ?)",
                user.getId(),
                user.getUsername(),
                user.getPassword());

        h.close();

        return user;
    }

    public String checkCredentials(User user) {
        DBI dbi = new DBI("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = dbi.open();

        String query = h.createQuery("SELECT id FROM `MUSIC_MARKET`.`USER` WHERE username=:username AND password=:password")
                .bind("username", user.getUsername())
                .bind("password", user.getPassword())
                .mapTo(String.class).first();

        h.close();

        return query;
    }

//    public User findUserByUsernameAndPassword(User user) {
//        DBI dbi = new DBI("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
//        Handle h = dbi.open();
//
//        User query = h.createQuery("SELECT id, username FROM `MUSIC_MARKET`.`USER` WHERE username=:username AND password=:password")
//                .bind("username", user.getUsername())
//                .bind("password", user.getPassword())
//                .map(userMapper).first();
//
//        h.close();
//
//        return query;
//    }

}
