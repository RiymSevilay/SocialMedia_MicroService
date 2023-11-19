package com.sevilay.service;

import com.sevilay.dto.request.*;
import com.sevilay.dto.response.RegisterResponseDto;
import com.sevilay.exception.AuthServiceException;
import com.sevilay.exception.ErrorType;
import com.sevilay.manager.UserManager;
import com.sevilay.mapper.AuthMapper;
import com.sevilay.repository.AuthRepository;
import com.sevilay.repository.entity.Auth;
import com.sevilay.utility.CodeGenerator;
import com.sevilay.utility.JwtTokenManager;
import com.sevilay.utility.ServiceManager;
import com.sevilay.utility.enums.Status;
import org.springframework.stereotype.Service;


import java.util.Optional;

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

    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
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




    public Boolean deletionStatus(Long id) {
        Optional<Auth> auth = authRepository.findById(id);
        if (auth.isEmpty()){
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(Status.DELETED);
        update(auth.get());
        DeletionRequestDto dto = DeletionRequestDto.builder()
                .authId(auth.get().getId())
                .build();
        userManager.deletionStatus(dto);
        return true;
    }


}
