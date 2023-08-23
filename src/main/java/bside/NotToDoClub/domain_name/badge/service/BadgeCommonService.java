package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeCommonService {

    private final BadgeJpaRepository badgeJpaRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    public UserBadge createUserBadge(UserEntity userEntity, BadgeList badgeList){
        UserBadge userBadge = UserBadge.builder()
                .user(userEntity)
                .badge(badgeJpaRepository
                        .findById(badgeList.name())
                        .orElseThrow(() -> new CustomException(ErrorCode.BADGE_NOT_FOUND)))
                .build();
        UserBadge savedUserBadge = userBadgeJpaRepository.save(userBadge);
        log.info(savedUserBadge.toString());
        return savedUserBadge;
    }
}
