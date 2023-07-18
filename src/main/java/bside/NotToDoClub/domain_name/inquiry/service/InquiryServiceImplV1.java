package bside.NotToDoClub.domain_name.inquiry.service;

import bside.NotToDoClub.domain_name.inquiry.dto.InquiryDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryRequestDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryUpdateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.entity.Inquiry;
import bside.NotToDoClub.domain_name.inquiry.repository.InquiryJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service @Slf4j
@RequiredArgsConstructor
public class InquiryServiceImplV1 implements InquiryService{

    private final InquiryJpaRepository inquiryJpaRepository;
//    private final UserJpaRepository userJpaRepository;
    private final UserCommonService userCommonService;
    private final ModelMapper mapper;

    @Override
    public InquiryDto createInquiry(String accessToken, InquiryRequestDto inquiryRequestDto) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        Inquiry inquiry = Inquiry.builder()
                .title(inquiryRequestDto.getTitle())
                .content(inquiryRequestDto.getContent())
                .replyEmail(inquiryRequestDto.getReplyEmail())
                .user(userEntity)
                .build();

        Inquiry savedInquiry = inquiryJpaRepository.save(inquiry);

//        log.info("saved inquiry = {}", savedInquiry);

        InquiryDto inquiryDto = mapper.map(savedInquiry, InquiryDto.class);

        return inquiryDto;
    }

    @Override
    public InquiryDto updateContents(String accessToken, Long inquiryId, InquiryRequestDto inquiryRequestDto) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        Inquiry inquiry = inquiryJpaRepository.findById(inquiryId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INQUIRY_ID)
        );

        inquiry.setContent(inquiryRequestDto.getContent());

        Inquiry savedInquiry = inquiryJpaRepository.save(inquiry);
        InquiryDto result = mapper.map(savedInquiry, InquiryDto.class);

        return result;
    }

    @Override
    public InquiryDto getInquiryInfo(Long inquiryId) {
        Inquiry inquiry = inquiryJpaRepository.findById(inquiryId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INQUIRY_ID)
        );

        InquiryDto result = mapper.map(inquiry, InquiryDto.class);

        return result;
    }

    @Override
    public InquiryDto deleteInquiry(Long inquiryId) {
        Inquiry inquiry = inquiryJpaRepository.findById(inquiryId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INQUIRY_ID)
        );

        inquiryJpaRepository.deleteById(inquiryId);
        InquiryDto result = mapper.map(inquiry, InquiryDto.class);

        return result;
    }
}
