package app.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.bind.DatatypeConverter;

public class TokenAuthentication {

    private static byte[] secretBytes;

    public TokenAuthentication() {
        String key = "music_market_secret";
        String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
        secretBytes = DatatypeConverter.parseBase64Binary(base64Key);
    }

    public String generateToken() {
        return Jwts.builder()
                .setSubject("Joe")
                .signWith(SignatureAlgorithm.HS512, secretBytes)
                .compact();
    }

}
