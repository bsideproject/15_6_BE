package bside.NotToDoClub.domain_name.nottodo.dto;

import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NotToDoListResponseDto {

    private String notToDoText;
    private String goal;
    private ProgressState progressState;
    private String startDate;
    private String endDate;
    private String useYn;

}
