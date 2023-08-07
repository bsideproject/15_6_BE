package bside.NotToDoClub.domain_name.moderationrecord.controller;

import bside.NotToDoClub.domain_name.moderationrecord.dto.*;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.moderationrecord.service.ModerationRecordService;
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
    @GetMapping("/list/fromDate/{fromDate}/toDate/{toDate}")
    public ResultResponse<List<ModerationRecordListResponseDto>> getModerationRecordList(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable(name = "fromDate") String fromDate,
            @PathVariable(name = "toDate") String toDate){
        List<ModerationRecordListResponseDto> moderationRecordList = moderationRecordService.getModerationRecordList(accessToken, fromDate, toDate);
        return  ResultResponse.of(ResponseCode.GET_MODERATION_RECORD, moderationRecordList);
    }

    /**
     * 절제기록 상세 조회
     * 성공/실패 여부, 기록한 시간 (hh:mm), 기록한 텍스트
     */
    @GetMapping("/{recordId}")
    public ResultResponse<ModerationRecord> getModerationRecord(@RequestHeader(value="access-token")String accessToken){

        return null;
    }

    /**
     * 절제기록 등록
     */
    @PostMapping("/ntdId/{notToDoId}")
    public ResultResponse<ModerationRecordCreateResponseDto> createModerationRecord(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable(name = "notToDoId") Long notToDoId,
            @RequestBody @Valid ModerationRecordCreateRequestDto moderationRecordCreateRequestDto){
        ModerationRecordCreateResponseDto moderationRecord = moderationRecordService.createModerationRecord(accessToken, notToDoId, moderationRecordCreateRequestDto);
        return ResultResponse.of(ResponseCode.CREATE_MODERATION_RECORD, moderationRecord);
    }

    /**
     * 절제기록 수정
     */
    @PutMapping("/{recordId}")
    public ResultResponse<ModerationRecordCreateResponseDto> updateModerationRecord(
            @RequestHeader(value = "access-token") String accessToken,
            @PathVariable(name = "recordId") Long recordId,
            @RequestBody @Valid ModerationRecordUpdateRequestDto moderationRecordUpdateRequestDto){
        ModerationRecordCreateResponseDto response = moderationRecordService.updateModerationRecord(accessToken, recordId, moderationRecordUpdateRequestDto);
        return ResultResponse.of(ResponseCode.UPDATE_MODERATION_RECORD, response);
    }

    /**
     * 절제기록 삭제
     */
    @DeleteMapping("/{recordId}")
    public ResultResponse<Integer> deleteModerationRecord(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable(name = "recordId") Long recordId){
        int result = moderationRecordService.deleteModerationRecord(accessToken, recordId);
        return ResultResponse.of(ResponseCode.DELETE_MODERATION_RECORD, result);
    }
}
