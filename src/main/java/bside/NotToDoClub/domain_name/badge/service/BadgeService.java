package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.badge.dto.BadgeRequestDto;
import bside.NotToDoClub.domain_name.badge.dto.BadgeResponseDto;
import bside.NotToDoClub.domain_name.badge.entity.Badge;
import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final BadgeJpaRepository badgeJpaRepository;
    private final ModelMapper mapper;
    private final UserCommonService userCommonService;

    public BadgeResponseDto createBadge(String accessToken, BadgeRequestDto badgeRequestDto){

        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);
        if(userEntity.getRole() != UserRole.ADMIN){
            throw new RuntimeException("관리자가 아닌 회원은 뱃지를 등록 할 수 없습니다.");
        }

        Badge badge = Badge.builder()
                .name(badgeRequestDto.getName())
                .explanation(badgeRequestDto.getExplanation())
                .qualification(badgeRequestDto.getQualification())
                .build();

        Badge savedBadge = badgeJpaRepository.save(badge);

        BadgeResponseDto result = mapper.map(savedBadge, BadgeResponseDto.class);

        return result;
    }
}
