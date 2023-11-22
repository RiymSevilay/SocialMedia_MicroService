package com.sevilay.manager;


import com.sevilay.dto.request.UserCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sevilay.constants.RestApi.*;


@FeignClient(url = "http://localhost:7071/api/v1/user", name = "auth-userprofile")
public interface UserManager {

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto);


    @GetMapping(ACTIVATESTATUS + "/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @GetMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(@RequestParam Long id);

}
