package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.nottodo.dto.CheerUpMessageDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.dto.UserNotToDoStatusNumberDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService{

    private final UserJpaRepository userJpaRepository;
    private final UserNotToDoJpaRepository userNotToDoJpaRepository;
    private final ModelMapper mapper;
    private final UserCommonService userCommonService;

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
                .deleteByAccessToken(userEntity.getAccessToken()).orElseThrow(()->{
                    log.error("no {} user for delete", userEntity.getLoginId());
                    return new RuntimeException("해당 유저가 존재하지 않습니다.");
                });

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
        log.info(userEntity.getLoginId());

        UserEntity userEntity1 = userJpaRepository.findByLoginId(userEntity.getLoginId()).orElseThrow(() ->
                new RuntimeException());
        log.info(userEntity1.toString());

        UserEntity findUser = userJpaRepository.getUserCheerUpListByLoginId(userEntity.getLoginId()).orElseThrow(RuntimeException::new);

        List<CheerUpMessage> cheerUpMessages = findUser.getCheerUpMessages();

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

//        List<UserNotToDo> userNotToDoList = userEntity.getUserNotToDoList();

        UserEntity findUser = userJpaRepository.getUserNotToDoByLoginId(userEntity.getLoginId()).orElseThrow(() ->
                new RuntimeException());
        List<UserNotToDo> userNotToDoList = findUser.getUserNotToDoList();

        List<UserNotToDo> userNotToDosInProgress = userNotToDoList.stream()
                .filter(userNotToDo -> userNotToDo.getProgressState()
                        .equals(progressState))
                .collect(Collectors.toList());
        return userNotToDosInProgress;
    }

}
