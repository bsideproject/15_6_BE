package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.dto.UserDto;

public interface UserService {

    UserDto updateUserNickname(String accessToken, String nickname);
    UserDto deleteUser(String accessToken);
}
