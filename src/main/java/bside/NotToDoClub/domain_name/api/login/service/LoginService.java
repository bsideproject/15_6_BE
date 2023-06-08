package bside.NotToDoClub.domain_name.api.login.service;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.auth.service.OauthService;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final OauthService oAuthService;
    private final UserRepository userRepository;

    public UserRequestDto googleLogin(String code) throws JsonProcessingException {
        GoogleUserInfoDto googleUser = oAuthService.getGoogleUserInfo(code);

        if(!userRepository.existsByLoginId(googleUser.getEmail())){

            userRepository.save(
                    UserEntity.builder()
                            .loginId(googleUser.getEmail())
                            .nickname(googleUser.getName())
                            .password("google")
                            .role(UserRole.USER)
                            .build()
            );

            UserEntity userEntity = userRepository.findByLoginId(googleUser.getEmail()).orElseThrow(
                    () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
            );

            System.out.println("userEntity = " + userEntity.getLoginId());

            UserRequestDto userRequestDto = new UserRequestDto(userEntity);

            System.out.println("userRequestDto = " + userRequestDto);

            return userRequestDto;
        }

        UserEntity userEntity = userRepository.findByLoginId(googleUser.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.TOKEN_AUTHENTICATION_FAIL)
        );
        UserRequestDto userRequestDto = new UserRequestDto(userEntity);

        return userRequestDto;

    }
}
