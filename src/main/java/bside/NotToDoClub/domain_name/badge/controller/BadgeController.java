package bside.NotToDoClub.domain_name.badge.controller;

import bside.NotToDoClub.domain_name.badge.dto.UserBadgeDto;
import bside.NotToDoClub.domain_name.badge.dto.UserBadgeResponseDto;
import bside.NotToDoClub.domain_name.badge.service.UserBadgeService;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/badge")
@Tag(name = "Badge", description = "뱃지")
public class BadgeController {

    private final UserBadgeService userBadgeService;

    /**
     * 사용자 뱃지 리스트 조회
     */
    @GetMapping("/list")
    @Operation(summary = "사용자 뱃지 리스트 조회")
    public ResultResponse<List<UserBadgeResponseDto>> getUserBadgeList(
            @RequestHeader(value="access-token")String accessToken){
        List<UserBadgeResponseDto> userBadgeList = userBadgeService.getUserBadgeList(accessToken);

        return  ResultResponse.of(ResponseCode.GET_USER_BADGE_LIST, userBadgeList);
    }
}
