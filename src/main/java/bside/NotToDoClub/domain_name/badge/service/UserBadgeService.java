package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.badge.dto.UserBadgeDto;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBadgeService {

    private final AuthTokenProvider authTokenProvider;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    @Value("${app.auth.accessTokenSecret}")
    private String key;

    public List<UserBadgeDto> getUserBadgeList (String accessToken){

        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        List<UserBadgeDto> UserBadgeList = userBadgeJpaRepository.findAllUserBadge(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_BADGE_LIST_NOT_FOUND)
        );

        return UserBadgeList;
    }

}
