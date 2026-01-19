package com.gdg_team9.SafePlate.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class MemberRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRequest {
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        private String name;
    }
}