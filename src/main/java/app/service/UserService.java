package app.service;

import app.model.User;
import app.repository.UserRepository;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

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
        // get all owned songs
        return repository.findById(userId);
    }

}
