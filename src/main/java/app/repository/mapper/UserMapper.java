package app.repository.mapper;

import app.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {

    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        String id = r.getString("USER.id");
        String username = r.getString("USER.username");


        return new User(id, username);
    }

}
