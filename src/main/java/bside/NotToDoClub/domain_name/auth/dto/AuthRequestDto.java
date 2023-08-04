package bside.NotToDoClub.domain_name.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    String code;
    String redirectUri;
}
