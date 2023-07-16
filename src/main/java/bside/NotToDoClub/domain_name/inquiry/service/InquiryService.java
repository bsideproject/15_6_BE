package bside.NotToDoClub.domain_name.inquiry.service;

import bside.NotToDoClub.domain_name.inquiry.dto.InquiryDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryRequestDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryUpdateRequestDto;

public interface InquiryService {

    InquiryDto createInquiry(String accessToken, InquiryRequestDto inquiryRequestDto);
    InquiryDto updateContents(String accessToken, Long inquiryId, InquiryRequestDto inquiryRequestDto);
    InquiryDto getInquiryInfo(Long inquiryId);
    InquiryDto deleteInquiry(Long inquiryId);
}
