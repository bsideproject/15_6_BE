package bside.NotToDoClub.domain_name.nottodo.controller;

import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoResponseDto;
import bside.NotToDoClub.domain_name.nottodo.service.NotToDoService;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public void getNotToDoList(){

    }

    /**
     * 낫투두 등록
     */
    @PostMapping("")
    public ResultResponse<NotToDoResponseDto> createNotToDo(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody @Valid NotToDoCreateRequestDto notToDoCreateRequestDto){
        NotToDoResponseDto notToDo = notToDoService.createNotToDo(accessToken, notToDoCreateRequestDto);
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
    @DeleteMapping("/")
    public void deleteNotToDo(){

    }
}
