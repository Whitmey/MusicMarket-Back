package app.util;

import spark.*;

public class Filters {

    // Enable GZIP for all responses
    public static Filter addGzipHeader = (Request request, Response response) -> {
        response.header("Content-Encoding", "gzip");
    };

    public static Filter addCORSHeader = (req, res) -> {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS");
        res.header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, AuthKey");
        if (req.requestMethod().equals("OPTIONS")) {
            Spark.halt(200);
        }
    };

}
