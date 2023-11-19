package com.sevilay.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank
    @Size(min = 5, max = 20, message = "Kullanıcı adı en az 5, en fazla 20 karakterden oluşabilir.")
    private String username;

    @Email
    private String email;

    @Size(min = 5, max = 32, message = "Şifre en az 5, en fazla 32 karakterden oluşmalıdır.")
    private String password;

}
