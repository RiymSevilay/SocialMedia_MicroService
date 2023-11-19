package com.sevilay.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sevilay.exception.UserServiceException;
import com.sevilay.exception.ErrorType;
import com.sevilay.utility.enums.Role;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {
    //@Value("${jwt.secretkey}")
    String secretKey = "socialmediaauthsecretkey";
   // @Value("${jwt.issuer}")
    String issuer = "socialmediaauthissuer";
   // @Value("${jwt.audience}")
    String audience = "socialmediaauthaudience";

    Long expiration = System.currentTimeMillis() + 1000 * 60 * 5;

    public Optional<String> createToken2(Long id) {
        String token = null;
        Date date = new Date(expiration);
        try {
            token = JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .withClaim("id", id)
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<String> createToken(Long id, Role role) {
        String token = null;
        Date date = new Date(expiration);
        try {
            token = JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .withClaim("id", id)
                    .withClaim("role", role.toString())
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }


    /**
     * Aldığımız token ı doğrulama methodu
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UserServiceException(ErrorType.INVALID_TOKEN);
        }
    }

    public Optional<Long> getIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null) {
                throw new UserServiceException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id);
        } catch (Exception e) {
            e.getMessage();
            throw new UserServiceException(ErrorType.INVALID_TOKEN);
        }
    }





}
