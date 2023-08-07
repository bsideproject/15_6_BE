package bside.NotToDoClub.domain_name.moderationrecord.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ModerationRecordUpdateRequestDto {
    private String content;
    private String recordType;
}
