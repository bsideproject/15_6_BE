package bside.NotToDoClub.domain_name.badge.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class BadgeResponseDto {

    private Long id;
    private String name;
    private String imageUrl;
    private String qualification;
    private String explanation;
}
