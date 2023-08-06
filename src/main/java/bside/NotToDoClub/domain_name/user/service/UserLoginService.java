package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.dto.UserResponseDto;

public interface UserLoginService {
    boolean checkLoginIdDuplicate(String loginId);
    boolean checkNicknameDuplicate(String nickname);
//    void join(UserDto req);
    void createUser(UserDto req);
    UserDto login(UserDto req);
    UserDto getLoginUserById(Long userId);
    UserDto getLoginUserByLoginId(String loginId);

    UserDto getLoginUserInfo(String accessToken);

    String tosAgree(String accessToken);

    UserResponseDto autoLoginAgree(String accessToken, Boolean autoLogin);
}
