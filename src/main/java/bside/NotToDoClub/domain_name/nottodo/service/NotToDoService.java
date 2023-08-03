package bside.NotToDoClub.domain_name.nottodo.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoUpdateRequestDto;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotToDoService {

    private final AuthTokenProvider authTokenProvider;
    private final UserNotToDoJpaRepository userNotToDoRepository;
    private final CheerUpMessageJpaRepository cheerUpMessageJpaRepository;
    private final UserJpaRepository userRepository;

    @Value("${app.auth.accessTokenSecret}")
    private String key;

    @Transactional
    public NotToDoCreateResponseDto createNotToDo(String accessToken, NotToDoCreateRequestDto notToDoCreateRequestDto){
        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        int cnt = userNotToDoRepository.countUserNotToDoByUserId(user.getId());
        if(cnt >= 7){
            throw new CustomException(ErrorCode.REGISTER_NOT_TO_DO_LIMIT);
        }

        Map<Integer, String> cheerUpMsgMap = new HashMap<>();
        cheerUpMsgMap.put(1, notToDoCreateRequestDto.getCheerUpMsg1());
        cheerUpMsgMap.put(2, notToDoCreateRequestDto.getCheerUpMsg2());
        cheerUpMsgMap.put(3, notToDoCreateRequestDto.getCheerUpMsg3());
        /*cheerUpMsgList.add(notToDoCreateRequestDto.getCheerUpMsg1());
        cheerUpMsgList.add(notToDoCreateRequestDto.getCheerUpMsg2());
        cheerUpMsgList.add(notToDoCreateRequestDto.getCheerUpMsg3());*/

        UserNotToDo newUserNotToDo = UserNotToDo.createUserNotToDo(notToDoCreateRequestDto, user, cheerUpMsgMap);

        UserNotToDo userNotToDo = userNotToDoRepository.save(newUserNotToDo);

        for (int i=1; i <= cheerUpMsgMap.size(); i++) {
            CheerUpMessage newCheerUpMessage = CheerUpMessage.builder()
                    .dspOrder(i)
                    .userNotToDo(userNotToDo)
                    .content(cheerUpMsgMap.get(i))
                    .registerUser(user)
                    .useYn(true)
                    .build();

            cheerUpMessageJpaRepository.save(newCheerUpMessage);
        }

        NotToDoCreateResponseDto notToDoCreateResponseDto = NotToDoCreateResponseDto.builder()
                .notToDoId(userNotToDo.getId())
                .userLoginId(userNotToDo.getUser().getLoginId())
                .notToDoText(userNotToDo.getNotToDoText())
                .goal(userNotToDo.getGoal())
                .progressState(userNotToDo.getProgressState().toString())
                .startDate(userNotToDo.getStartDate())
                .endDate(userNotToDo.getEndDate())
                .build();

        return notToDoCreateResponseDto;
    }

    public List<NotToDoListResponseDto> getNotToDoList(String accessToken, String orderBy){

        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        List<UserNotToDo> userNotToDoList = new ArrayList<>();

        if(orderBy.equals("in_close")){
            System.out.println("orderBy = " + orderBy);
            userNotToDoList = userNotToDoRepository.findByUserIdAndUseYnOrderByEndDate(userId).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_TO_DO_NOT_FOUND)
            );
        }
        else if (orderBy.equals("in_distant")){
            System.out.println("orderBy = " + orderBy);
            userNotToDoList = userNotToDoRepository.findByUserIdAndUseYnOrderByEndDateDesc(userId).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_TO_DO_NOT_FOUND)
            );
        }

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();

        List<NotToDoListResponseDto> notToDoListResponseDtoList = new ArrayList<>();
        for(UserNotToDo userNotToDo : userNotToDoList) {
            LocalDate startDate = LocalDate.parse(userNotToDo.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(userNotToDo.getEndDate(), formatter);

            String progressState = userNotToDo.getProgressState().toString();
            if(startDate.compareTo(now) <= 0 && endDate.compareTo(now) >= 0) progressState = "IN_PROGRESS";
            else if(endDate.compareTo(now) < 0) progressState = "COMPLETE";

            NotToDoListResponseDto notToDoListResponseDto = NotToDoListResponseDto.builder()
                    .notToDoId(userNotToDo.getId())
                    .notToDoText(userNotToDo.getNotToDoText())
                    .goal(userNotToDo.getGoal())
                    .progressState(progressState)
                    .startDate(userNotToDo.getStartDate())
                    .endDate(userNotToDo.getEndDate())
                    .useYn(booleanToYNConverter.convertToDatabaseColumn(userNotToDo.getUseYn()))
                    .build();

            notToDoListResponseDtoList.add(notToDoListResponseDto);
        }

        return notToDoListResponseDtoList;
    }

    @Transactional
    public Long updateUserNotToDo(String accessToken, Long id, NotToDoUpdateRequestDto notToDoUpdateRequestDto){
        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        UserNotToDo userNotToDo = userNotToDoRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_TO_DO_NOT_FOUND)
        );

        Map<Integer, String> cheerUpMsgMap = new HashMap<>();
        cheerUpMsgMap.put(1, notToDoUpdateRequestDto.getCheerUpMsg1());
        cheerUpMsgMap.put(2, notToDoUpdateRequestDto.getCheerUpMsg2());
        cheerUpMsgMap.put(3, notToDoUpdateRequestDto.getCheerUpMsg3());


        userNotToDo.updateUserNotToDo(notToDoUpdateRequestDto, cheerUpMsgMap);

        Long userNotToDoId = userNotToDo.getId();
        for (int i=1; i <= cheerUpMsgMap.size(); i++) {
            CheerUpMessage cheerUpMessage = cheerUpMessageJpaRepository.findByUserNotToDoIdAndDspOrder(userNotToDoId, i)
                    .orElseThrow(
                            () -> new CustomException(ErrorCode.CHEER_UP_MESSAGE_NOT_FOUND)
                    );

            cheerUpMessage.updateContent(cheerUpMsgMap.get(i));
        }

        return userNotToDoId;
    }


    @Transactional
    public int deleteUserNotToDo(String accessToken, Long id){
        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        UserNotToDo userNotToDo = userNotToDoRepository.findByIdAndUseYn(id).orElseThrow(
                () -> new CustomException(ErrorCode.ALREADY_USER_NOT_TO_DO_DELETE)
        );

        userNotToDo.updateUseYn();

        return 1;
    }
}
