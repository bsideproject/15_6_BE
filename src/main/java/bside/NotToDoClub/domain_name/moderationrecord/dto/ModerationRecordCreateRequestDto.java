package bside.NotToDoClub.domain_name.moderationrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ModerationRecordCreateRequestDto {
    private String content;
    private String recordType;
}
