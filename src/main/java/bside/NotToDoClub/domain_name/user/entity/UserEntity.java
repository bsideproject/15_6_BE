package bside.NotToDoClub.domain_name.user.entity;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // OAuth 로그인에 사용
    private String provider;
    private String providerId;

    private String accessToken;
    private String refreshToken;

    public UserEntity createUserEntity(UserDto userDto){
        this.loginId = userDto.getLoginId();
        this.password = userDto.getPassword();
        this.nickname = userDto.getNickname();
        this.role = userDto.getRole();
        this.accessToken = userDto.getAccessToken();
        this.refreshToken = userDto.getRefreshToken();

        return this;
    }
}
