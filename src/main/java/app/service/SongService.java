package app.service;

import app.model.Song;
import app.repository.SongRepository;
import app.schedule.SongParser;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SongService {

    private SongRepository repository;
    private TokenAuthentication tokenAuthentication;
    private Gson gson;

    public SongService() {
        repository = new SongRepository();
        tokenAuthentication = new TokenAuthentication();
        gson = new Gson();
    }

    public String updateSongs(Request request, Response response) throws IOException {
        SongParser songParser = new SongParser();
        repository.updateSongs(songParser.fetchSongs());
        return "";
    }

    public List<Song> getSongs(Request request, Response response) throws IOException {
        Date date = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        return repository.getSongs(currentDate);
    }

    public List<Song> getSongByName(Request request, Response response) throws IOException {
        String name = request.params("name");

        return repository.getSongByName(name);
    }

}
