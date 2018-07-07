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

    public String registerUser(Request request, Response response) {
        User user = gson.fromJson(request.body(), User.class);
//        if (checkUserNameIsAvailable(user) == false) {
//            return "Error, user already exists with that username";
//        }
        user.setBalance(new BigDecimal(10000));
        repository.createUser(user);
        return "User created";
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
        BigDecimal portfolioValue = BigDecimal.ZERO;
        BigDecimal totalProfitLoss = BigDecimal.ZERO;

        for(int i = 0; i < shares.size(); i++) {
            Song song = getLatestSongDetails(shares.get(i).getTrackName(), shares.get(i).getArtist());
            BigDecimal currentValue = new BigDecimal(shares.get(i).getQuantity()).multiply(song.getPrice());
//            quantity of shares at start
//            BigDecimal purchaseValue =
//            shares.get(i).setValue(currentValue);
            portfolioValue = portfolioValue.add(currentValue);
        }

        // profit is difference between value bought at and current value

        return new Portfolio(shares, portfolioValue, totalProfitLoss);
    }

    public Song getLatestSongDetails(String trackName, String artist) { // duplicated from tradeService, should be in song service
        return repository.getLatestSongByName(trackName);
    }

    public Boolean checkUserNameIsAvailable(User user) {
        Boolean userNameNotAllowed = false; // Implement userName filtering here
        // Need to get all users then filter by username about to be generated
        if (repository.findUserByUsername(user).getId() != null || userNameNotAllowed == true) {
            return false;
        }
        return true;
    }

}
