package bside.NotToDoClub.domain_name.nottodo.dto;

import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NotToDoListResponseDto {
    private long notToDoId;
    private String notToDoText;
    private String goal;
    private String progressState;
    private String startDate;
    private String endDate;
    private String useYn;

}
