package bside.NotToDoClub.domain_name.nottodo.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoResponseDto;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotToDoService {

    private final AuthTokenProvider authTokenProvider;
    private final UserNotToDoJpaRepository userNotToDoRepository;

    @Value("${app.auth.accessTokenSecret}")
    private String key;

    public NotToDoResponseDto createNotToDo(String accessToken, NotToDoCreateRequestDto notToDoCreateRequestDto){
        AuthToken authToken = new AuthToken(key, accessToken);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserNotToDo newUserNotToDo = UserNotToDo.builder()
                .userLoginId(email)
                .notToDoText(notToDoCreateRequestDto.getNotToDoText())
                .goal(notToDoCreateRequestDto.getGoal())
                .successYn(false)
                .startDate(notToDoCreateRequestDto.getStartDate())
                .endDate(notToDoCreateRequestDto.getEndDate())
                .build();

        userNotToDoRepository.save(newUserNotToDo);

        System.out.println("notToDoCreateRequestDto = " + notToDoCreateRequestDto.getNotToDoText());

        return null;
    }
}
