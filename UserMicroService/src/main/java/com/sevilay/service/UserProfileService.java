package com.sevilay.service;


import com.sevilay.dto.request.UpdateEmailOrUsernameRequestDto;
import com.sevilay.dto.request.UserCreateRequestDto;
import com.sevilay.dto.request.UserProfileUpdateRequestDto;
import com.sevilay.exception.ErrorType;
import com.sevilay.exception.UserServiceException;
import com.sevilay.manager.AuthManager;
import com.sevilay.mapper.UserMapper;
import com.sevilay.repository.UserRepository;
import com.sevilay.repository.entity.UserProfile;
import com.sevilay.utility.JwtTokenManager;
import com.sevilay.utility.ServiceManager;
import com.sevilay.utility.enums.Status;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile, Long> {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final AuthManager authManager;

    public UserProfileService(UserRepository userRepository, JwtTokenManager jwtTokenManager, AuthManager authManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
    }

    public Boolean createUser(UserCreateRequestDto dto) {
        try {
            save(UserMapper.INSTANCE.fromCreateRequestToUser(dto));
            return true;
        } catch (Exception e) {
            throw new UserServiceException(ErrorType.USER_NOT_CREATED);
        }
    }

    /**
     * Parametre olarak verdiğimiz authId yi, UserProfile dan gelecek olan authId ye eşit olan kişinin
     * Status unu değiştireceğiz
     *
     * @param authId
     * @return
     */
    public Boolean activateStatus(Long authId) {
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserServiceException(ErrorType.USER_NOT_FOUND);
        } else {
            userProfile.get().setStatus(Status.ACTIVE);
            update(userProfile.get());
            return true;
        }
    }


    public Boolean update(UserProfileUpdateRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserServiceException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserServiceException(ErrorType.USER_NOT_FOUND);
        }
        if (!dto.getUsername().equals(userProfile.get().getUsername()) || !dto.getEmail().equals(userProfile.get().getEmail())) {
            userProfile.get().setUsername(dto.getUsername());
            userProfile.get().setUsername(dto.getEmail());
            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = UpdateEmailOrUsernameRequestDto.builder()
                    .username(userProfile.get().getUsername())
                    .email(userProfile.get().getEmail())
                    .id(userProfile.get().getId())
                    .build();
            authManager.update(updateEmailOrUsernameRequestDto);
        }
        userProfile.get().setUsername(dto.getUsername());
        userProfile.get().setEmail(dto.getEmail());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatarUrl(dto.getAvatarUrl());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAbout(dto.getAbout());
        update(userProfile.get());
        return true;
    }


    public Boolean delete(Long authId) {
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserServiceException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(Status.DELETED);
        update(userProfile.get());
        return true;
    }

    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) { //DeneME1 -> deneme1, ->cacheleyecek
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile = userRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()) {
            throw new UserServiceException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "findbyrole", key = "#role.toUpperCase()") //bu cache'i register methodunda sıfırlayacağız
    public List<UserProfile> findByRole(String role) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        ResponseEntity<List<Long>> authIds = authManager.findByRole(role);
        List<Long> authIds = authManager.findByRole(role).getBody();
        /**
         * authManager dan bulduğumuz rollerin authId lerini getireceğiz
         * her bir authId için denk gelen UserProfile ı bulup listemize ekleyeceğiz
         * bunu streamlerle yapıp herbir authId yi bir UserProfile çevirip
         * collect(Collectors) diyip dışarıya UserProfile list döndüreceğiz
         * .orElseThrow(() buradaki boş parantez x,y,.. bütün paramatreler demek
         */
        return authIds.stream().map(x -> userRepository.findOptionalByAuthId(x)
                .orElseThrow(() -> {
                    throw new UserServiceException(ErrorType.USER_NOT_FOUND);
                })).collect(Collectors.toList());
    }
}
