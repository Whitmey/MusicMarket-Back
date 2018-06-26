package app.schedule;

import app.model.Song;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

        //Position,"Track Name",Artist,Streams,URL
        List<Song> songs = new ArrayList<>();
        for(CSVRecord record : parser){
            Song song = new Song();
            song.setPosition(Integer.parseInt(record.get("Position")));
            song.setTrackName(record.get("Track Name"));
            song.setArtist(record.get("Artist"));
            song.setStreams(Integer.parseInt(record.get("Streams")));
            song.setUrl(record.get("URL"));
            songs.add(song);
        }
        //close the parser
        parser.close();

        return songs;
    }
}
