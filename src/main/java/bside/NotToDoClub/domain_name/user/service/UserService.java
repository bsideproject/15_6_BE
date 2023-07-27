package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.nottodo.dto.CheerUpMessageDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.dto.UserNotToDoStatusNumberDto;

import java.util.List;

public interface UserService {

    UserDto updateUserNickname(String accessToken, String nickname);
    UserDto deleteUser(String accessToken);
    List<NotToDoListResponseDto> findInProgressUserNotTodoList(String accessToken);
    List<CheerUpMessageDto> findCheerupList(String accessToken);
    UserNotToDoStatusNumberDto getUserNotToDoStatus(String accessToken);

}
