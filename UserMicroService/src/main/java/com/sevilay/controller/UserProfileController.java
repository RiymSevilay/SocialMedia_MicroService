package com.sevilay.controller;

import com.sevilay.dto.request.DeletionRequestDto;
import com.sevilay.dto.request.UserCreateRequestDto;
import com.sevilay.dto.request.UserProfileUpdateRequestDto;
import com.sevilay.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.sevilay.constants.RestApi.*;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto) {
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @GetMapping(ACTIVATESTATUS + "/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @GetMapping(DELETEBYID)
    public ResponseEntity<Boolean> deletionStatus(@RequestBody DeletionRequestDto dto){
        return ResponseEntity.ok(userProfileService.deletionStatus(dto));
    }

}
