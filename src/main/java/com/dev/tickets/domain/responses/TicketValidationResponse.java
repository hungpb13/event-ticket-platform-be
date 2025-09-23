package com.dev.tickets.domain.responses;

import com.dev.tickets.domain.enums.TicketValidationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidationResponse {
    private UUID ticketId;
    private TicketValidationStatusEnum status;
}
