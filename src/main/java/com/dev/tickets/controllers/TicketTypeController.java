package com.dev.tickets.controllers;

import com.dev.tickets.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dev.tickets.util.JwtUtil.getUserId;

@RestController
@RequestMapping(path = "/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping(path = "/{ticketTypeId}/tickets")
    public ResponseEntity<Void> purchaseTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("ticketTypeId") UUID ticketTypeId
    ) {
        UUID purchaserId = getUserId(jwt);

        ticketTypeService.purchaseTicket(purchaserId, ticketTypeId);

        return ResponseEntity.noContent().build();
    }

}
