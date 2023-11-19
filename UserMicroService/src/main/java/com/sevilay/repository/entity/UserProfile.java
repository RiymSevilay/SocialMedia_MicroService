package com.sevilay.repository.entity;

import com.sevilay.utility.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserProfile extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authId;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private String address;

    private String about;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;





}
