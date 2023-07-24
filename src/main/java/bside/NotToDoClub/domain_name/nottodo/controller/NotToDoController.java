package bside.NotToDoClub.domain_name.nottodo.controller;

import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateResponseDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.nottodo.service.NotToDoService;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/nottodo")
public class NotToDoController {

    private final NotToDoService notToDoService;

    /**
     * 낫투두 리스트 조회
     */
    @GetMapping("/list")
    public ResultResponse<List<NotToDoListResponseDto>> getNotToDoList(@RequestHeader(value="access-token")String accessToken, @RequestParam(required = false, defaultValue = "in_close") String orderBy){
        List<NotToDoListResponseDto> notToDoListResponseDto = notToDoService.getNotToDoList(accessToken, orderBy);
        return ResultResponse.of(ResponseCode.GET_USER_NOT_TO_DO, notToDoListResponseDto);
    }

    /**
     * 낫투두 등록
     */
    @PostMapping("")
    public ResultResponse<NotToDoCreateResponseDto> createNotToDo(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody @Valid NotToDoCreateRequestDto notToDoCreateRequestDto){
        NotToDoCreateResponseDto notToDo = notToDoService.createNotToDo(accessToken, notToDoCreateRequestDto);
        return ResultResponse.of(ResponseCode.CREATE_USER_NOT_TO_DO, notToDo);
    }

    /**
     * 낫투두 수정
     */
    @PutMapping("/")
    public void updateNotToDo(){

    }

    /**
     * 낫투두 삭제
     */
    @DeleteMapping("/{notToDoId}")
    public ResultResponse<Integer> deleteNotToDo(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable(name = "notToDoId") Long id){
        int result = notToDoService.deleteUserNotToDo(accessToken, id);

        return ResultResponse.of(ResponseCode.DELETE_USER_NOT_TO_DO, result);
    }
}
