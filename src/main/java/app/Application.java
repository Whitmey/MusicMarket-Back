package app;

import app.adapter.SongAdapter;
import app.adapter.UserAdapter;
import com.google.gson.Gson;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Application {

    public static void main(String[] args) {

        port(8000);
        enableDebugScreen();

        Gson gson = new Gson();
        UserAdapter.configureRoutes(gson);
        SongAdapter.configureRoutes(gson);
    }
}
