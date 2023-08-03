package bside.NotToDoClub.domain_name.nottodo.dto;

import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.global.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class NotToDoListDto {
    private long notToDoId;
    private String notToDoText;
    private String goal;
    private String progressState;
    private String startDate;
    private String endDate;
    private String useYn;
    List<CheerUpMessageNtdDto> cheerUpMessageList;

    public NotToDoListDto(UserNotToDo userNotToDo){

        BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();
        notToDoId = userNotToDo.getId();
        notToDoText = userNotToDo.getNotToDoText();
        goal = userNotToDo.getGoal();
        progressState = userNotToDo.getProgressState().toString();
        startDate = userNotToDo.getStartDate();
        endDate = userNotToDo.getEndDate();
        useYn = booleanToYNConverter.convertToDatabaseColumn(userNotToDo.getUseYn());
        cheerUpMessageList = userNotToDo.getCheerUpMessages().stream()
                .map(cheerUpMessage -> new CheerUpMessageNtdDto(cheerUpMessage))
                .collect(Collectors.toList());

    }

    public void changeProgressState(String state){
        this.progressState = state;
    }
}
