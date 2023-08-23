package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.badge.dto.UserBadgeDto;
import bside.NotToDoClub.domain_name.badge.dto.UserBadgeResponseDto;
import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBadgeService {

    private final AuthTokenProvider authTokenProvider;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    @Value("${app.auth.accessTokenSecret}")
    private String key;

    public List<UserBadgeResponseDto> getUserBadgeList (String accessToken){

        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        List<UserBadgeDto> UserBadgeList = userBadgeJpaRepository.findAllUserBadge(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_BADGE_LIST_NOT_FOUND)
        );

        List<UserBadgeResponseDto> userBadgeResponseDtoList = new ArrayList<>();

        for (UserBadgeDto userBadgeDto : UserBadgeList) {
            List<UserBadge> userBadge = userBadgeJpaRepository.findUserBadgeByBadgeId(userId, userBadgeDto.getBadgeId()).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_BADGE_LIST_NOT_FOUND)
            );

            UserBadgeResponseDto userBadgeResponseDto = new UserBadgeResponseDto(userBadgeDto, userBadge);
            userBadgeResponseDtoList.add(userBadgeResponseDto);
        }


        return userBadgeResponseDtoList;
    }

}
