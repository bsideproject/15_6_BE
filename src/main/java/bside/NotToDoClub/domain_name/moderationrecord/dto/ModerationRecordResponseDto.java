package bside.NotToDoClub.domain_name.moderationrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModerationRecordResponseDto {

    private Long moderationId;
    private Long notToDoId;
    private String notToDoText;
    private String content;
    private String regDtm;
    private String recordType;
    private String useYn;

}
