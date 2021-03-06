package app.adapter;

import app.service.SongService;
import app.util.Filters;
import com.google.gson.Gson;

import static spark.Spark.*;

public class SongAdapter {

    private static SongService service;

    static {
        service = new SongService();
    }

    public static void configureRoutes(Gson gson) {

        post("updateSongs", (request, response) -> service.updateSongs(request, response));

        get("songs", (request, response) -> service.getSongs(request, response), gson::toJson);

        get("song/:name", (request, response) -> service.getSongByName(request, response), gson::toJson);

    }
}
