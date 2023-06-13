package bside.NotToDoClub.domain_name.auth.config.oauth;

import bside.NotToDoClub.domain_name.auth.dto.KakaoOAuthTokenDto;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth{

    @Value("${spring.OAuth2.kakao.client-id}")
    private String KAKAO_REST_API_KEY;

    @Value("${spring.OAuth2.kakao.redirect-url}")
    private String KAKAO_REDIRECT_URL;

    @Value("${spring.OAuth2.kakao.url}")
    private String KAKAO_LOGIN_URL;

    @Value("${spring.OAuth2.kakao.token-request-url}")
    private String KAKAO_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URL;

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", KAKAO_REST_API_KEY);
        params.put("redirect_uri", KAKAO_REDIRECT_URL);


        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = KAKAO_LOGIN_URL + "?" + parameterString;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
        /*
         * https://accounts.google.com/o/oauth2/v2/auth?scope=profile&response_type=code
         * &client_id="할당받은 id"&redirect_uri="access token 처리")
         * 로 Redirect URL을 생성하는 로직을 구성
         * */
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        //Access-Token 받기
        HttpHeaders headersAccess = new HttpHeaders();
        headersAccess.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_REST_API_KEY);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(params, headersAccess);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_TOKEN_REQUEST_URL, kakaoRequest, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity;
        }

        return null;
    }

    public KakaoOAuthTokenDto getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuthTokenDto kakaoOAuthTokenDto = objectMapper.readValue(response.getBody(), KakaoOAuthTokenDto.class);

        return kakaoOAuthTokenDto;
    }

    public ResponseEntity<String> requestUserInfo(KakaoOAuthTokenDto oAuthToken){
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USER_INFO_URL, HttpMethod.GET, request, String.class);
        return response;
    }

    public KakaoUserInfoDto getUserInfo(ResponseEntity<String> response) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserInfoDto kakaoUserInfoDto = objectMapper.readValue(response.getBody(), KakaoUserInfoDto.class);
        return kakaoUserInfoDto;
    }
}
