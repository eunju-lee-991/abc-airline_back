package com.abcairline.abc.service.auth;

import com.abcairline.abc.domain.JWToken;
import com.abcairline.abc.domain.User;
import com.abcairline.abc.exception.ExpiredTokenException;
import com.abcairline.abc.exception.InvalidJwtTokenException;
import com.abcairline.abc.service.auth.constant.JwtConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
//    private final JWTVerifier verifier;

    private int accessTokenExpMinutes = 30;
    private int refreshTokenExpMinutes = 1000;
    private JwtTokenVerifier jwtTokenVerifier;

    public JWToken createToken(User user) {
        String accessToken = generateToken(user.getId(), user.getName(), accessTokenExpMinutes, JwtConstants.TOKEN_TYPE_ACCESS);
        String refreshToken = generateToken(user.getId(), user.getName(), refreshTokenExpMinutes, JwtConstants.TOKEN_TYPE_REFRESH);

        return JWToken.builder().accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpMinutes * 60) // 초 단위여서 * 60
                .refreshTokenExpiresIn(refreshTokenExpMinutes * 60) // 초 단위여서 * 60
                .build();
    }

    public Long validateToken(String token) {
        String accessToken = null;
        Date accessTokenExp = null;
        String socialAccessToken = null;
        Long id = null;

        try {
            jwtTokenVerifier = new JwtTokenVerifier(JwtConstants.SECRET_KEY);
            DecodedJWT decodedJWT = jwtTokenVerifier.verify(token);
            Date currentDate = new Date();
            if(currentDate.compareTo(decodedJWT.getExpiresAt()) > 0){
                throw new ExpiredTokenException();
            }

            if (decodedJWT.getClaim("name") != null) {
                id = Long.valueOf(decodedJWT.getSubject());
            } else {
                throw new InvalidJwtTokenException(token);
            }
        } catch (JWTVerificationException ex) {
            throw new InvalidJwtTokenException(token);
        }

        return id;
    }

    public String refreshToken(String refreshToken) {

        return null;
    }

    // 토큰 생성
    private String generateToken(Long id, String name, int expMinutes, String tokenType) {
        Date tokenExp = new Date(System.currentTimeMillis() + (60000 * expMinutes));

        String generatedToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("name", name)
                .withClaim("tokenType", tokenType)
                .withExpiresAt(tokenExp)
                .sign(Algorithm.HMAC512(JwtConstants.SECRET_KEY));

        return generatedToken;
    }
}
