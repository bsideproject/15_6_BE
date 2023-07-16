package bside.NotToDoClub.domain_name.inquiry.controller;

import bside.NotToDoClub.domain_name.inquiry.dto.InquiryDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryCreateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryResponseDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryUpdateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.service.InquiryService;
import bside.NotToDoClub.global.response.ResponseCode;
import bside.NotToDoClub.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final ModelMapper mapper;

    @PostMapping("/create")
    public ResultResponse<InquiryResponseDto> createInquiry(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody @Valid InquiryCreateRequestDto inquiryCreateRequestDto
    ){
        InquiryDto inquiryDto = inquiryService.createInquiry(accessToken, inquiryCreateRequestDto);
        InquiryResponseDto result = mapper.map(inquiryDto, InquiryResponseDto.class);

        return ResultResponse.of(ResponseCode.CREATE, result);
    }

    @PostMapping("/update/contents")
    public ResultResponse<InquiryResponseDto> updateInquiryContents(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody @Valid InquiryUpdateRequestDto inquiryUpdateRequestDto
    ){
        InquiryDto inquiryDto = inquiryService.updateContents(accessToken, inquiryUpdateRequestDto);
        InquiryResponseDto result = mapper.map(inquiryDto, InquiryResponseDto.class);

        return ResultResponse.of(ResponseCode.UPDATE, result);
    }
}
