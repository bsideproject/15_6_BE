package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.dto.UserDto;

public interface UserService {
    boolean checkLoginIdDuplicate(String loginId);
    boolean checkNicknameDuplicate(String nickname);
//    void join(UserDto req);
    void createUser(UserDto req);
    UserDto login(UserDto req);
    UserDto getLoginUserById(Long userId);
    UserDto getLoginUserByLoginId(String loginId);

    UserDto getLoginUserByAccessToken(String accessToken);

    String tosAgree(String accessToken);
}
