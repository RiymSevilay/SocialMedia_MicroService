package com.sevilay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationRequestDto {

   // private String username;

    private Long id;

    private String activationCode;


}
