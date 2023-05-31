package bside.NotToDoClub.domain_name.api.login.vo;

import bside.NotToDoClub.config.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUser {

    private String loginId;
    private String password;
    private String nickname;
    private UserRole role;
}
