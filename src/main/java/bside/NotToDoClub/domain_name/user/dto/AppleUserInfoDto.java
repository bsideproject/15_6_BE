package bside.NotToDoClub.domain_name.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppleUserInfoDto {
    private String uniqueId;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String picture;
    private String locale;
    private String access_token;
    private String refresh_token;

    @Builder
    public AppleUserInfoDto(String uniqueId, String email, Boolean verified_email, String name, String given_name, String picture, String locale, String access_token, String refresh_token) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.picture = picture;
        this.locale = locale;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
}
