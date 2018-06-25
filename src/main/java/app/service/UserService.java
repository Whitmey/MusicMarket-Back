package app.service;

import app.model.User;
import app.repository.UserRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class UserService {

    private UserRepository repository;
    private Gson gson;

    public UserService() {
        repository = new UserRepository();
        gson = new Gson();
    }

    public User registerUser(Request request, Response response) {
        User user = gson.fromJson(request.body(), User.class);
        return repository.createUser(user);
    }

    public String loginUser(Request request, Response response) {
        String authenticatedId = repository.checkCredentials(gson.fromJson(request.body(), User.class));

        if (authenticatedId != null) {
            // generate token and set on header
            response.header("Authorisation", "SUCCESS");
        }
        else {
            response.status(401);
        }

        return "";
    }
}
