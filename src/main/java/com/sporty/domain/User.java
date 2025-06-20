package com.sporty.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
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
