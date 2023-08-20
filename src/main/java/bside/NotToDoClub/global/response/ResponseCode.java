package bside.NotToDoClub.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlType;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GET_USER_INFO(OK, "사용자 정보 조회 성공"),
    PROVIDE_APP_TOKEN(OK, "토큰 발급 성공"),
    GET_USER_NOT_TO_DO(OK, "사용자 낫투두 조회 성공"),
    GET_MODERATION_RECORD(OK, "절제기록 조회 성공"),
    GET_USER_BADGE_LIST(OK, "사용자 뱃지 리스트 조회 성공"),

    TOS_AGREE(OK, "이용약관 동의 완료"),
    AUTO_LOGIN_AGREE(OK, "자동로그인 동의 완료"),

    CREATE_USER_NOT_TO_DO(CREATED, "낫투두 생성 완료"),
    UPDATE_USER_NOT_TO_DO(CREATED, "낫투두 수정 완료"),
    DELETE_USER_NOT_TO_DO(NO_CONTENT, "낫투두 삭제 완료"),

    CREATE_MODERATION_RECORD(CREATED, "절제기록 생성 완료"),
    UPDATE_MODERATION_RECORD(CREATED, "절제기록 수정 완료"),
    DELETE_MODERATION_RECORD(NO_CONTENT, "절제기록 삭제 완료"),

    CREATE(CREATED, "생성 완료"),

    UPDATE_USER(OK, "사용자 정보 변경 성공"),

    DELETE_USER(OK, "사용자 정보 변경 성공"),
    UPDATE(OK, "업데이트 성공"),
    DELETE(OK, "삭제 성공"),
    OK_(OK, "성공");

    private HttpStatus status;
    private final String message;
}
