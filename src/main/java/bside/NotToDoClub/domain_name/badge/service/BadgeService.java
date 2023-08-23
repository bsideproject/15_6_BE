package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.badge.entity.Badge;
import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeJpaRepository badgeJpaRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    /**
     * 공통 - 사용자 뱃지 추가
     * @param badgeId
     * @param user
     */
    public void presentBadge(String badgeId, UserEntity user){
        Badge badge = badgeJpaRepository.findById(badgeId).orElseThrow(
                () -> new CustomException(ErrorCode.BADGE_NOT_FOUND)
        );

        try {
            UserBadge newUserBadge = UserBadge.builder()
                    .badge(badge)
                    .user(user)
                    .build();

            userBadgeJpaRepository.save(newUserBadge);
            user.getBadges().add(newUserBadge);
        }
        catch (Exception e){
            throw new CustomException(ErrorCode.BADGE_PRESENT_FAIL);
        }
    }

    /**
     * 완벽한 출발 - 낫투두명, 목표, 응원메시지 3개 등록을 모두 완료
     * @param notToDoCreateRequestDto
     * @param cheerUpMsgMap
     */
    @Transactional
    public void registerAllCondition(NotToDoCreateRequestDto notToDoCreateRequestDto, Map<Integer, String> cheerUpMsgMap, UserEntity user){

        //낫투두 텍스트, 목표 입력 체크
        if(notToDoCreateRequestDto.getNotToDoText().isBlank() || notToDoCreateRequestDto.getGoal() == null || notToDoCreateRequestDto.getGoal().isBlank()){
            return;
        }

        //응원메세지 입력 체크
        for (Integer key : cheerUpMsgMap.keySet()){
            if(cheerUpMsgMap.get(key) == null || cheerUpMsgMap.get(key).isBlank()){
                return;
            }
        }

        presentBadge(BadgeList.PERFECT_START.toString(), user);

    }
}
