package bside.NotToDoClub.domain_name.api.login.controller;

import bside.NotToDoClub.config.Constant;
import bside.NotToDoClub.domain_name.api.login.service.LoginService;
import bside.NotToDoClub.domain_name.auth.dto.AuthRequestDto;
import bside.NotToDoClub.domain_name.auth.service.OauthService;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.domain_name.user.dto.UserResponseDto;
import bside.NotToDoClub.domain_name.user.service.UserLoginService;
import bside.NotToDoClub.global.response.AuthResponse;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController @Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/login")
public class LoginRestController {

    private final OauthService oAuthService;
    private final LoginService loginService;
    private final UserLoginService userService;

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
        AuthResponse authResponse = loginService.kakaoLogin(authRequestDto.getCode(), authRequestDto.getRedirectUri());
        return ResultResponse.of(ResponseCode.PROVIDE_APP_TOKEN, authResponse);
    }

    /**
     * @sihun
     * 로컬PC에서 카카오 응답 테스트를 위해 만든 GET 방식의 컨트롤러
     * @param code : 응답 코드
     * @return
     * @throws IOException
     */
    @GetMapping("/kakao")
    public ResultResponse<AuthResponse> kakaoCallbacks(@RequestParam(name="code") String code) throws IOException {
        log.info("kakao login code = {}", code);
        String dummyUri = "http://localhost:8080/login/kakao";
        AuthResponse authResponse = loginService.kakaoLogin(code, dummyUri);
        return ResultResponse.of(ResponseCode.PROVIDE_APP_TOKEN, authResponse);
    }

    @PostMapping("/auth/apple-callback")
    public ResultResponse<UserRequestDto> appleCallback(@RequestParam(name="code") String code) throws Exception {
        UserRequestDto userRequestDto = loginService.appleLogin(code);
        return ResultResponse.of(ResponseCode.GET_USER_INFO, userRequestDto);
    }

    @PutMapping("/tos")
    public ResultResponse<String> tosAgree(@RequestHeader(value="access-token")String accessToken){
        String tosYn = userService.tosAgree(accessToken);
        return ResultResponse.of(ResponseCode.TOS_AGREE, tosYn);
    }

    @PutMapping("/auto-login")
    @Operation(summary = "자동 로그인", description = "자동 로그인 동의 여부 API")
    public ResultResponse<UserResponseDto> autoLoginAgree(
            @RequestHeader(value="access-token")String accessToken,
            @RequestParam(name = "yn") Boolean autoLogin){
        UserResponseDto result = userService.autoLoginAgree(accessToken, autoLogin);
        return ResultResponse.of(ResponseCode.AUTO_LOGIN_AGREE, result);
    }
}
