package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.badge.dto.UserBadgeResponseDto;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordListResponseDto;
import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
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

    public List<UserBadgeResponseDto> getUserBadgeList (String accessToken){

        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        List<UserBadgeResponseDto> UserBadgeList = userBadgeJpaRepository.findAllUserBadge(userId);


        return null;
    }

}
