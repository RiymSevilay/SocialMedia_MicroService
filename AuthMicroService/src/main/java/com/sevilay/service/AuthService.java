package com.sevilay.service;

import com.sevilay.dto.request.*;
import com.sevilay.dto.response.RegisterResponseDto;
import com.sevilay.dto.response.RoleResponseDto;
import com.sevilay.exception.AuthServiceException;
import com.sevilay.exception.ErrorType;
import com.sevilay.manager.UserManager;
import com.sevilay.mapper.AuthMapper;
import com.sevilay.repository.AuthRepository;
import com.sevilay.repository.entity.Auth;
import com.sevilay.utility.CodeGenerator;
import com.sevilay.utility.JwtTokenManager;
import com.sevilay.utility.ServiceManager;
import com.sevilay.utility.enums.Role;
import com.sevilay.utility.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.management.relation.RoleList;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final AuthRepository authRepository;
    private final UserManager userManager;

    private JwtTokenManager jwtTokenManager;


    public AuthService(AuthRepository authRepository, UserManager userManager, JwtTokenManager jwtTokenManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    /**
     * @param dto
     * @return
     * @Transactional-> Methodta herhangi bir yerden exception dönüyorsa method içinde yapılan bütün değişiklikler
     * geri alınır (Rollback)
     * auth u kaydettikten sonra userManager in createUser methodunda bir sıkıntı yaşarsak auth ta kaydettiğimiz verileri
     * silmemiz gerekir, çünkü onları user a gönderememiş oluruz. delete(user) methodu yerine
     * @Transactional anatasyonu eklersek aynı işi yaptırmış oluruz.
     * eğer userMicroService ayakta değilse; auth ta yapılan değişikliler user a yansımaz
     */
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        try {
            userManager.createUser(AuthMapper.INSTANCE.fromAuthToUserCreateRequestDto(auth));
        } catch (Exception e) {
            //         delete(auth); -> Transactional
        }
        userManager.createUser(AuthMapper.INSTANCE.fromAuthToUserCreateRequestDto(auth));
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException((ErrorType.LOGIN_ERROR));
        }
        if (!optionalAuth.get().getStatus().equals(Status.ACTIVE)) {
            throw new AuthServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        String token = jwtTokenManager.createToken(optionalAuth.get().getId(), optionalAuth.get().getRole()).get();
//        if (token.isEmpty()) {
//            throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
//        }
//        return jwtTokenManager.createToken(optionalAuth.get().getId(), optionalAuth.get().getRole()).get();
        return jwtTokenManager.createToken(optionalAuth.get().getId(), optionalAuth.get().getRole())
                .orElseThrow(() -> {
                    throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    public Boolean activateStatus(ActivationRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findById(dto.getId());
        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        if (dto.getActivationCode().equals(optionalAuth.get().getActivationCode())) {
            optionalAuth.get().setStatus(Status.ACTIVE);
            update(optionalAuth.get());
            userManager.activateStatus(optionalAuth.get().getId());
            return true;
        } else {
            throw new AuthServiceException(ErrorType.ACTIVATION_CODE_ERROR);
        }
    }


    public Boolean updateEmailOrUsername(UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto) {
        Optional<Auth> auth = authRepository.findById(updateEmailOrUsernameRequestDto.getId());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setUsername(updateEmailOrUsernameRequestDto.getUsername());
        auth.get().setEmail(updateEmailOrUsernameRequestDto.getEmail());
        update(auth.get());
        return true;
    }


    public Boolean delete(Long id) {
        Optional<Auth> auth = authRepository.findById(id);
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(Status.DELETED);
        update(auth.get());
        userManager.delete(id);
        return true;
    }


    public Boolean deleteByToken(String token) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(token);
        Optional<Auth> auth = authRepository.findById(authId.get());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(Status.DELETED);
        update(auth.get());
        userManager.delete(authId.get());
        return true;

    }

    public List<RoleResponseDto> findByRole(String role) {
        try {
            Role roles = Role.valueOf(role.toUpperCase());
            return AuthMapper.INSTANCE.fromAuthToRoleResponses(authRepository.findAllOptionalByRole(roles));
        } catch (Exception e) {
         throw new AuthServiceException(ErrorType.ROLE_NOT_FOUND);
        }


    }
}
