package app.schedule;

import app.model.Song;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SongParser {

    public List<Song> fetchSongs() throws IOException {

        DownloadCSV client = new DownloadCSV();
        client.download();

        BufferedReader reader = new BufferedReader(new FileReader("/Users/willwhitmey/MusicMarket-Back/src/main/java/app/schedule/Songs.csv"));
        reader.readLine(); // Read the first/current line to get rid of comment.

        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');

        //initialize the CSVParser object
        CSVParser parser = new CSVParser(reader, format);

        Date date = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        //Position,"Track Name",Artist,Streams,URL
        List<Song> songs = new ArrayList<>();
        for(CSVRecord record : parser){
            Song song = new Song();
            song.setPosition(Integer.parseInt(record.get("Position")));
            song.setTrackName(record.get("Track Name"));
            song.setArtist(record.get("Artist"));
            song.setStreams(Integer.parseInt(record.get("Streams")));
            song.setUrl(record.get("URL"));
            song.setPrice(calculatePrice(Integer.parseInt(record.get("Position")), Integer.parseInt(record.get("Streams"))));
            song.setDate(currentDate);
            songs.add(song);
        }
        //close the parser
        parser.close();

        return songs;
    }

    public BigDecimal calculatePrice(Integer position, Integer streams) {
        return new BigDecimal(10000).divide(new BigDecimal(position), 2, RoundingMode.HALF_UP);
    }

}
