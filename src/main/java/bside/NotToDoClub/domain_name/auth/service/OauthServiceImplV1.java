package bside.NotToDoClub.domain_name.auth.service;

import bside.NotToDoClub.config.Constant;
import bside.NotToDoClub.domain_name.auth.config.oauth.AppleOauth;
import bside.NotToDoClub.domain_name.auth.config.oauth.GoogleOauth;
import bside.NotToDoClub.domain_name.auth.config.oauth.KakaoOauth;
import bside.NotToDoClub.domain_name.auth.dto.GoogleOAuthTokenDto;
import bside.NotToDoClub.domain_name.auth.dto.KakaoOAuthTokenDto;
import bside.NotToDoClub.domain_name.user.dto.AppleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OauthServiceImplV1 implements OauthService {
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;
    private final AppleOauth appleOauth;
    private final HttpServletResponse response;

    @Override
    public String getRedirectUrl(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType){
            case GOOGLE:{
                redirectURL= googleOauth.getOauthRedirectURL();
                break;
            }
            case KAKAO:{
                redirectURL = kakaoOauth.getOauthRedirectURL();
                break;
            }
            case APPLE:{
                redirectURL = appleOauth.getOauthRedirectURL();
                break;
            }
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }

        return redirectURL;
    }

    @Override
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

    @Override
    public KakaoUserInfoDto getKakaoUserInfo(String code, String redirectUri) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code, redirectUri);
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

    @Override
    public AppleUserInfoDto getAppleUserInfo(String code) throws Exception {
        String apiResponse = appleOauth.getApiResponse(code);

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject tokenResponse = objectMapper.readValue(apiResponse, JSONObject.class);

        log.info("apple token response value {}", String.valueOf(tokenResponse));

        String accessToken = tokenResponse.getString("access_token");
        String refreshToken = tokenResponse.getString("refresh_token");

        // 애플 정보조회 성공
        if (tokenResponse.get("error") == null ) {
            /**
             *
             * TODO : 리턴받은 appleUniqueNo 해당하는 회원정보 LoginService로 넘겨주기(LoginService에서 회원정보 저장)
             * appleUniqueNo은 apple login 유저의 고유값을 넘겨주게 된다.
             *
             */
            // 애플 회원 정보
            JSONObject payload = appleOauth.decodeFromIdToken(tokenResponse.getString("id_token"));
            log.info("apple user info= {}", payload);
            String email = payload.getString("email") + ".apple";

            //  회원 고유 식별자
            String appleUniqueNo = payload.getString("sub");
            log.info("apple unique no= {}",appleUniqueNo);

            AppleUserInfoDto appleUserInfoDto = AppleUserInfoDto.builder()
                    .access_token(accessToken)
                    .refresh_token(refreshToken)
                    .email(email)
                    .uniqueId(appleUniqueNo).build();

            return appleUserInfoDto;

        } else {
//            throw new ErrorMessage("애플 정보조회에 실패했습니다.");
            throw new Exception("애플 정보조회에 실패했습니다.");
        }
    }

}
