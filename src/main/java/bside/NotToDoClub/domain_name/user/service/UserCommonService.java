package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service @Slf4j
@RequiredArgsConstructor
public class UserCommonService {

    private final UserJpaRepository userJpaRepository;
    private final AuthTokenProvider authTokenProvider;

    @Value("${app.auth.accessTokenSecret}")
    private String key;
    public UserEntity checkUserByToken(String accessToken) {

        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity userEntity = userJpaRepository.findByLoginId(email).orElseThrow(() -> {
                log.error("can not find {} user", email);
                return new CustomException(ErrorCode.USER_NOT_FOUND);
            }
        );

        return userEntity;
    }
}
