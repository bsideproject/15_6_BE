package bside.NotToDoClub.domain_name.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {

    private String nickname;
    private String loginId;
    private String profileImgUrl;
    private Boolean tosYn;
    private Boolean autoLoginYn;

    @Builder
    public UserResponseDto(String nickname, String loginId, String profileImgUrl, Boolean tosYn, Boolean autoLoginYn) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.profileImgUrl = profileImgUrl;
        this.tosYn = tosYn;
        this.autoLoginYn = autoLoginYn;
    }
}
