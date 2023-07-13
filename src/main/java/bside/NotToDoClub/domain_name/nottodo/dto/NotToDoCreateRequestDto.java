package bside.NotToDoClub.domain_name.nottodo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotToDoCreateRequestDto {
    private String notToDoText;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String goal;
    private String cheerUpMsg1;
    private String cheerUpMsg2;
    private String cheerUpMsg3;
}
