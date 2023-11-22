package com.sevilay.controller;

import com.sevilay.dto.request.*;
import com.sevilay.dto.response.RegisterResponseDto;
import com.sevilay.dto.response.RoleResponseDto;
import com.sevilay.repository.entity.Auth;
import com.sevilay.service.AuthService;
import com.sevilay.utility.JwtTokenManager;
import com.sevilay.utility.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sevilay.constants.RestApi.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    /**
     * 21.11.2023 - redis
     * <p>
     * findByUsername methodu yazalım, bu methodu service'te cache'leyelim
     */

    private final AuthService authService;

    private final JwtTokenManager jwtTokenManager;

    private final CacheManager cacheManager; //anatasyonsuz rediste silme işlemi yapmak için -> redisDelete2

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivationRequestDto dto) {
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("/create_token")
    public ResponseEntity<String> createToken(Long id, Role role) {
        return ResponseEntity.ok(jwtTokenManager.createToken(id, role).get());
    }

    @GetMapping("/create_token2")
    public ResponseEntity<String> createToken2(Long id) {
        return ResponseEntity.ok(jwtTokenManager.createToken2(id).get());
    }

    @GetMapping("/get_id_from_token")
    public ResponseEntity<Long> getIdFromToken(String token) {
        return ResponseEntity.ok(jwtTokenManager.getIdFromToken(token).get());
    }

    @GetMapping("/get_role_from_token")
    public ResponseEntity<String> getRoleFromToken(String token) {
        return ResponseEntity.ok(jwtTokenManager.getRoleFromToken(token).get());
    }

    @PutMapping("/update_email_or_username")
    public ResponseEntity<Boolean> update(@RequestBody UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto) {
        return ResponseEntity.ok(authService.updateEmailOrUsername(updateEmailOrUsernameRequestDto));
    }


    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(Long id) {//@RequestParam
        return ResponseEntity.ok(authService.delete(id));
    }


    @DeleteMapping(DELETEBYTOKEN)
    public ResponseEntity<Boolean> deleteByToken(String token) {
        return ResponseEntity.ok(authService.deleteByToken(token));
    }

    /**
     * swagger da sayfayı inceleden redisin çalışma yapısını ve süresini inceleyebiliriz.
     *
     * @param value
     * @return
     */
    @GetMapping("/redis")
    @Cacheable(value = "redisexample") //@Cacheable -> Önbelleklenebilir
    public String redisExample(@RequestParam String value) {
        try {
            Thread.sleep(2000);
            return value;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample", allEntries = true)//@CacheEvict -> Önbellek tahliyesi bütün verileri siler
    public void redisDelete() {

    }

    @GetMapping("/redisdelete2")
    public Boolean redisDelete2() {
        try {
            cacheManager.getCache("redisexample").clear(); //"redisexample" etiketli bütün cache'leri temizler.
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Auth tan Role gireceğiz (User), bulduğumuz bütün bu USER ların authId lerini alıp userProfile a göndereceğiz.
     * UserProfile da findOptionalByAuthId methodunu kullanıp her bir authId denk gelen UserProfile ı bulacağız,
     * çünkü geriye UserProfile dönmek istiyoruz
     */
    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>> findByRole(@RequestParam String role) {
        return ResponseEntity.ok(authService.findByRole(role));
    }


}
