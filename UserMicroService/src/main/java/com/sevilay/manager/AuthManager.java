package com.sevilay.manager;

import com.sevilay.dto.request.UpdateEmailOrUsernameRequestDto;
import com.sevilay.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "http://localhost:7070/api/v1/auth", name = "userprofile-auth")
public interface AuthManager {

    @PutMapping("/update_email_or_username")
    public ResponseEntity<Boolean> update(@RequestBody UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto);

    @GetMapping("find_by_role")
    public ResponseEntity<List<UserProfile>> findByRole(@RequestParam String role);

    }
