package bside.NotToDoClub.domain_name.inquiry.service;

import bside.NotToDoClub.domain_name.inquiry.dto.InquiryDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryCreateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryUpdateRequestDto;

public interface InquiryService {

    InquiryDto createInquiry(String accessToken, InquiryCreateRequestDto inquiryCreateRequestDto);

    InquiryDto updateContents(String accessToken, InquiryUpdateRequestDto inquiryUpdateRequestDto);
}
