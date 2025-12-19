package Bank.BankingSystem.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private static final long EXPIRATION_TIME= 86400000;  //1day
    //200b085178740642cc5f88434dcfc28f2c14d4c1fd31c443151fb30e8113b68f
    @Value ("{jwtSecret}")
    private String jwtSecret;

    public SecretKey generateKey(){
        // Convert the secret string into bytes and build an HMAC-SHA key.
        // Keys.hmacShaKeyFor ensures the key length is valid for HS256.
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
     }

    /**
     * 2) Generate a JWT token for a given username.
     *    - subject = username
     *    - issuedAt = now
     *    - expiration = now + JWT_EXPIRATION_MS
     *    - signed with HS256 using our SecretKey
     */
     public String generateToken(String username){
         Date now = new Date();
         Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

         return Jwts.builder()
                 .subject(username)          // who is this token about?
                 .issuedAt(now)              // when was it created?
                 .expiration(expiry)         // when does it expire?
                 .signWith(generateKey(), Jwts.SIG.HS256) // sign with HS256
                 .compact();                 // build the final token string

     }
     public Claims extractClaims (String token){
         return Jwts.parser()
                 .verifyWith(generateKey())          // verify signature
                 .build()
                 .parseSignedClaims(token)           // parse token
                 .getPayload();                      // get the Claims body

     }
     public String extractSubject( String token){
         return extractClaims(token).getSubject();

     }
     public boolean isTokenValid(String token){
         try{
             return new Date().before(extractExpiration(token));
         }
         catch(ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException e){
             return false;
         }
     }
     public Date extractExpiration (String token){
         return extractClaims(token).getExpiration();

     }


}
