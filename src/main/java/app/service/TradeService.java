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

    public Trade buySong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class);
        Song song = getLatestSongDetails(trade.getTrackName(), trade.getArtist());

        BigDecimal newPrice = song.getPrice().add(calculatePriceChange(song.getPrice(), trade.getQuantity()));

        BigDecimal newBalance = getDecreasedBalance(userId, trade, newPrice);
        String newShareLotId = UUID.randomUUID().toString();

        if (isSongAvailable(song) == false) {
            response.status(400);
            System.out.println("Song is currently not available for purchase");
        }

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            repository.buyShares(userId, newShareLotId, trade, song, newPrice);
            repository.updateSongPrice(song, newPrice);
            repository.logTrade(userId, newShareLotId, trade.getQuantity(), song, "BUY", newPrice);
            repository.updateUserBalance(userId, newBalance);
        }
        else {
            response.status(400);
            System.out.println("Insufficient balance");
        }

        trade.setId("1");
        return trade;
    }

    public String sellSong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class);

        Share shareLotToSell = getShareLotById(trade.getShareLotId());

        Song song = getLatestSongDetails(shareLotToSell.getTrackName(), shareLotToSell.getArtist());

        BigDecimal newPrice = song.getPrice().subtract(calculatePriceChange(song.getPrice(), shareLotToSell.getQuantity()));

        if (isSongAvailable(song) == false) {
            return "Song cannot be sold as it is no longer listed, try selling again if it returns to the top 200";
        }

        if (shareLotToSell.getQuantity() > 0) {
            repository.updateShareLot(shareLotToSell.getId(), 0);
            repository.updateSongPrice(song, newPrice);
            BigDecimal newBalance = getIncreasedBalance(userId, shareLotToSell, newPrice);
            repository.logTrade(userId, trade.getShareLotId(), shareLotToSell.getQuantity(), song, "SELL", newPrice);
            repository.updateUserBalance(userId, newBalance);
        }
        else {
            return "You don't own any shares";
        }

        return "Success, shares sold";
    }

    public Song getLatestSongDetails(String trackName, String artist) { // what if the song doesn't exist
        return repository.getLatestSongByName(trackName);
    }

    public BigDecimal getUserBalance(String userId) { // what if the song doesn't exist
        return repository.findUserBalanceById(userId);
    }

    public BigDecimal calculatePriceChange(BigDecimal price, Integer quantity) {
        return price.divide(new BigDecimal(5000)).multiply(new BigDecimal(quantity));
    }

    public BigDecimal getDecreasedBalance(String userId, Trade trade, BigDecimal newPrice) {
        BigDecimal balance = getUserBalance(userId);
        BigDecimal purchaseAmount = newPrice.multiply(new BigDecimal(trade.getQuantity()));

        return balance.subtract(purchaseAmount);
    }

    public BigDecimal getIncreasedBalance(String userId, Share shareLotToSell, BigDecimal newPrice) {
        BigDecimal balance = getUserBalance(userId);
        BigDecimal sellAmount = newPrice.multiply(new BigDecimal(shareLotToSell.getQuantity()));

        return balance.add(sellAmount);
    }

    public Share getShareLotById(String shareLotId) {
        return repository.findShareLotById(shareLotId);
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
