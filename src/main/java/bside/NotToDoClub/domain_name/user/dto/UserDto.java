package bside.NotToDoClub.domain_name.user.dto;

import bside.NotToDoClub.config.UserRole;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder @ToString
public class UserDto {

    private String loginId;
    private String password;
    private String nickname;
    private UserRole role;
    private String accessToken;
    private String refreshToken;
    private boolean tosYn;
}
