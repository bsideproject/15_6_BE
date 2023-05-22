package bside.NotToDoClub.global.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultResponse<T> {
    private int status;
    private String message;
    private T response_data;

    public static <T> ResultResponse<T> of(ResponseCode resultCode, T data) {
        return new ResultResponse<>(resultCode, data);
    }

    public ResultResponse(ResponseCode resultCode, T data) {
        this.status = resultCode.getStatus().value();
        this.message = resultCode.getMessage();
        this.response_data = data;
    }
}
