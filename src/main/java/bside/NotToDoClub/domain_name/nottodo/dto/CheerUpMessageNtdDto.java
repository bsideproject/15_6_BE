package bside.NotToDoClub.domain_name.nottodo.dto;

import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.global.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheerUpMessageNtdDto {

    Long userId;
    Long cheerUpMsgId;
    String content;
    String useYn;
    int dspOrder;

    public CheerUpMessageNtdDto(CheerUpMessage cheerUpMessage){

        BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();
        userId = cheerUpMessage.getRegisterUser().getId();
        cheerUpMsgId = cheerUpMessage.getId();
        content = cheerUpMessage.getContent();
        useYn = booleanToYNConverter.convertToDatabaseColumn(cheerUpMessage.getUseYn());
        dspOrder = cheerUpMessage.getDspOrder();
    }
}
