package app.service;

import app.model.Share;
import app.model.Song;
import app.model.Trade;
import app.repository.TradeRepository;
import app.util.TokenAuthentication;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TradeService {

    private TradeRepository repository;
    private TokenAuthentication tokenAuthentication;
    private Gson gson;

    public TradeService() {
        repository = new TradeRepository();
        tokenAuthentication = new TokenAuthentication();
        gson = new Gson();
    }

    public String buySong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class);
        Song song = getLatestSongDetails(trade.getTrackName(), trade.getArtist());

        BigDecimal newBalance = getDecreasedBalance(userId, trade, song);
        Share currentOwnership = checkCurrentOwnership(userId, trade.getTrackName(), trade.getArtist());
        String shareId = currentOwnership.getShareId() != null ? currentOwnership.getShareId() : UUID.randomUUID().toString();

        if (isSongAvailable(song) == false) {
            return "Song is currently not available for purchase";
        }

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            if (currentOwnership.getQuantity() >= 0) {
                Integer newQuantity = currentOwnership.getQuantity() + trade.getQuantity();
                repository.updateShareOwnership(userId, song, newQuantity);
            }
            else {
                repository.buyShares(userId, shareId, trade, song);
            }
            repository.logTrade(userId, shareId, trade, song);
            repository.updateUserBalance(userId, newBalance);
        }
        else {
            return "Insufficient balance";
        }

        return "Success, shares purchased";
    }

    public String sellSong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class);
        Song song = getLatestSongDetails(trade.getTrackName(), trade.getArtist());

        if (isSongAvailable(song) == false) {
            return "Song cannot be sold as it is no longer listed";
        }

        Share currentOwnership = checkCurrentOwnership(userId, trade.getTrackName(), trade.getArtist());

        if (currentOwnership.getQuantity() >= trade.getQuantity()) {
            Integer newQuantity = currentOwnership.getQuantity() - trade.getQuantity();
            String shareId = currentOwnership.getShareId();
            repository.updateShareOwnership(userId, song, newQuantity);
            BigDecimal newBalance = getIncreasedBalance(userId, trade, song);
            repository.logTrade(userId, shareId, trade, song);
            repository.updateUserBalance(userId, newBalance);
        }
        else {
            return "You don't own enough shares";
        }

        return "Success, shares sold";
    }

    public Song getLatestSongDetails(String trackName, String artist) { // what if the song doesn't exist
        return repository.getLatestSongByName(trackName);
    }

    public BigDecimal getUserBalance(String userId) { // what if the song doesn't exist
        return repository.findUserBalanceById(userId);
    }

    public BigDecimal getDecreasedBalance(String userId, Trade trade, Song song) {
        BigDecimal balance = getUserBalance(userId);
        BigDecimal purchaseAmount = song.getPrice().multiply(new BigDecimal(trade.getQuantity()));

        return balance.subtract(purchaseAmount);
    }

    public BigDecimal getIncreasedBalance(String userId, Trade trade, Song song) {
        BigDecimal balance = getUserBalance(userId);
        BigDecimal purchaseAmount = song.getPrice().multiply(new BigDecimal(trade.getQuantity()));

        return balance.add(purchaseAmount);
    }

    public Share checkCurrentOwnership(String userId, String trackName, String artist) {
        List<Share> allShares = repository.findSharesByUserId(userId, trackName, artist);

        for (int i=0; i < allShares.size(); i++) {
            if (allShares.get(i).getTrackName().equals(trackName) && allShares.get(i).getArtist().equals(artist)) {
                return allShares.get(i);
            }
        }
        return new Share(null, "", "", "", -1);
    }

    public boolean isSongAvailable(Song song) {
        Date date = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        if (song.getDate().equals(currentDate)) {
            return true;
        }
        return false;
    }

}
