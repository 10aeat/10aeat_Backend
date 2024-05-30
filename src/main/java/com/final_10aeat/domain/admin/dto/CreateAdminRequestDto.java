package com.final_10aeat.domain.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequestDto(
    @NotBlank(message = "이메일은 필수 값입니다.")
    @Email
    String email,
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    String password
) {

}
