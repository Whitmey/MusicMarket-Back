package app.repository.mapper;

import app.model.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    public User map(ResultSet r, StatementContext ctx) throws SQLException {
        String id = r.getString("USER.id");
        String username = r.getString("USER.username");
        Double balance = r.getDouble("USER.balance");


        return new User(id, username, balance);
    }

}
