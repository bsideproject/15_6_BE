package bside.NotToDoClub.domain_name.badge;

import bside.NotToDoClub.domain_name.badge.dto.BadgeRequestDto;
import bside.NotToDoClub.domain_name.badge.dto.BadgeResponseDto;
import bside.NotToDoClub.domain_name.badge.service.BadgeService;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping("")
    public ResultResponse<BadgeResponseDto> createBadge(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody BadgeRequestDto badgeRequestDto){
        BadgeResponseDto badge = badgeService.createBadge(accessToken, badgeRequestDto);
        return ResultResponse.of(ResponseCode.CREATE, badge);
    }
}
