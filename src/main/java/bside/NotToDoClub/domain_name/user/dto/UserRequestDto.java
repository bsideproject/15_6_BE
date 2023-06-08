package bside.NotToDoClub.domain_name.user.dto;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String loginId;
    private String nickname;
    private UserRole role;

    public UserRequestDto(UserEntity userEntity){
        loginId = userEntity.getLoginId();
        nickname = userEntity.getNickname();
        role = userEntity.getRole();
    }
}
