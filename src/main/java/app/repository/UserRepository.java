package app.repository;

import app.model.User;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.UUID;

public class UserRepository {

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

}
