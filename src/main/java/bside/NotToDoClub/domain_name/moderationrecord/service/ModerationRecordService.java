package bside.NotToDoClub.domain_name.moderationrecord.service;


import bside.NotToDoClub.config.AuthToken;
import bside.NotToDoClub.config.AuthTokenProvider;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordCreateRequestDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordCreateResponseDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordUpdateRequestDto;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.moderationrecord.repository.ModerationRecordJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateResponseDto;
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

@Service
@RequiredArgsConstructor
public class ModerationRecordService {

    private final AuthTokenProvider authTokenProvider;
    private final UserNotToDoJpaRepository userNotToDoRepository;
    private final UserJpaRepository userRepository;
    private final ModerationRecordJpaRepository moderationRecordJpaRepository;

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

        ModerationRecordCreateResponseDto moderationRecordCreateResponseDto = ModerationRecordCreateResponseDto.builder()
                .moderationId(moderationRecord.getId())
                .build();

        return moderationRecordCreateResponseDto;
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
    public long deleteModerationRecord (String accessToken, Long recordId) {
        AuthToken authToken = new AuthToken(accessToken, key);
        String email = authTokenProvider.getEmailByToken(authToken);

        UserEntity user = userRepository.findByLoginId(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        ModerationRecord moderationRecord = moderationRecordJpaRepository.findById(recordId).orElseThrow(
                () -> new CustomException(ErrorCode.MODERATION_RECORD_NOT_FOUND)
        );

        moderationRecord.updateUseYn();

        return recordId;
    }
}
