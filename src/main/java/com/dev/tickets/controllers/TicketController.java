package com.dev.tickets.controllers;

import com.dev.tickets.domain.responses.ListTicketResponse;
import com.dev.tickets.mappers.TicketMapper;
import com.dev.tickets.services.TicketService;
import com.dev.tickets.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @GetMapping
    public Page<ListTicketResponse> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
        UUID purchaserId = JwtUtil.getUserId(jwt);
        return ticketService.listTicketsForUser(purchaserId, pageable)
                .map(ticketMapper::toListTicketResponse);
    }
}
