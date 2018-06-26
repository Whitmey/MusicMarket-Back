package app.adapter;

import app.service.UserService;
import com.google.gson.Gson;

import static spark.Spark.*;

public class UserAdapter {

    private static UserService service;

    static {
        service = new UserService();
    }

    public static void configureRoutes(Gson gson) {

        post("login", (request, response) -> service.loginUser(request, response), gson::toJson);

        post("register", (request, response) -> service.registerUser(request, response), gson::toJson);

        get("account", (request, response) -> service.getUserAccount(request, response), gson::toJson);

        //new adapter
        get("songs", (request, response) -> service.fetchSongs(request, response), gson::toJson);

    }
}
