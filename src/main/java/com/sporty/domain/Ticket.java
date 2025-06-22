package com.sporty.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Ticket {

    @Id
    @GeneratedValue
    @NotNull
    private UUID id;
    @NotBlank
    @Length(max = 255)
    private String subject;
    @NotBlank
    @Length(max = 2000)
    private String description;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @NotNull
    @CreatedBy
    private UUID userId;
    private UUID assigneeId;
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
