package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.nottodo.dto.CheerUpMessageDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.dto.UserNotToDoStatusNumberDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService{

    private final UserJpaRepository userJpaRepository;
    private final ModelMapper mapper;
//    private final AuthTokenProvider authTokenProvider;
    private final UserCommonService userCommonService;

//    @Value("${app.auth.accessTokenSecret}")
//    private String key;

    @Override
    public UserDto updateUserNickname(String accessToken, String nickname) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        userEntity.updateNickname(nickname);
        UserEntity updateUser = userJpaRepository.save(userEntity);

        UserDto userDto = mapper.map(updateUser, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto deleteUser(String accessToken) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        UserEntity deleteUser = userJpaRepository
                .deleteByAccessToken(userEntity.getAccessToken()).get();

        UserDto userDto = mapper.map(deleteUser, UserDto.class);

        return userDto;
    }

    @Override
    public List<NotToDoListResponseDto> findInProgressUserNotTodoList(String accessToken) {

        List<UserNotToDo> userNotToDosInProgress = getUserNotToDoListInStatus(accessToken, ProgressState.IN_PROGRESS);

        List<NotToDoListResponseDto> result = new ArrayList<>();
        userNotToDosInProgress.forEach(userNotToDo -> {
            result.add(mapper.map(userNotToDo, NotToDoListResponseDto.class));
        });

        return result;
    }

    @Override
    public List<CheerUpMessageDto> findCheerupList(String accessToken) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        List<CheerUpMessage> cheerUpMessages = userEntity.getCheerUpMessages();

        List<CheerUpMessageDto> result = new ArrayList<>();
        for(CheerUpMessage cheerUpMessage : cheerUpMessages){
            UserDto userDto = mapper.map(cheerUpMessage.getRegisterUser(), UserDto.class);
            CheerUpMessageDto cheerUpMessageDto = CheerUpMessageDto.builder()
                    .userDto(userDto)
                    .content(cheerUpMessage.getContent())
                    .userYn(cheerUpMessage.getUseYn())
                    .build();
            result.add(cheerUpMessageDto);
        }

        return result;
    }

    @Override
    public UserNotToDoStatusNumberDto getUserNotToDoStatus(String accessToken) {

        UserNotToDoStatusNumberDto result = UserNotToDoStatusNumberDto.builder()
                .beforeStart(getUserNotToDoListInStatus(accessToken, ProgressState.BEFORE_START).size())
                .inProgress(getUserNotToDoListInStatus(accessToken, ProgressState.IN_PROGRESS).size())
                .complete(getUserNotToDoListInStatus(accessToken, ProgressState.COMPLETE).size()).build();

        return result;
    }

    private List<UserNotToDo> getUserNotToDoListInStatus(String accessToken, ProgressState progressState){
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        List<UserNotToDo> userNotToDoList = userEntity.getUserNotToDoList();

        List<UserNotToDo> userNotToDosInProgress = userNotToDoList.stream()
                .filter(userNotToDo -> userNotToDo.getProgressState()
                        .equals(progressState))
                .collect(Collectors.toList());
        return userNotToDosInProgress;
    }

}
