package com.dev.tickets.controllers;

import com.dev.tickets.domain.dtos.CreateEventRequestDto;
import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.requests.CreateEventRequest;
import com.dev.tickets.domain.responses.CreateEventResponse;
import com.dev.tickets.domain.responses.ListEventResponse;
import com.dev.tickets.mappers.EventMapper;
import com.dev.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<CreateEventResponse> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto dto
    ) {
        CreateEventRequest createEventRequest = eventMapper.toCreateEventRequest(dto);
        UUID organizerId = getUserId(jwt);
        Event createdEvent = eventService.createEvent(organizerId, createEventRequest);
        return new ResponseEntity<>(
                eventMapper.toCreateEventResponse(createdEvent),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponse>> getEventsForOrganizer(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable

    ) {
        UUID organizerId = getUserId(jwt);
        Page<ListEventResponse> events = eventService.getEventsForOrganizer(organizerId, pageable)
                .map(eventMapper::toListEventResponse);
        return ResponseEntity.ok(events);
    }

    private static UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
