package bside.NotToDoClub.domain_name.user.dto;

import bside.NotToDoClub.config.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String loginId;
    private String password;
    private String nickname;
    private UserRole role;
    private String accessToken;
    private String refreshToken;
}
