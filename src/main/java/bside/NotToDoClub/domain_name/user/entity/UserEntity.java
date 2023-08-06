package bside.NotToDoClub.domain_name.user.entity;

import bside.NotToDoClub.config.UserRole;
import bside.NotToDoClub.domain_name.inquiry.entity.Inquiry;
import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.global.BooleanToYNConverter;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@ToString(exclude = {"badges", "userNotToDoList", "cheerUpMessages"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    /**
     * 소셜 로그인 id
     * kakao: kakao email id
     * google: google email id
     */
    @Column(name = "LOGIN_ID")
    private String loginId;

    /**
     * 비밀번호
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 사용자 닉네임
     */
    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "PROFILE_IMG_URL")
    private String profileImgUrl;

    /**
     * 어플리케이션내 유저 권한
     * ADMIN, USER
     */
    @Column(name = "USER_ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // OAuth 로그인에 사용
    @Column(name = "PROVIDER")
    private String provider;
    @Column(name = "PROVIDER_ID")
    private String providerId;

    /**
     * 소셜 로그인 code 발급후 사용자 인증 token
     */
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    /**
     * 이용약관 동의 여부
     */
    @Column(name = "TOS_YN", length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean tosYn;

    @Column(name = "AUTO_LOGIN_YN", length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean autoLoginYn = false;

    /**
     * user - badge
     * N:M 관계 테이블 매핑을 위한 user-badge 테이블
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<UserBadge> badges = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<UserNotToDo> userNotToDoList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<Inquiry> userInquiryList = new ArrayList<>();

    @OneToMany(mappedBy = "registerUser", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<CheerUpMessage> cheerUpMessages = new ArrayList<>();

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UserEntity createUserEntity(UserDto userDto){
        this.loginId = userDto.getLoginId();
        this.password = userDto.getPassword();
        this.nickname = userDto.getNickname();
        this.role = userDto.getRole();
        this.accessToken = userDto.getAccessToken();
        this.refreshToken = userDto.getRefreshToken();

        return this;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void agreeTos(UserEntity userEntity){
        if(userEntity.isTosYn()){
            throw new CustomException(ErrorCode.ALREADY_TOS_AGREE);
        }

        this.tosYn = true;
    }

    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void updateAutoLoginYn(Boolean autoLoginYn){
        this.autoLoginYn = autoLoginYn;
    }
}
