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

    public Song getLatestPriceForSong(String trackName, String artist) { // what if the song doesn't exist
        return repository.getLatestSongByName(trackName);
    }

    public String buySong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class); // track name, artist, quantity
        Song song = getLatestPriceForSong(trade.getTrackName(), trade.getArtist());

        BigDecimal newBalance = checkBalance(userId, trade, song);
        Share currentOwnership = checkCurrentOwnership(userId, trade.getTrackName(), trade.getArtist());
        String shareId;

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            if (currentOwnership.getQuantity() >= 0) {
//                update
                shareId = currentOwnership.getShareId();
                Integer newQuantity = currentOwnership.getQuantity() + trade.getQuantity();
                repository.buyAdditionalShares(userId, song, newQuantity);
            }
            else {
//                purchase new
                shareId = UUID.randomUUID().toString();
                repository.buyShares(userId, shareId, trade, song);
            }
            repository.logTrade(userId, shareId, trade, song);
            repository.updateUserBalance(userId, newBalance);
        }

        return "";
    }

    public String sellSong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class); // track name, artist, quantity
        Song song = getLatestPriceForSong(trade.getTrackName(), trade.getArtist());

        BigDecimal newBalance = checkBalance(userId, trade, song);
        Share currentOwnership = checkCurrentOwnership(userId, trade.getTrackName(), trade.getArtist());
        String shareId;

//        check they own sufficient shares

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            if (currentOwnership.getQuantity() >= 0) {
//                update
                shareId = currentOwnership.getShareId();
                Integer newQuantity = currentOwnership.getQuantity() + trade.getQuantity();
                repository.buyAdditionalShares(userId, song, newQuantity);
            }
            else {
//                purchase new
                shareId = UUID.randomUUID().toString();
                repository.buyShares(userId, shareId, trade, song);
            }
            repository.logTrade(userId, shareId,  trade, song);
            repository.updateUserBalance(userId, newBalance);
        }

        return "";
    }

    public BigDecimal getUserBalance(String userId) { // what if the song doesn't exist
        return repository.findUserBalanceById(userId);
    }

    public BigDecimal checkBalance(String userId, Trade trade, Song song) {
//        check balance, return new balance
        BigDecimal balance = getUserBalance(userId);
        BigDecimal purchaseAmount = song.getPrice().multiply(new BigDecimal(trade.getQuantity()));

        return balance.subtract(purchaseAmount);
    }

    public Share checkCurrentOwnership(String userId, String trackName, String artist) {
        // If doesn't own return -1! // this needs to be slicker.
        List<Share> allShares = repository.findSharesByUserId(userId, trackName, artist);

        for (int i=0; i < allShares.size(); i++) {
            if (allShares.get(i).getTrackName() == trackName && allShares.get(i).getArtist() == artist) {
                return allShares.get(i);
            }
        }
        return new Share("", "", "", "", -1);
    }

    public boolean checkSongListing(String userId, String trackName, String artist) {
        // check that song is currently available for purchase
        return true;
    }

}
