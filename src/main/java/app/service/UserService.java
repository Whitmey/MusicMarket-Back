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

    public User registerUser(Request request, Gson gson) {
        User user = gson.fromJson(request.body(), User.class);
        return repository.createUser(user);
    }

    public Response loginUser(Request request, Response response) {
        User requestedUser = gson.fromJson(request.body(), User.class);
        User foundUser = repository.findByUsernameAndPassword(requestedUser);

        if (foundUser.getId() != null) {
            // generate token and set on header
            response.body(gson.toJson(foundUser));
            response.header("Authorisation", "SUCCESS");
        }
        else {
            response.status(401);
        }

        return response;
    }
}
