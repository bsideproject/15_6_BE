package bside.NotToDoClub.domain_name.inquiry.controller;

import bside.NotToDoClub.domain_name.inquiry.dto.InquiryDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryRequestDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryResponseDto;
import bside.NotToDoClub.domain_name.inquiry.dto.InquiryUpdateRequestDto;
import bside.NotToDoClub.domain_name.inquiry.service.InquiryService;
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
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

    private final UserCommonService userCommonService;
    private final InquiryService inquiryService;
    private final ModelMapper mapper;

    @PostMapping("/create")
    public ResultResponse<InquiryResponseDto> createInquiry(
            @RequestHeader(value="access-token")String accessToken,
            @RequestBody @Valid InquiryRequestDto inquiryRequestDto
    ){
        InquiryDto inquiryDto = inquiryService.createInquiry(accessToken, inquiryRequestDto);

        InquiryResponseDto result = mapper.map(inquiryDto, InquiryResponseDto.class);

        return ResultResponse.of(ResponseCode.CREATE, result);
    }

    @PostMapping("/get/{inquiryId}")
    public ResultResponse<InquiryResponseDto> getInquiryInfo(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable("inquiryId") Long inquiryId
//            @RequestBody @Valid InquiryRequestDto inquiryRequestDto
    ){
        userCommonService.checkUserByToken(accessToken);
        InquiryDto inquiryDto = inquiryService.getInquiryInfo(inquiryId);
        InquiryResponseDto result = mapper.map(inquiryDto, InquiryResponseDto.class);

        return ResultResponse.of(ResponseCode.OK_, result);
    }

    @PostMapping("/update/{inquiryId}/contents")
    public ResultResponse<InquiryResponseDto> updateInquiryContents(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable("inquiryId") Long inquiryId,
            @RequestBody @Valid InquiryRequestDto inquiryRequestDto
    ){
        InquiryDto inquiryDto = inquiryService.updateContents(accessToken, inquiryId, inquiryRequestDto);
        InquiryResponseDto result = mapper.map(inquiryDto, InquiryResponseDto.class);

        return ResultResponse.of(ResponseCode.UPDATE, result);
    }

    @PostMapping("/delete/{inquiryId}")
    public ResultResponse<InquiryResponseDto> deleteInquiry(
            @RequestHeader(value="access-token")String accessToken,
            @PathVariable("inquiryId") Long inquiryId,
            @RequestBody @Valid InquiryRequestDto inquiryRequestDto
    ) {
        InquiryDto inquiryDto = inquiryService.deleteInquiry(inquiryId);
        InquiryResponseDto result = mapper.map(inquiryDto, InquiryResponseDto.class);

        return ResultResponse.of(ResponseCode.DELETE, result);
    }
}
