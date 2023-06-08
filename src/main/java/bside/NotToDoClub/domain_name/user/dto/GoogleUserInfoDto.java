package bside.NotToDoClub.domain_name.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleUserInfoDto {
    private String id;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String picture;
    private String locale;
}
