package app.service;

import app.model.User;
import app.repository.UserRepository;
import com.google.gson.Gson;
import spark.Request;

public class UserService {

    private UserRepository repository;

    public UserService() {
        repository = new UserRepository();
    }

    public User registerUser(Request request, Gson gson) {
        User user = gson.fromJson(request.body(), User.class);
        return repository.createUser(user);
    }
}
