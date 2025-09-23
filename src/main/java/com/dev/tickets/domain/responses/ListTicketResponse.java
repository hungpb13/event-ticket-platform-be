package com.dev.tickets.domain.responses;

import com.dev.tickets.domain.enums.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTicketResponse {
    private UUID id;
    private TicketStatusEnum status;
    private ListTicketTypeForTicketResponse ticketType;
}
