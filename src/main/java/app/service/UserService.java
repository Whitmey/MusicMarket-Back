package app.service;

import app.model.Portfolio;
import app.model.Share;
import app.model.Song;
import app.model.User;
import app.repository.UserRepository;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
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
        user.setBalance(new BigDecimal(1000));
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

    public Portfolio getUserPortfolio(Request request, Response response) {
        String userId = tokenAuthentication.getUserId(request);
        List<Share> shares = repository.findSharesByUserId(userId);

        for(int i = 0; i < shares.size(); i++) {
            Song song = getLatestSongDetails(shares.get(i).getTrackName(), shares.get(i).getArtist());
            BigDecimal value = new BigDecimal(shares.get(i).getQuantity()).multiply(song.getPrice());
            shares.get(i).setValue(value);
        }

        return new Portfolio(shares);
    }

    public Song getLatestSongDetails(String trackName, String artist) { // duplicated from tradeService, should be in song service
        return repository.getLatestSongByName(trackName);
    }

}
