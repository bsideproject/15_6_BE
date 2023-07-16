package bside.NotToDoClub.domain_name.nottodo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotToDoCreateResponseDto {
    private long notToDoId;
    private String userLoginId;
    private String notToDoText;
    private String startDate;
    private String endDate;
    private String goal;
    private String progressState;
    //private String cheerUpMsg1;
    //private String cheerUpMsg2;
    //private String cheerUpMsg3;
}
