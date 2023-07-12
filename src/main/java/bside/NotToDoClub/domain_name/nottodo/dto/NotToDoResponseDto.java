package bside.NotToDoClub.domain_name.nottodo.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NotToDoResponseDto {

    private String notToDoText;
    private String progressState;
    private String goal;
    private int currentChallengeDays;
    private int totalChallengeDays;
    private String startDate;
    private String endDate;
    private String useYn;
    private Date regDtm;
    private Date modDtm;

}
