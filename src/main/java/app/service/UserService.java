package app.service;

import app.model.*;
import app.repository.UserRepository;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
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

    public AuthKey loginUser(Request request, Response response) {
        String userId = repository.checkCredentials(gson.fromJson(request.body(), User.class));
        AuthKey data = new AuthKey();

        if (userId != null) {
            data.setAuthKey(tokenAuthentication.generateToken(userId));
        }
        else {
            response.status(401);
        }

        return data;
    }

    public User getUserAccount(Request request, Response response) {
        String userId = tokenAuthentication.getUserId(request);
        return repository.findById(userId);
    }

    public Portfolio getUserPortfolio(Request request, Response response) {
        String userId = tokenAuthentication.getUserId(request);
        User user = repository.findById(userId);
        List<Share> shares = repository.findSharesByUserId(userId);
        BigDecimal portfolioValue = BigDecimal.ZERO;

        for(int i = 0; i < shares.size(); i++) {
            Song song = getLatestSongDetails(shares.get(i).getTrackName(), shares.get(i).getArtist());
            BigDecimal currentValue = new BigDecimal(shares.get(i).getQuantity()).multiply(song.getPrice());
            portfolioValue = portfolioValue.add(currentValue);
            Trade firstBuyTrade = repository.findFirstTradeLog(shares.get(i).getShareId());
            shares.get(i).setProfitLoss(song.getPrice()
                    .multiply(new BigDecimal(shares.get(i).getQuantity()))
                    .subtract(firstBuyTrade.getPrice().multiply(new BigDecimal(shares.get(i).getQuantity()))));
            // Profit = Current price * current quantity MINUS Buy price * current quantity
        }

        BigDecimal totalProfitLoss = user.getBalance().add(portfolioValue).subtract(BigDecimal.valueOf(10000));

        return new Portfolio(userId, shares, portfolioValue, totalProfitLoss);
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
