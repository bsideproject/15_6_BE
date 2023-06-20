package bside.NotToDoClub.domain_name.api.login.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.auth.service.OauthService;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.KakaoUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import bside.NotToDoClub.global.response.AuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final OauthService oAuthService;
    private final UserRepository userRepository;
    private final AuthTokenProvider authTokenProvider;

    public UserRequestDto googleLogin(String code) throws JsonProcessingException {
        GoogleUserInfoDto googleUser = oAuthService.getGoogleUserInfo(code);

        if(!userRepository.existsByLoginId(googleUser.getEmail())){

            userRepository.save(
                    UserEntity.builder()
                            .loginId(googleUser.getEmail())
                            .nickname(googleUser.getName())
                            .password("google")
                            .role(UserRole.USER)
                            .accessToken(googleUser.getAccess_token())
                            .refreshToken(googleUser.getRefresh_token())
                            .build()
            );

            UserEntity userEntity = userRepository.findByLoginId(googleUser.getEmail()).orElseThrow(
                    () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
            );

            UserRequestDto userRequestDto = new UserRequestDto(userEntity);

            return userRequestDto;
        }

        UserEntity userEntity = userRepository.findByLoginId(googleUser.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
        );
        UserRequestDto userRequestDto = new UserRequestDto(userEntity);

        return userRequestDto;

    }

    public AuthResponse kakaoLogin(String code) throws JsonProcessingException {
        KakaoUserInfoDto kakaoUser = oAuthService.getKakaoUserInfo(code);

        AuthToken appToken = authTokenProvider.createUserAppToken(kakaoUser.getKakao_account().getEmail());

        //사용자 정보 없으면 DB 저장 (회원가입)
        if(!userRepository.existsByLoginId(kakaoUser.getKakao_account().getEmail())){

            userRepository.save(
                    UserEntity.builder()
                            .loginId(kakaoUser.getKakao_account().getEmail())
                            .nickname(kakaoUser.getProperties().getNickname())
                            .password("kakao")
                            .role(UserRole.USER)
                            .accessToken(kakaoUser.getAccess_token())
                            .refreshToken(kakaoUser.getRefresh_token())
                            .build()
            );

            /*UserEntity userEntity = userRepository.findByLoginId(kakaoUser.getKakao_account().getEmail()).orElseThrow(
                    () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
            );

            UserRequestDto userRequestDto = new UserRequestDto(userEntity);

            return userRequestDto;*/
        }

        /*UserEntity userEntity = userRepository.findByLoginId(kakaoUser.getKakao_account().getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
        );

        UserRequestDto userRequestDto = new UserRequestDto(userEntity);*/

        return AuthResponse.builder()
                .appToken(appToken.getToken())
                .build();

    }
}
