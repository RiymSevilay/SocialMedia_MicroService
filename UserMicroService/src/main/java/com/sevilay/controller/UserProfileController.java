package com.sevilay.controller;

import com.sevilay.dto.request.UserCreateRequestDto;
import com.sevilay.dto.request.UserProfileUpdateRequestDto;
import com.sevilay.repository.entity.UserProfile;
import com.sevilay.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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

    @PostMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody UserProfileUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.update(dto));
    }


    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(@RequestParam Long authId) {
        return ResponseEntity.ok(userProfileService.delete(authId));
    }


    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }


    @GetMapping("/find_by_username")
    public ResponseEntity<UserProfile> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @GetMapping("find_by_role")
    public ResponseEntity<List<UserProfile>> findByRole(@RequestParam String role) {
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }
}
