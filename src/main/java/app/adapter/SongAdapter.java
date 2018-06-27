package app.adapter;

import app.service.SongService;
import com.google.gson.Gson;

import static spark.Spark.*;

public class SongAdapter {

    private static SongService service;

    static {
        service = new SongService();
    }

    public static void configureRoutes(Gson gson) {

        //new adapter
        post("updateSongs", (request, response) -> service.updateSongs(request, response));

        get("songs", (request, response) -> service.getSongs(request, response), gson::toJson);

    }
}
