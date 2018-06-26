package app.service;

import app.model.Song;
import app.model.User;
import app.repository.UserRepository;
import app.schedule.SongParser;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;

public class UserService {

    private UserRepository repository;
    private TokenAuthentication tokenAuthentication;
    private Gson gson;

    public UserService() {
        repository = new UserRepository();
        tokenAuthentication = new TokenAuthentication();
        gson = new Gson();
    }

    public User registerUser(Request request, Response response) {
        User user = gson.fromJson(request.body(), User.class);
        return repository.createUser(user);
    }

    public String loginUser(Request request, Response response) {
        String userId = repository.checkCredentials(gson.fromJson(request.body(), User.class));

        if (userId != null) {
            response.header("AuthKey", tokenAuthentication.generateToken(userId));
        }
        else {
            response.status(401);
        }

        return "";
    }

    public User getUserAccount(Request request, Response response) {
        String userId = tokenAuthentication.getUserId(request);
        return repository.findById(userId);
    }

    public List<Song> fetchSongs(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        SongParser songParser = new SongParser();
        return songParser.fetchSongs();
    }

}
