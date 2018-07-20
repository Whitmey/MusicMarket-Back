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

        BigDecimal newBalance = getDecreasedBalance(userId, trade, song);
        String newShareLotId = UUID.randomUUID().toString();

        if (isSongAvailable(song) == false) {
            response.status(401);
            System.out.println("Song is currently not available for purchase");
        }

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            repository.buyShares(userId, newShareLotId, trade, song);
            repository.logTrade(userId, newShareLotId, trade, song, "BUY");
            repository.updateUserBalance(userId, newBalance);
        }
        else {
            response.status(401);
            System.out.println("Insufficient balance");
        }

        trade.setId("1");
        return trade;
    }

    public String sellSong(Request request, Response response) throws IOException {
        String userId = tokenAuthentication.getUserId(request);
        Trade trade = gson.fromJson(request.body(), Trade.class);

        // get all share lots
        //

        Share shareLotToSell = getShareLotById(trade.getShareLotId());
        Song song = getLatestSongDetails(trade.getTrackName(), trade.getArtist());

        if (isSongAvailable(song) == false) {
            return "Song cannot be sold as it is no longer listed, try selling again if it returns to the top 200";
        }

        if (shareLotToSell.getQuantity() >= trade.getQuantity()) {
            Integer newQuantity = shareLotToSell.getQuantity() - trade.getQuantity();
            repository.updateShareLot(shareLotToSell.getId(), newQuantity);
            BigDecimal newBalance = getIncreasedBalance(userId, trade, song);
            repository.logTrade(userId, trade.getShareLotId(), trade, song, "SELL");
            repository.updateUserBalance(userId, newBalance);
        }
        else {
            return "You don't own enough shares";
        }

        return "Success, shares sold";
    }

    public Share getCurrentOwnership(Request request, Response response) { // reuse for sell?
        String userId = tokenAuthentication.getUserId(request);
        List<Share> shareLots = repository.findShareLotsByNameAndUserId(request.params("trackName"), request.params("artist"), userId);

        if (shareLots.size() > 0) {
            Integer quantity = 0;

            for (int i=0; i< shareLots.size(); i++) {
                quantity = quantity + shareLots.get(i).getQuantity();
            }

            Share share = shareLots.get(0);
            share.setQuantity(quantity);

            return share;
        }

        return new Share("1", null, null, null, 0);
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
