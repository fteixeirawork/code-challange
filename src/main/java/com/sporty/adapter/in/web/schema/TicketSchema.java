package com.sporty.adapter.in.web.schema;

import javax.validation.constraints.NotBlank;

public record TicketSchema(
        @NotBlank
        String subject,
        @NotBlank
        String description
) {
}
