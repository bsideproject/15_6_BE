package bside.NotToDoClub.domain_name.badge.dto;

import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBadgeResponseDto {
    private String badgeId;
    private String badgeName;
    private int badgeCnt;
    private String imageUrl;
    private String explanation;
    private String qualification;
    private String gainYn;
    private List<Map<String, Object>> gainDate;

    public UserBadgeResponseDto(UserBadgeDto userBadgeDto, List<UserBadge> userBadgeList){
        this.badgeId = userBadgeDto.getBadgeId();
        this.badgeName = userBadgeDto.getBadgeName();
        this.badgeCnt = userBadgeDto.getBadgeCnt();
        this.imageUrl = userBadgeDto.getImageUrl();
        this.explanation = userBadgeDto.getExplanation();
        this.qualification = userBadgeDto.getQualification();

        if(userBadgeList.size() != 0){
            this.gainDate = new ArrayList<>();
            this.gainYn = "Y";
            for(UserBadge badge : userBadgeList){
                Map<String, Object> dateMap = new HashMap<>();
                dateMap.put("regDtm", badge.getCreatedAt());
                this.gainDate.add(dateMap);
            }
        }
        else {
            this.gainYn = "N";
        }

    }
}
