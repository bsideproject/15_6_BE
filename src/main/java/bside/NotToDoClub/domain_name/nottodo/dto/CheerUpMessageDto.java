package bside.NotToDoClub.domain_name.nottodo.dto;

import bside.NotToDoClub.domain_name.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheerUpMessageDto {
    UserDto userDto;
    String content;
    Boolean userYn;
}
