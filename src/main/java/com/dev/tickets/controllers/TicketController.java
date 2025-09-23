package com.dev.tickets.controllers;

import com.dev.tickets.domain.responses.GetTicketResponse;
import com.dev.tickets.domain.responses.ListTicketResponse;
import com.dev.tickets.mappers.TicketMapper;
import com.dev.tickets.services.QrCodeService;
import com.dev.tickets.services.TicketService;
import com.dev.tickets.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;

    @GetMapping
    public Page<ListTicketResponse> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
        UUID purchaserId = JwtUtil.getUserId(jwt);
        return ticketService.listTicketsForUser(purchaserId, pageable)
                .map(ticketMapper::toListTicketResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetTicketResponse> getTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID id
    ) {
        UUID userId = JwtUtil.getUserId(jwt);
        return ticketService.getTicketForUser(userId, id)
                .map(ticketMapper::toGetTicketResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/qr-codes")
    public ResponseEntity<byte[]> getTicketQrCode(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID id
    ) {
        UUID userId = JwtUtil.getUserId(jwt);
        byte[] qrCodeImage = qrCodeService.getQrCodeImageForUserAndTicket(userId, id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeImage.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCodeImage);
    }
}
