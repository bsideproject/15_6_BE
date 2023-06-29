package bside.NotToDoClub.domain_name.api.login.controller;

import bside.NotToDoClub.config.Constant;
import bside.NotToDoClub.domain_name.api.login.service.LoginService;
import bside.NotToDoClub.domain_name.auth.dto.AuthRequestDto;
import bside.NotToDoClub.domain_name.auth.service.OauthService;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.global.response.AuthResponse;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController @Slf4j
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginRestController {

    private final OauthService oAuthService;
    private final LoginService loginService;

    @GetMapping("/{socialLoginType}/page") //socialLoginType=naver, google, kakao...
    public void socialLoginRedirect(HttpServletResponse response, @PathVariable(name="socialLoginType") String SocialLoginPath) throws Exception {
        log.info("call {} social login", SocialLoginPath);
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        response.sendRedirect(oAuthService.getRedirectUrl(socialLoginType));
    }

    @GetMapping("/auth/google-callback")
    public ResultResponse<AuthResponse> googleCallback(@RequestParam(name="code") String code) throws IOException{
        log.info("google login code = {}", code);
        AuthResponse authResponse = loginService.googleLogin(code);
        return ResultResponse.of(ResponseCode.PROVIDE_APP_TOKEN, authResponse);
    }

    @PostMapping("/auth/kakao-callback")
    public ResultResponse<AuthResponse> kakaoCallback(@RequestBody AuthRequestDto authRequestDto) throws IOException{
        log.info("kakao login code = {}", authRequestDto.getCode());
        AuthResponse authResponse = loginService.kakaoLogin(authRequestDto.getCode());
        return ResultResponse.of(ResponseCode.PROVIDE_APP_TOKEN, authResponse);
    }
}
