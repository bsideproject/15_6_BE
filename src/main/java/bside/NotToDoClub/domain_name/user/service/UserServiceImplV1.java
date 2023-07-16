package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService{

    private final UserJpaRepository userJpaRepository;
    private final ModelMapper mapper;
//    private final AuthTokenProvider authTokenProvider;
    private final UserCommonService userCommonService;

//    @Value("${app.auth.accessTokenSecret}")
//    private String key;

    @Override
    public UserDto updateUserNickname(String accessToken, String nickname) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        userEntity.updateNickname(nickname);
        UserEntity updateUser = userJpaRepository.save(userEntity);

        UserDto userDto = mapper.map(updateUser, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto deleteUser(String accessToken) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        UserEntity deleteUser = userJpaRepository
                .deleteByAccessToken(userEntity.getAccessToken()).get();

        UserDto userDto = mapper.map(deleteUser, UserDto.class);

        return userDto;
    }

}
