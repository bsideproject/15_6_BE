package bside.NotToDoClub.domain_name.moderationrecord.controller;

import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordCreateRequestDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordCreateResponseDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordResponseDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordUpdateRequestDto;
import bside.NotToDoClub.domain_name.moderationrecord.service.ModerationRecordService;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateResponseDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoListCUMsgResponseDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoUpdateRequestDto;
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
@RequestMapping("/moderationRecord")
public class ModerationRecordController {

    private final ModerationRecordService moderationRecordService;

    /**
     * 절제기록 리스트 조회
     */
    @GetMapping("/list")
    public ResultResponse<List<ModerationRecordResponseDto>> getModerationRecord(@RequestHeader(value="access-token")String accessToken){

        return  null;
    }

    /**
     * 절제기록 등록
     */
    @PostMapping("/ntdId/{notToDoId}")
    public ResultResponse<ModerationRecordCreateResponseDto> createModerationRecord(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody @Valid ModerationRecordCreateRequestDto moderationRecordCreateRequestDto){
        ModerationRecordCreateResponseDto notToDo = moderationRecordService.createModerationRecord(accessToken, moderationRecordCreateRequestDto);
        return ResultResponse.of(ResponseCode.CREATE_USER_NOT_TO_DO, notToDo);
    }

    /**
     * 절제기록 수정
     */
    @PutMapping("/{recordId}")
    public ResultResponse<Long> updateModerationRecord(
            @RequestHeader(value = "access-token") String accessToken,
            @PathVariable(name = "recordId") Long notToDoId,
            @RequestBody @Valid ModerationRecordUpdateRequestDto ModerationRecordUpdateRequestDto){

        return null;
    }

    /**
     * 절제기록 삭제
     */
    @DeleteMapping("/{recordId}")
    public ResultResponse<Integer> deleteModerationRecord(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable(name = "recordId") Long id){

        return null;
    }
}
