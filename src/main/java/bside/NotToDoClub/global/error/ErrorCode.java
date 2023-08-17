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
    USER_NOT_TO_DO_NOT_FOUND(NOT_FOUND, "사용자의 낫투두 정보를 찾을 수 없습니다."),
    CHEER_UP_MESSAGE_NOT_FOUND(NOT_FOUND, "해당 낫투두 ID의 응원메시지를 찾을 수 없습니다."),
    ALREADY_USER_NOT_TO_DO_DELETE(NOT_FOUND, "삭제된 낫투두입니다."),
    INVALID_INQUIRY_ID(NOT_FOUND, "해당 문의사항을 찾을 수 없습니다."),
    USER_MODERATION_RECORD_NOT_FOUND(NOT_FOUND, "사용자의 절제기록을 찾을 수 없습니다."),
    MODERATION_RECORD_NOT_FOUND(NOT_FOUND, "해당 절제기록 ID의 절제기록을 찾을 수 없습니다."),

    BADGE_NOT_FOUND(NOT_FOUND, "해당 ID의 뱃지를 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL SERVER ERROR : 서버 내부 오류 */
    ALREADY_TOS_AGREE(INTERNAL_SERVER_ERROR,"이미 이용약관에 동의한 회원입니다."),
    REGISTER_NOT_TO_DO_LIMIT(INTERNAL_SERVER_ERROR, "등록한 낫투두 갯수가 7개를 초과했습니다."),
    DATETIME_FORMAT_PARSING_ERROR(INTERNAL_SERVER_ERROR, "날짜 형식 파싱오류"),
    BADGE_PRESENT_FAIL(INTERNAL_SERVER_ERROR, "사용자 뱃지 등록 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
