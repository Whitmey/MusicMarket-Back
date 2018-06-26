package app.service;

import app.model.User;
import app.repository.UserRepository;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

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
            response.header("Authorisation", tokenAuthentication.generateToken());
        }
        else {
            response.status(401);
        }

        return "";
    }
}
