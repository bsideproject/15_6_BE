package bside.NotToDoClub.domain_name.auth.service;

import bside.NotToDoClub.config.Constant;
import bside.NotToDoClub.domain_name.user.dto.AppleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.GoogleUserInfoDto;
import bside.NotToDoClub.domain_name.user.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface OauthService {

    String getRedirectUrl(Constant.SocialLoginType socialLoginType) throws IOException;
    GoogleUserInfoDto getGoogleUserInfo(String code) throws JsonProcessingException;
    KakaoUserInfoDto getKakaoUserInfo(String code) throws JsonProcessingException;
    AppleUserInfoDto getAppleUserInfo(String code) throws Exception;
}
