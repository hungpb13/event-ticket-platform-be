package com.dev.tickets.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPublishedEventTicketTypeResponse {
    private String id;
    private String name;
    private Double price;
    private String description;
}
