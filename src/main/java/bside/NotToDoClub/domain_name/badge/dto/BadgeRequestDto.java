package bside.NotToDoClub.domain_name.badge.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;

@Data
@Builder
@ToString
public class BadgeRequestDto {

    private String name;
    private String imageUrl;
    private String qualification;
    private String explanation;

}
