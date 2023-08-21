package bside.NotToDoClub.domain_name.moderationrecord.service;


import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
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
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ModerationRecordService {

    private final AuthTokenProvider authTokenProvider;
    private final UserNotToDoJpaRepository userNotToDoRepository;
    private final UserJpaRepository userRepository;
    private final ModerationRecordJpaRepository moderationRecordJpaRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    private final UserCommonService userCommonService;
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

        UserNotToDo userNotToDo = userNotToDoRepository.findByIdAndUseYn(notToDoId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_TO_DO_NOT_FOUND)
        );

        ModerationRecord newModerationRecord =ModerationRecord.createModerationRecord(moderationRecordCreateRequestDto, userNotToDo);
        ModerationRecord moderationRecord = moderationRecordJpaRepository.save(newModerationRecord);

        // 첫번째 기록 뱃지
        int firstRecordBadgeCnt = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.FIRST_RECORD.toString());
        int cntAfterRegister = moderationRecordJpaRepository.countModerationRecordByUserIdAndUseYn(user.getId());
        if(firstRecordBadgeCnt == 0 && cntAfterRegister == 1){
            badgeService.presentBadge(BadgeList.FIRST_RECORD.toString(), user);
        }

        // 첫번째 인내 뱃지
        int firstPatienceBadgeCnt = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.FIRST_PATIENCE.toString());
        if(firstPatienceBadgeCnt == 0 && moderationRecordCreateRequestDto.getRecordType().equals("success")){
            badgeService.presentBadge(BadgeList.FIRST_PATIENCE.toString(), user);
        }

        // 실패해도 괜찮아 뱃지
        int firstFailBadgeCnt = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.FIRST_FAIL.toString());
        if(firstFailBadgeCnt == 0 && moderationRecordCreateRequestDto.getRecordType().equals("fail")){
            badgeService.presentBadge(BadgeList.FIRST_FAIL.toString(), user);
        }

        // 성실한 기록가 뱃지
        int diligentRecorderBadgeCnt = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.DILIGENT_RECORDER.toString());
        if(diligentRecorderBadgeCnt == 0 && cntAfterRegister == 10){
            badgeService.presentBadge(BadgeList.DILIGENT_RECORDER.toString(), user);
        }

        // 성실한 인고자 뱃지
        int diligentPatienceBadgeCnt = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.DILIGENT_PATIENCE.toString());
        int successRecordCnt = moderationRecordJpaRepository.countModerationRecordByRecordType(user.getId(), moderationRecordCreateRequestDto.getRecordType());
        if(diligentPatienceBadgeCnt == 0 && successRecordCnt == 10){
            badgeService.presentBadge(BadgeList.DILIGENT_PATIENCE.toString(), user);
        }

        // 대단한 기록가 뱃지
        int greatRecorderBadgeCnt = userBadgeJpaRepository.countUserBadgeByBadgeId(user.getId(), BadgeList.GREAT_RECORDER.toString());
        if (greatRecorderBadgeCnt == 0 && cntAfterRegister == 30){
            badgeService.presentBadge(BadgeList.GREAT_RECORDER.toString(), user);
        }
        // 성공은 실패의 어머니 뱃지추가
        this.checkFirstFailModerationRecord(accessToken, moderationRecordCreateRequestDto.getRecordType());

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
    public ModerationRecordCreateResponseDto updateModerationRecord (String accessToken, Long recordId,
                                                                     ModerationRecordUpdateRequestDto moderationRecordUpdateRequestDto) {
        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        ModerationRecord moderationRecord = moderationRecordJpaRepository.findById(recordId).orElseThrow(
                () -> new CustomException(ErrorCode.MODERATION_RECORD_NOT_FOUND)
        );

        moderationRecord.updateModerationRecord(moderationRecordUpdateRequestDto);
        
        // 성공은 실패의 어머니 뱃지추가
        this.checkFirstFailModerationRecord(accessToken, moderationRecordUpdateRequestDto.getRecordType());

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


    private void checkFirstFailModerationRecord(String accessToken,
                                                String moderationRecordType){
        if(moderationRecordType.equals("fail")){
            UserEntity userEntity = userCommonService.checkUserByToken(accessToken);
            List<ModerationRecord> moderationRecords = moderationRecordJpaRepository.findByUserAllRecords(userEntity.getId()).orElseThrow(() ->
                    new CustomException(ErrorCode.MODERATION_RECORD_NOT_FOUND));

            int count = 0;
            for (ModerationRecord moderationRecord : moderationRecords) {
                if(moderationRecord.getRecordType().equals("fail")){
                    ++count;
                    break;
                }
            }
            if(count > 0){
                badgeService.getFirstFailBadge(userEntity);
            }
        }

    }
}
