package com.dev.tickets.controllers;

import com.dev.tickets.domain.entities.TicketValidation;
import com.dev.tickets.domain.enums.TicketValidationMethodEnum;
import com.dev.tickets.domain.requests.TicketValidationRequest;
import com.dev.tickets.domain.responses.TicketValidationResponse;
import com.dev.tickets.mappers.TicketValidationMapper;
import com.dev.tickets.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponse> validateTicket(
            @RequestBody TicketValidationRequest request
    ) {
        TicketValidationMethodEnum method = request.getMethod();
        TicketValidation ticketValidation;

        if (TicketValidationMethodEnum.MANUAL.equals(method)) {
            ticketValidation = ticketValidationService.validateTicketManually(request.getId());
        } else {
            ticketValidation = ticketValidationService.validateTicketByQrCode(request.getId());
        }

        return ResponseEntity.ok(
                ticketValidationMapper.toTicketValidationResponse(ticketValidation)
        );
    }
}
