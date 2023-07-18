package bside.NotToDoClub.domain_name.inquiry.dto;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
public class InquiryDto {
    private Long id;
    private String title;
    private String content;
    private String replyEmail;
    private String userLoginId;
}
