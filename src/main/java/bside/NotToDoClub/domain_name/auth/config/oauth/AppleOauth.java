package bside.NotToDoClub.domain_name.auth.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOauth {

    /**
     *     apple:
     *       url:
     *       team-id:
     *       redirect-url:
     *       client-id:
     *       key-id:
     *       key-path:
     */
    @Value("${spring.OAuth2.apple.url}")
    private String KAKAO_LOGIN_URL;

    @Value("${spring.OAuth2.kakao.team-id}")
    private String APPLE_KEY_ID;

    @Value("${spring.OAuth2.kakao.url}")
    private String KAKAO_LOGIN_URL;

    @Value("${spring.OAuth2.kakao.token-request-url}")
    private String KAKAO_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URL;
}
