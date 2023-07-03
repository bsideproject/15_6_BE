package bside.NotToDoClub.domain_name.user.entity;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.global.BooleanToYNConverter;
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
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOGIN_ID")
    private String loginId;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "USER_ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // OAuth 로그인에 사용
    @Column(name = "PROVIDER")
    private String provider;
    @Column(name = "PROVIDER_ID")
    private String providerId;

    @Column(name = "ACCESS_TOKEN")
    private String accessToken;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(name = "TOS_YN", length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean tosYn;

    public UserEntity createUserEntity(UserDto userDto){
        this.loginId = userDto.getLoginId();
        this.password = userDto.getPassword();
        this.nickname = userDto.getNickname();
        this.role = userDto.getRole();
        this.accessToken = userDto.getAccessToken();
        this.refreshToken = userDto.getRefreshToken();

        return this;
    }

    public void agreeTos(){
        if(this.tosYn){
            throw new IllegalStateException("이미 약관에 동의한 회원입니다.");
        }

        this.tosYn = true;
    }

    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
