package com.sporty.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User {
    @NotNull
    private UUID userId;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
