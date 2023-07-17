package bside.NotToDoClub.domain_name.nottodo.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import bside.NotToDoClub.domain_name.nottodo.repository.CheerUpMessageJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateResponseDto;
import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.BooleanToYNConverter;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotToDoService {

    private final AuthTokenProvider authTokenProvider;
    private final UserNotToDoJpaRepository userNotToDoRepository;
    private final UserJpaRepository userRepository;

    @Value("${app.auth.accessTokenSecret}")
    private String key;

    @Transactional
    public NotToDoCreateResponseDto createNotToDo(String accessToken, NotToDoCreateRequestDto notToDoCreateRequestDto){
        AuthToken authToken = new AuthToken(key, accessToken);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        List<String> cheerUpMsgList = new ArrayList<>();
        cheerUpMsgList.add(notToDoCreateRequestDto.getCheerUpMsg1());
        cheerUpMsgList.add(notToDoCreateRequestDto.getCheerUpMsg2());
        cheerUpMsgList.add(notToDoCreateRequestDto.getCheerUpMsg3());

        UserNotToDo newUserNotToDo = UserNotToDo.createUserNotToDo(notToDoCreateRequestDto, user, cheerUpMsgList);

        UserNotToDo userNotToDo = userNotToDoRepository.save(newUserNotToDo);

        NotToDoCreateResponseDto notToDoCreateResponseDto = NotToDoCreateResponseDto.builder()
                .notToDoId(userNotToDo.getId())
                .userLoginId(userNotToDo.getUser().getLoginId())
                .notToDoText(userNotToDo.getNotToDoText())
                .goal(userNotToDo.getGoal())
                .progressState(userNotToDo.getProgressState().toString())
                .startDate(userNotToDo.getStartDate())
                .endDate(userNotToDo.getEndDate())
                .build();


        System.out.println("notToDoCreateRequestDto = " + notToDoCreateRequestDto.getNotToDoText());

        return notToDoCreateResponseDto;
    }

    public List<NotToDoListResponseDto> getNotToDoList(String accessToken){

        AuthToken authToken = new AuthToken(key, accessToken);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        List<UserNotToDo> userNotToDoList = userNotToDoRepository.findAllByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_TO_DO_NOT_FOUND)
        );

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //String formatedNow = now.format(formatter);

        for(UserNotToDo userNotToDo : userNotToDoList){
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            LocalDate startDate = LocalDate.parse(userNotToDo.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(userNotToDo.getEndDate(), formatter);
            //if(startDate.compareTo(now) <= 0)
        }
        BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();
        List<NotToDoListResponseDto> notToDoListResponseDtoList = userNotToDoList.stream()
                .map(n -> NotToDoListResponseDto.builder()
                        .notToDoText(n.getNotToDoText())
                        .goal(n.getGoal())
                        .progressState(n.getProgressState())
                        .startDate(n.getStartDate())
                        .endDate(n.getEndDate())
                        .useYn(booleanToYNConverter.convertToDatabaseColumn(n.getUseYn()))
                        .build())
                .collect(Collectors.toList());

        return notToDoListResponseDtoList;
    }

    public void deleteUserNotToDo(String accessToken, Long id){
        userNotToDoRepository.deleteById(id);
    }
}
