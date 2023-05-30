package bside.NotToDoClub.domain_name.user.entity;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String nickname;

    private UserRole role;

    // OAuth 로그인에 사용
    private String provider;
    private String providerId;

    public UserEntity createUserEntity(UserDto userDto){
        this.loginId = userDto.getLoginId();
        this.password = userDto.getPassword();
        this.nickname = userDto.getNickname();
        this.role = userDto.getRole();
        return this;
    }
}
