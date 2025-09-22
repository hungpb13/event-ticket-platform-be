package com.dev.tickets.controllers;

import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.responses.GetPublishedEventDetailsResponse;
import com.dev.tickets.domain.responses.ListPublishedEventResponse;
import com.dev.tickets.mappers.EventMapper;
import com.dev.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponse>> getPublishedEvents(
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        Page<Event> events;

        if (null != q && !q.trim().isEmpty()) {
            events = eventService.searchPublishedEvents(q, pageable);
        } else {
            events = eventService.getPublishedEvents(pageable);
        }

        return ResponseEntity.ok(
                events.map(eventMapper::toListPublishedEventResponse)
        );
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetPublishedEventDetailsResponse> getPublishedEventDetails(
            @PathVariable("id") UUID id
    ) {
        return eventService.getPublishedEventDetails(id)
                .map(eventMapper::toGetPublishedEventDetailsResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
