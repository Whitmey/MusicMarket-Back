package app.service;

import app.model.Song;
import app.repository.SongRepository;
import app.schedule.SongParser;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Date currentDay = new Date();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDay);

        List<Song> songs = repository.getSongs(currentDate);

        for (Song song : songs) {
            BigDecimal change = song.getPrice().subtract(song.getStartPrice());
            song.setChange(change);
            if (song.getStartPrice().compareTo(BigDecimal.ZERO) > 0) {
                song.setChangeAsPercent(change.divide(song.getStartPrice(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            }
        }

        return songs;
    }

    public List<Song> getSongByName(Request request, Response response) throws IOException {
        String name = request.params("name");

        return repository.getSongByName(name);
    }

}
