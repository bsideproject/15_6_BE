package bside.NotToDoClub.domain_name.moderationrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModerationRecordCreateResponseDto {
    private Long moderationId;
}
