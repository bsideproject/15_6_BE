package bside.NotToDoClub.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    TOKEN_AUTHENTICATION_FAIL(UNAUTHORIZED, "토큰을 받아오는데 실패했습니다."),
    TOKEN_VALID_FAIL(UNAUTHORIZED, "토큰의 유효성 검사에 실패했습니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰의 유효기간이 만료되었습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    ALREADY_TOS_AGREE(INTERNAL_SERVER_ERROR,"이미 이용약관에 동의한 회원입니다.");


    private final HttpStatus status;
    private final String message;
}
