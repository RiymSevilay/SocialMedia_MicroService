package com.sevilay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")

public class FallbackController {
    // 2 service imiz oluğu için iki tane methodumuz olacak
    @GetMapping("/authservice")
    public ResponseEntity<String> authServiceFallback(){
        return ResponseEntity.ok("Auth service şu anda hizmet dışıdır.");
    }

    @GetMapping("/userservice")
    public ResponseEntity<String> userServiceFallback(){
        return ResponseEntity.ok("User service şu anda hizmet dışıdır");
    }

}
