package bside.NotToDoClub.domain_name.inquiry.service;

import bside.NotToDoClub.domain_name.inquiry.dto.InquiryDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryCreateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryUpdateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.entity.Inquiry;
import bside.NotToDoClub.domain_name.inquiry.repository.InquiryJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryServiceImplV1 implements InquiryService{

    private final InquiryJpaRepository inquiryJpaRepository;
//    private final UserJpaRepository userJpaRepository;
    private final UserCommonService userCommonService;
    private final ModelMapper mapper;

    @Override
    public InquiryDto createInquiry(String accessToken, InquiryCreateRequestDto inquiryCreateRequestDto) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        Inquiry inquiry = Inquiry.builder()
                .title(inquiryCreateRequestDto.getTitle())
                .content(inquiryCreateRequestDto.getTitle())
                .replyEmail(inquiryCreateRequestDto.getUserLoginId())
                .user(userEntity)
                .build();

        Inquiry savedInquiry = inquiryJpaRepository.save(inquiry);

        InquiryDto inquiryDto = mapper.map(savedInquiry, InquiryDto.class);

        return inquiryDto;
    }

    @Override
    public InquiryDto updateContents(String accessToken, InquiryUpdateRequestDto inquiryUpdateRequestDto) {
        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

        Inquiry inquiry = inquiryJpaRepository.findById(inquiryUpdateRequestDto.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INQUIRY_ID)
        );

        inquiry.setContent(inquiryUpdateRequestDto.getContent());

        Inquiry savedInquiry = inquiryJpaRepository.save(inquiry);
        InquiryDto result = mapper.map(savedInquiry, InquiryDto.class);

        return result;
    }
}
