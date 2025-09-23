package com.dev.tickets.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTicketTypeForTicketResponse {
    private String id;
    private String name;
    private Double price;
}
