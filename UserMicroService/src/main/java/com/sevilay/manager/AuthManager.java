package com.sevilay.manager;

import com.sevilay.dto.request.UpdateEmailOrUsernameRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:7070/aip/v1/auth", name = "userprofile-auth")
public interface AuthManager {

    @PutMapping("/update_email_or_username")
    public ResponseEntity<Boolean> update(@RequestBody UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto);

}
