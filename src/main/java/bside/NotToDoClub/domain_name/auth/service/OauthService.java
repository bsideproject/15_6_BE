package bside.NotToDoClub.domain_name.auth.service;

import bside.NotToDoClub.config.Constant;
import bside.NotToDoClub.domain_name.auth.config.oauth.GoogleOauth;
import bside.NotToDoClub.domain_name.auth.config.oauth.KakaoOauth;
import bside.NotToDoClub.domain_name.auth.dto.GoogleOAuthTokenDto;
import bside.NotToDoClub.domain_name.auth.dto.KakaoOAuthTokenDto;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;
    private final HttpServletResponse response;

    public String getRedirectUrl(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType){
            case GOOGLE:{
                redirectURL= googleOauth.getOauthRedirectURL();
            }
            case KAKAO:{
                redirectURL = kakaoOauth.getOauthRedirectURL();
            }
            break;
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }

        return redirectURL;
    }

    public GoogleUserInfoDto getGoogleUserInfo(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
        GoogleOAuthTokenDto oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
        GoogleUserInfoDto googleUser = googleOauth.getUserInfo(userInfoResponse);

        String accessToken = oAuthToken.getAccess_token();
        String refreshToken = oAuthToken.getRefresh_token();

        googleUser.setAccess_token(accessToken);
        googleUser.setRefresh_token(refreshToken);

        return googleUser;
    }

    public KakaoUserInfoDto getKakaoUserInfo(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);
        KakaoOAuthTokenDto oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);
        KakaoUserInfoDto kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);

        System.out.println("userInfoResponse = " + userInfoResponse);

        String accessToken = oAuthToken.getAccess_token();
        String refreshToken = oAuthToken.getRefresh_token();

        kakaoUser.setAccess_token(accessToken);
        kakaoUser.setRefresh_token(refreshToken);

        return kakaoUser;
    }

}
