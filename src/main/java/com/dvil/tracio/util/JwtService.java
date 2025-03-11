package com.dvil.tracio.util;


import com.dvil.tracio.enums.TokenType;
import com.dvil.tracio.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {
    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${JWT_ACCESS_TOKEN_EXPIRATION}")
    private long accessTokenExpiration;

    @Value("${JWT_REFRESH_TOKEN_EXPIRATION}")
    private long refreshTokenExpiration;

    @Value("${JWT_FORGOT_PASSWORD_TOKEN_EXPIRATION}")
    private long forgotTokenExpiration;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user, long expirationMillis, String TOKEN_TYPE) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .claim("Token_Type", TOKEN_TYPE)
                .signWith(getSigninKey())
                .compact();
    }

    public String generateEmailVerifyToken(User user) {
        return generateToken(user, accessTokenExpiration, TokenType.EmailVerificationToken.name());
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, TokenType.AccessToken.name());
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, TokenType.RefreshToken.name());
    }

    public String generateFogotPasswordToken(User user) {
        return generateToken(user, forgotTokenExpiration, TokenType.ForgotPasswordToken.name());
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}





