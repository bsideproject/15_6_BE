package bside.NotToDoClub.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GET_USER_INFO(OK, "사용자 정보 조회 성공");

    private HttpStatus status;
    private final String message;
}
