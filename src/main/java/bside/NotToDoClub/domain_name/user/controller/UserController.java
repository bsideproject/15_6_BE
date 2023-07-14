package bside.NotToDoClub.domain_name.user.controller;

import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.domain_name.user.dto.UserResponseDto;
import bside.NotToDoClub.domain_name.user.service.UserLoginService;
import bside.NotToDoClub.domain_name.user.service.UserService;
import bside.NotToDoClub.global.BooleanToYNConverter;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserLoginService userLoginService;
    private final UserService userService;
    private final ModelMapper mapper;

    @PostMapping("/info")
    public ResultResponse<UserResponseDto> getUserInfo(@RequestHeader(value="access-token")String accessToken){
        log.info("access token = {}", accessToken);
        UserDto findUser = userLoginService.getLoginUserInfo(accessToken);
        log.info("find user by token = {}", findUser);

        BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .email(findUser.getLoginId())
                .nickname(findUser.getNickname())
                .profileImgUrl(findUser.getProfileImgUrl())
                .tosYn(booleanToYNConverter.convertToDatabaseColumn(findUser.isTosYn()))
                .build();

        return ResultResponse.of(ResponseCode.GET_USER_INFO, userResponseDto);
    }

    @GetMapping("/update/nickname")
    public ResultResponse<UserResponseDto> updateUserNickname(
            @RequestHeader(value = "access-token") String accessToken,
            @RequestParam(value = "nickname")String nickname
    ){
        UserDto userDto = userService.updateUserNickname(accessToken, nickname);

        UserResponseDto userResponseDto = mapper.map(userDto, UserResponseDto.class);

        return ResultResponse.of(ResponseCode.GET_USER_INFO, userResponseDto);
    }

    @GetMapping("/delete")
    public ResultResponse<UserResponseDto> deleteUser(
            @RequestHeader(value = "access-token") String accessToken
    ){
        userService.deleteUser(accessToken);
        return null;
    }
}
