package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService{

    private final UserJpaRepository userJpaRepository;
    private final ModelMapper mapper;

    @Override
    public UserDto updateUserNickname(String accessToken, String nickname) {

        UserEntity findUser = checkUserByToken(accessToken).get();

        findUser.updateNickname(nickname);
        UserEntity updateUser = userJpaRepository.save(findUser);

        UserDto userDto = mapper.map(updateUser, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto deleteUser(String accessToken) {
        checkUserByToken(accessToken);

        UserEntity userEntity = userJpaRepository.deleteByAccessToken(accessToken).get();

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        return userDto;
    }

    private Optional<UserEntity> checkUserByToken(String accessToken) {
        Optional<UserEntity> findUserOptional = userJpaRepository.findByAccessToken(accessToken);

        if(findUserOptional.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return findUserOptional;
    }
}
