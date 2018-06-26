package app.repository;

import app.model.User;
import app.repository.mapper.UserMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

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

        h.execute("INSERT INTO `MUSIC_MARKET`.`USER` (`id`, `username`, `password`) VALUES (?, ?, ?)",
                user.getId(),
                user.getUsername(),
                user.getPassword());

        h.close();

        return user;
    }

    public String checkCredentials(User user) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        String query = h.createQuery("SELECT id FROM `MUSIC_MARKET`.`USER` WHERE username=:username AND password=:password")
                .bind("username", user.getUsername())
                .bind("password", user.getPassword())
                .mapTo(String.class).findOnly();

        h.close();

        return query;
    }

    public User findById(String userId) {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://127.0.0.1:3306/MUSIC_MARKET?user=root&relaxAutoCommit=true");
        Handle h = jdbi.open();

        User query = h.createQuery("SELECT id, username, balance FROM `MUSIC_MARKET`.`USER` WHERE id=:id")
                .bind("id", userId)
                .map(userMapper).findOnly();

        h.close();

        return query;
    }

}
