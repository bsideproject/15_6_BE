package bside.NotToDoClub.domain_name.nottodo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotToDoUpdateRequestDto {
    private String notToDoText;
    private String startDate;
    private String endDate;
    private String goal;
    private String cheerUpMsg1;
    private String cheerUpMsg2;
    private String cheerUpMsg3;
}
