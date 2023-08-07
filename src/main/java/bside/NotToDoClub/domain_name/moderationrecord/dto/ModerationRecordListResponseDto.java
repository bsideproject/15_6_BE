package bside.NotToDoClub.domain_name.moderationrecord.dto;


public interface ModerationRecordListResponseDto {
    Long getModerationId();
    Long getNotToDoId();
    String getNotToDoText();
    String getContent();
    String getRegDtm();
    String getRecordType();
    String getUseYn();
}
