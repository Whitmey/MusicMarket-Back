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

        get("hello", "application/json", (req, res) -> "hello");

    }
}