package bside.NotToDoClub.domain_name.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private String nickname;
    private String email;
    private String profileImgUrl;
    private String tosYn;
}
