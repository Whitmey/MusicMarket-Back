package app.adapter;

import app.service.TradeService;
import app.util.Filters;
import com.google.gson.Gson;

import static spark.Spark.before;
import static spark.Spark.post;

public class TradeAdapter {

    private static TradeService service;

    static {
        service = new TradeService();
    }

    public static void configureRoutes(Gson gson) {

        post("buy", (request, response) -> service.buySong(request, response), gson::toJson);

        post("sell", (request, response) -> service.sellSong(request, response), gson::toJson);

    }

}
