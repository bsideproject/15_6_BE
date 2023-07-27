package bside.NotToDoClub.domain_name.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNotToDoStatusNumberDto{

    private Integer beforeStart;
    private Integer inProgress;
    private Integer complete;
}
