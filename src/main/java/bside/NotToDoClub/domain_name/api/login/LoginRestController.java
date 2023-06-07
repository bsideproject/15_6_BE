package bside.NotToDoClub.domain_name.api.login;

import bside.NotToDoClub.config.Constant;
import bside.NotToDoClub.domain_name.auth.oauth.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginRestController {

    private final OauthService oAuthService;

    @GetMapping("/auth/{socialLoginType}") //socialLoginType=naver, google, kakao...
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }
}
