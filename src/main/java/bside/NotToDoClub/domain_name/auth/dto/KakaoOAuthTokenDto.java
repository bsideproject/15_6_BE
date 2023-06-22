package bside.NotToDoClub.domain_name.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoOAuthTokenDto {
    private String access_token;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private Integer expires_in;
    private String scope;
    private String token_type;
}
