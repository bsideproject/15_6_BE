package bside.NotToDoClub.config;

import bside.NotToDoClub.domain_name.auth.dto.TokenDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Component
public class AuthTokenProvider {

    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    private final UserJpaRepository userRepository;

    public AuthTokenProvider(@Value("${app.auth.accessTokenSecret}") String accessSecretKey, UserJpaRepository userRepository) {
        this.key = hmacShaKeyFor(accessSecretKey.getBytes());
        this.userRepository = userRepository;
    }

    public TokenDto createAccessToken(String userEmail, UserRole roles) {
        //long accessTokenValidTime = 1000L * 60L * 10L; //10분
        long accessTokenValidTime = Duration.ofDays(30).toMillis(); //30일
        long refreshTokenValidTime = Duration.ofDays(60).toMillis(); //60일

        Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        //Refresh Token
        String refreshToken =  Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).key(userEmail).build();

    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public static Date getExpiryDate(String expiry) {
        return new Date(System.currentTimeMillis() + Long.parseLong(expiry));
    }

    public String getEmailByToken(AuthToken authToken) {

        if(authToken.validate()) { //토큰 유효성 체크

            Claims claims = authToken.getTokenClaims();
            String email = claims.getSubject();

            UserEntity userEntity = userRepository.findByLoginId(email).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );

            return userEntity.getLoginId();
        } else {
            throw new CustomException(ErrorCode.TOKEN_VALID_FAIL);
        }
    }

    public Long getUserIdByToken(AuthToken authToken) {

        if(authToken.validate()) { //토큰 유효성 체크

            Claims claims = authToken.getTokenClaims();
            String email = claims.getSubject();

            UserEntity userEntity = userRepository.findByLoginId(email).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );

            return userEntity.getId();
        } else {
            throw new CustomException(ErrorCode.TOKEN_VALID_FAIL);
        }
    }
}
