package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.badge.entity.Badge;
import bside.NotToDoClub.domain_name.badge.entity.UserBadge;
import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeJpaRepository badgeJpaRepository;

    /**
     * 완벽한 출발 - 낫투두명, 목표, 응원메시지 3개 등록을 모두 완료
     * @param notToDoCreateRequestDto
     * @param cheerUpMsgMap
     */
    public void registerAllCondition(NotToDoCreateRequestDto notToDoCreateRequestDto, Map<Integer, String> cheerUpMsgMap, UserEntity user){
        if(notToDoCreateRequestDto.getNotToDoText().isBlank() || notToDoCreateRequestDto.getGoal() == null || notToDoCreateRequestDto.getGoal().isBlank()){
            return;
        }

        for (Integer key : cheerUpMsgMap.keySet()){
            if(cheerUpMsgMap.get(key) == null || cheerUpMsgMap.get(key).isBlank()){
                return;
            }
        }

        long badgeId = BadgeList.REGISTER_ALL_CONDITION;

        Badge badge = badgeJpaRepository.findById(badgeId).orElseThrow(
                () -> new CustomException(ErrorCode.BADGE_NOT_FOUND)
        );

        UserBadge newUserBadge = UserBadge.builder()
                .badge(badge)
                .user(user)
                .build();

        user.getBadges().add(newUserBadge);


    }
}
