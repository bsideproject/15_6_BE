package bside.NotToDoClub.domain_name.nottodo.controller;

import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateResponseDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListResponseDto;
import bside.NotToDoClub.domain_name.nottodo.service.NotToDoService;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "[#2 홈 메인 - 2]낫 투 두 리스트 조회", description = "#2 홈 메인 - " +
            "2번 홈 탭 케이스 구분\n" +
            "등록된 낫투두 없는 케이스\n" +
            "등록된 낫투두 있는 케이스\n" +
            "조회한 일자에 절제 기록 없는 케이스\n" +
            "주간 캘린더 선택\n" +
            "월간 캘린더 선택")
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
    @DeleteMapping("/")
    public void deleteNotToDo(){

    }
}
