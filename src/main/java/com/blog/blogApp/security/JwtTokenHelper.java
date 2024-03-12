//package com.blog.blogApp.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.security.PublicKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtTokenHelper {
//
//    public static final long  JWT_TOKEN_VALIDITY = 5*60*60;
//
//    private String secret = "jwtTokenKey";
//    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//
//    //retrieve username from jwt token
//    public  String getUsernameFromToken(String token){
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    //retrieve expiration date from jwt token
//    public Date getExpirationDateFromToken(String token){
//        return getClaimFromToken(token,Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
//        final Claims claims = getAllClaimsFromToken(token);
//        return  claimsResolver.apply(claims);
//    }
//
//    //for reteriving any info from token we need a secret key
//    private Claims getAllClaimsFromToken(String token) {
//
//        SecretKey secret1 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
//        return Jwts.parser()
//                .verifyWith(secret1)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    private Boolean isTokenExpired(String token){
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    //Generate token for user
//    public String generateToken(UserDetails userDetails){
//        Map<String,Object> claims = new HashMap<>();
//        return doGenerateToken(claims,userDetails.getUsername());
//    }
//
//    //While creating the token
//    //1/Define claims of the token ,like issuer,Expiration,subject and id
//    //2.Sign the JWT using the HSS12 algo and secret key
//    //3.According to JWS Compact Serialization
//    private String doGenerateToken(Map<String,Object> claims,String subject){
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*100))
//                .signWith(SignatureAlgorithm.HS512,secret).compact();
//    }
//
//    //validate token
//    public Boolean validateToken(String token,UserDetails userDetails){
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//}




package com.blog.blogApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Generate a secure random secret key for HS512 algorithm
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Retrieve username from JWT token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // For retrieving any info from token we need a secret key
    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();

        return Jwts.parser()
                .verifyWith(secretKey)
               .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // While creating the token
    // 1/ Define claims of the token, like issuer, expiration, subject, and id
    // 2/ Sign the JWT using the HS512 algorithm and secret key
    // 3/ According to JWS Compact Serialization
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
