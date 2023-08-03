package bside.NotToDoClub.domain_name.user.controller;

import bside.NotToDoClub.domain_name.nottodo.dto.CheerUpMessageDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.dto.UserNotToDoStatusNumberDto;
import bside.NotToDoClub.domain_name.user.dto.UserRequestDto;
import bside.NotToDoClub.domain_name.user.dto.UserResponseDto;
import bside.NotToDoClub.domain_name.user.service.UserLoginService;
import bside.NotToDoClub.domain_name.user.service.UserService;
import bside.NotToDoClub.global.BooleanToYNConverter;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User", description = "User 관련 정보 CRUD")
@RequiredArgsConstructor
public class UserController {

    private final UserLoginService userLoginService;
    private final UserService userService;
    private final ModelMapper mapper;


    @GetMapping("/info")
    @Operation(summary = "유저 정보 조회", description = "header의 access-token을 기반으로 유저 정보를 가져오는 api")
    public ResultResponse<UserResponseDto> getUserInfo(
            @RequestHeader(value="access-token")String accessToken){
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

    @PutMapping("/nickname")
    @Operation(summary = "유저 닉네임 변경", description = "유저 닉네임 변경 api")
    public ResultResponse<UserResponseDto> updateUserNickname(
            @RequestHeader(value = "access-token") String accessToken,
            @RequestParam(value = "nickname")String nickname
    ){
        UserDto userDto = userService.updateUserNickname(accessToken, nickname);

        UserResponseDto userResponseDto = mapper.map(userDto, UserResponseDto.class);

        return ResultResponse.of(ResponseCode.UPDATE, userResponseDto);
    }

    @DeleteMapping("")
    @Operation(summary = "유저 삭제", description = "유저 삭제 api")
    public ResultResponse<UserResponseDto> deleteUser(
            @RequestHeader(value = "access-token") String accessToken
    ){
        UserDto userDto = userService.deleteUser(accessToken);

        UserResponseDto userResponseDto = mapper.map(userDto, UserResponseDto.class);

        return ResultResponse.of(ResponseCode.DELETE_USER, userResponseDto);
    }

    @GetMapping("/nottodolist/inprogress")
    @Operation(summary = "[#2 홈 메인 - 4]현재 진행중인 not to do 리스트", description = "#2 홈 메인 - " +
            "4번 홈 탭에서 조회 가능한 낫투두\n" +
            "홈 탭에서는 현재 도전 중인 낫투두만 열람 가능\n" +
            "열람일 기준 현재 도전 기간 중인 낫투두 (도전 기간 마감일 ~23:59까지 홈탭 노출)\n" )
    public ResultResponse<List<NotToDoListResponseDto>> getInProgressNotToDoList(
            @RequestHeader(value = "access-token") String accessToken
    ){
        List<NotToDoListResponseDto> inProgressUserNotTodoList = userService.findInProgressUserNotTodoList(accessToken);

        return ResultResponse.of(ResponseCode.OK_, inProgressUserNotTodoList);
    }

    @GetMapping("/nottodoList/status/number")
    @Operation(summary = "[#2 홈 메인 - 8]not to do 리스트 상태별 갯수 조회", description = "#2 홈 메인 - " +
            "일자별 절제 현황\n" +
            "\n" +
            "사용자가 기록한 절제 기록의 성공 비율을 일 단위로 계산하여 캘린더에 일자별로 시각화하여 노출\n" +
            "계산식 = 성공으로 기록한 절제기록의 수/기록한 절제 기록의 수 * 100 (소수점 두자리수에서 반올림)\n" +
            "계산 결과를 Good/Not Bad/Bad로 나누고 각 결과값을 구분하여 캘린더에 일자별로 나타낸다.\n" +
            "실제 서비스에는 디자인 요소만 보여지고 결과값 (숫자 및 텍스트)은 노출되지 않는다.\n" +
            "Good : 하루 동안 기록한 절제 기록 중 성공 비율이 80% 이상일 때\n" +
            "Not Bad : 55% 이상 80% 미만\n" +
            "Bad : 55% 미만\n")
    public ResultResponse<UserNotToDoStatusNumberDto> getUserNotToDoListStatus(
            @RequestHeader(value = "access-token") String accessToken
    ){
        UserNotToDoStatusNumberDto userNotToDoStatus = userService.getUserNotToDoStatus(accessToken);

        return ResultResponse.of(ResponseCode.OK_, userNotToDoStatus);
    }

    @GetMapping("/cheeruplist")
    @Operation(summary = "[#2 홈 메인 - 5]유저 응원리스트 조회", description = "#2 홈 메인 - " +
            "5번 응원 메시지\n" +
            "#3-1.새 낫투두 등록 화면 > 응원 메시지 등록한 유저의 경우 해당 텍스트가 응원 메시지 영역에 노출\n" +
            "1개 등록 시 : 1개 메시지 고정 노출\n" +
            "2,3개 등록 시 : 탭 접속 시 N개 메시지 랜덤 노출\n" +
            "응원 메시지 등록하지 않은 유저 대상 프리셋 랜덤 노출\n" +
            "{유저}님, 오늘도 화이팅하세요! \n" +
            "{유저}님, 딱 한 번만 더 참아볼까요?\n" +
            "절제가 습관이 되어 달라진 {유저}님을 떠올려보세요!")
    public ResultResponse<List<CheerUpMessageDto>> getCheerUpMessages(
            @RequestHeader(value = "access-token") String accessToken
    ){
        List<CheerUpMessageDto> cheerupList = userService.findCheerupList(accessToken);

        return ResultResponse.of(ResponseCode.OK_, cheerupList);
    }
}
