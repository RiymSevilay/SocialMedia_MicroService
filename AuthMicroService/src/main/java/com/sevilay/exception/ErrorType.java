package com.sevilay.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType {

    INTERNAL_ERROR(5100,"Sunucu Hatası!", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre Hatası!", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4110,"Girdiğiniz kullanıcı adı veya şifre hatalıdır.", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4111,"Girdiğiniz kullanıcı adı kullanılmaktadır." ,HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4112,"Kullanıcı bulunamadı." , HttpStatus.BAD_REQUEST ),
    ACTIVATION_CODE_ERROR(4113,"Aktivasyon kodu hatalıdır." , HttpStatus.BAD_REQUEST ),
    INVALID_TOKEN(4114,"Geçersiz token!" ,HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4115,"Token oluşturulamadı." ,HttpStatus.BAD_REQUEST ),
    ACCOUNT_NOT_ACTIVE(4116,"Hesabınız aktif edilmemiştir. Lütfen hesabınızı aktif hale getiriniz." , HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(4117,"Giridiğiniz türde bir rol bulunmamaktadır", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
