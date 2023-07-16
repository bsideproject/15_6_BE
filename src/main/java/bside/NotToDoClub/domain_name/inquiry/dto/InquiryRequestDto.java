package bside.NotToDoClub.domain_name.inquiry.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class InquiryRequestDto {

    @NotNull(message = "Title cannot be null")
    @Size(min = 2, max = 100,
            message = "Title not be less than 2 characters and more than 100 characters")
    private String title;
    @NotNull(message = "Content cannot be null")
    @Size(min = 2, max = 3000,
            message = "Content not be less than 2 characters and more than 3000 characters")
    private String content;

    @NotNull(message = "Reply email cannot be null")
    @Size(min = 2, message = "Reply email not be less than two characters")
    @Email
    private String replyEmail;

    @NotNull(message = "User login id cannot be null")
    @Size(min = 2, message = "User login id not be less than two characters")
    private String userLoginId;
}
