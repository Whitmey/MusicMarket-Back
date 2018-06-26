package app.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import spark.Request;

import javax.xml.bind.DatatypeConverter;

public class TokenAuthentication {

    private static byte[] secretKey;

    public TokenAuthentication() {
        String key = "music_market_secret";
        String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
        secretKey = DatatypeConverter.parseBase64Binary(base64Key);
    }

    private byte[] getSecretKey() {
        return secretKey;
    }

    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUserId(Request request) {
        return Jwts.parser().setSigningKey(getSecretKey())
                .parseClaimsJws(request.headers("AuthKey")).getBody().getSubject();
    }

}
