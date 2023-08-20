package bside.NotToDoClub.domain_name.moderationrecord.service;


import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.domain_name.badge.service.BadgeList;
import bside.NotToDoClub.domain_name.badge.service.BadgeService;
import bside.NotToDoClub.domain_name.moderationrecord.dto.*;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.moderationrecord.repository.ModerationRecordJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModerationRecordService {

    private final AuthTokenProvider authTokenProvider;
    private final UserNotToDoJpaRepository userNotToDoRepository;
    private final UserJpaRepository userRepository;
    private final ModerationRecordJpaRepository moderationRecordJpaRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    private final BadgeService badgeService;

    @Value("${app.auth.accessTokenSecret}")
    private String key;

    @Transactional
    public ModerationRecordCreateResponseDto createModerationRecord (String accessToken, Long notToDoId, ModerationRecordCreateRequestDto moderationRecordCreateRequestDto) {
        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        UserNotToDo userNotToDo = userNotToDoRepository.findById(notToDoId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_TO_DO_NOT_FOUND)
        );

        ModerationRecord newModerationRecord =ModerationRecord.createModerationRecord(moderationRecordCreateRequestDto, userNotToDo);

        ModerationRecord moderationRecord = moderationRecordJpaRepository.save(newModerationRecord);

        // 첫번째 기록 뱃지
        int firstRecordBadge = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.FIRST_RECORD_BADGE);
        int cntAfterRegister = moderationRecordJpaRepository.countModerationRecordByUserIdAndUseYn(user.getId());
        if(firstRecordBadge == 0 && cntAfterRegister == 1){
            badgeService.presentBadge(BadgeList.FIRST_RECORD_BADGE.toString(), user);
        }

        // 성실한 기록가 뱃지
        int diligentRecorderBadge = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.DILIGENT_RECORDER_BADGE);
        if(diligentRecorderBadge == 0 && cntAfterRegister == 10){
            badgeService.presentBadge(BadgeList.DILIGENT_RECORDER_BADGE.toString(), user);
        }

        // 대단한 기록가 뱃지
        int greatRecorderBadge = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.GREAT_RECORDER_BADGE);
        if (greatRecorderBadge == 0 && cntAfterRegister == 30){
            badgeService.presentBadge(BadgeList.GREAT_RECORDER_BADGE.toString(), user);
        }

        ModerationRecordCreateResponseDto moderationRecordCreateResponseDto = ModerationRecordCreateResponseDto.builder()
                .moderationId(moderationRecord.getId())
                .build();

        return moderationRecordCreateResponseDto;
    }

    public List<ModerationRecordListResponseDto> getModerationRecordList (String accessToken, String fromDate, String toDate){

        //일자별 전체 절제기록 리스트 형태로 노출됨 (등록시간 내림차순=최신순)
        AuthToken authToken = new AuthToken(accessToken, key);
        Long userId = authTokenProvider.getUserIdByToken(authToken);

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        List<ModerationRecordListResponseDto> moderationRecords = moderationRecordJpaRepository.findByFromDateAndEndDate(userId, fromDate, toDate).orElseThrow(
                () -> new CustomException(ErrorCode.USER_MODERATION_RECORD_NOT_FOUND)
        );


        return  moderationRecords;
    }


    @Transactional
    public ModerationRecordCreateResponseDto updateModerationRecord (String accessToken, Long recordId, ModerationRecordUpdateRequestDto moderationRecordUpdateRequestDto) {
        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        ModerationRecord moderationRecord = moderationRecordJpaRepository.findById(recordId).orElseThrow(
                () -> new CustomException(ErrorCode.MODERATION_RECORD_NOT_FOUND)
        );

        moderationRecord.updateModerationRecord(moderationRecordUpdateRequestDto);

        ModerationRecordCreateResponseDto response = ModerationRecordCreateResponseDto.builder()
                .moderationId(recordId)
                .build();

        return response;
    }

    @Transactional
    public int deleteModerationRecord (String accessToken, Long recordId) {
        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        ModerationRecord moderationRecord = moderationRecordJpaRepository.findById(recordId).orElseThrow(
                () -> new CustomException(ErrorCode.MODERATION_RECORD_NOT_FOUND)
        );

        moderationRecord.updateUseYn();

        return 1;
    }
}
