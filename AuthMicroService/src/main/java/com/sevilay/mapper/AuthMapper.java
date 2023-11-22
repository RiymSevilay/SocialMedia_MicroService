package com.sevilay.mapper;

import com.sevilay.dto.request.RegisterRequestDto;
import com.sevilay.dto.request.UserCreateRequestDto;
import com.sevilay.dto.response.RegisterResponseDto;
import com.sevilay.dto.response.RoleResponseDto;
import com.sevilay.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    Auth fromRegisterRequestToAuth(RegisterRequestDto dto);

    RegisterResponseDto fromAuthToRegisterResponse(Auth auth);

    @Mapping(source = "id", target = "authId")
    UserCreateRequestDto fromAuthToUserCreateRequestDto(Auth auth);


}
