package com.dev.tickets.domain.requests;

import com.dev.tickets.domain.enums.TicketValidationMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidationRequest {
    private UUID id;
    private TicketValidationMethodEnum method;
}
