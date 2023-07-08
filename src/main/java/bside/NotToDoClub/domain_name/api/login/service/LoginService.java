package bside.NotToDoClub.domain_name.api.login.service;

import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.auth.dto.TokenDto;
import bside.NotToDoClub.domain_name.auth.service.OauthService;
import bside.NotToDoClub.domain_name.user.dto.AppleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.KakaoUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.BooleanToYNConverter;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import bside.NotToDoClub.global.response.AuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class LoginService {

    private final OauthService oAuthService;
    private final UserJpaRepository userRepository;
    private final AuthTokenProvider authTokenProvider;

    public AuthResponse googleLogin(String code) throws JsonProcessingException {
        GoogleUserInfoDto googleUser = oAuthService.getGoogleUserInfo(code);

        TokenDto tokenDto = authTokenProvider.createAccessToken(googleUser.getEmail(), UserRole.USER);

        if(!userRepository.existsByLoginId(googleUser.getEmail())){

            userRepository.save(
                    UserEntity.builder()
                            .loginId(googleUser.getEmail())
                            .nickname(googleUser.getName())
                            .password("google")
                            .role(UserRole.USER)
                            .accessToken(tokenDto.getAccessToken())
                            .refreshToken(tokenDto.getRefreshToken())
                            .build()
            );

            /*UserEntity userEntity = userRepository.findByLoginId(googleUser.getEmail()).orElseThrow(
                    () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
            );

            UserRequestDto userRequestDto = new UserRequestDto(userEntity);

            return userRequestDto;*/
        }

        /*UserEntity userEntity = userRepository.findByLoginId(googleUser.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
        );
        UserRequestDto userRequestDto = new UserRequestDto(userEntity);*/

        return AuthResponse.builder()
                .appAccessToken(tokenDto.getAccessToken())
                .appRefreshToken(tokenDto.getRefreshToken())
                .build();

    }

    @Transactional
    public AuthResponse kakaoLogin(String code) throws JsonProcessingException {
        KakaoUserInfoDto kakaoUser = oAuthService.getKakaoUserInfo(code);

        //AuthToken appToken = authTokenProvider.createUserAppToken(kakaoUser.getKakao_account().getEmail());
        TokenDto tokenDto = authTokenProvider.createAccessToken(kakaoUser.getKakao_account().getEmail(), UserRole.USER);

        //사용자 정보 없으면 DB 저장 (회원가입)
        if(!userRepository.existsByLoginId(kakaoUser.getKakao_account().getEmail())){

            userRepository.save(
                    UserEntity.builder()
                            .loginId(kakaoUser.getKakao_account().getEmail())
                            .nickname(kakaoUser.getProperties().getNickname())
                            .password("kakao")
                            .role(UserRole.USER)
                            .accessToken(tokenDto.getAccessToken())
                            .refreshToken(tokenDto.getRefreshToken())
                            .tosYn(false)
                            .build()
            );

        }

        UserEntity userEntity = userRepository.findByLoginId(kakaoUser.getKakao_account().getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );


        userEntity.updateAccessToken(tokenDto.getAccessToken());
        userEntity.updateRefreshToken(tokenDto.getRefreshToken());


        return AuthResponse.builder()
                .appAccessToken(tokenDto.getAccessToken())
                .appRefreshToken(tokenDto.getRefreshToken())
                .email(kakaoUser.getKakao_account().getEmail())
                .nickname(kakaoUser.getProperties().getNickname())
                //.isNew(booleanToYNConverter.convertToDatabaseColumnReverse(userEntity.isTosYn()))
                .build();

    }

    public UserRequestDto appleLogin(String code) throws Exception {
        /**
         * TODO: 회원 정보 저장 후 회원정보 return -> LoginRestController(apple-callback) 값 넘겨주기
         */
        AppleUserInfoDto appleUser = oAuthService.getAppleUserInfo(code);

        if(!userRepository.existsByLoginId(appleUser.getEmail())){

            userRepository.save(
                    UserEntity.builder()
                            .loginId(appleUser.getEmail())
//                            .nickname(appleUser.getProperties().getNickname())
                            .password("apple")
                            .role(UserRole.USER)
                            .accessToken(appleUser.getAccess_token())
                            .refreshToken(appleUser.getRefresh_token())
                            .build()
            );

            UserEntity userEntity = userRepository.findByLoginId(appleUser.getEmail()).orElseThrow(
                    () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
            );

            UserRequestDto userRequestDto = new UserRequestDto(userEntity);

            return userRequestDto;
        }

        UserEntity userEntity = userRepository.findByLoginId(appleUser.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
        );
        UserRequestDto userRequestDto = new UserRequestDto(userEntity);

        return userRequestDto;
    }
}
