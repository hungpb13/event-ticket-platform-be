package com.dev.tickets.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTypeRequest {
    private String name;
    private Double price;
    private String description;
    private Integer totalAvailable;
}
