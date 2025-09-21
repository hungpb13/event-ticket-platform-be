package com.dev.tickets.services;

import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.requests.CreateEventRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest request);

    Page<Event> getEventsForOrganizer(UUID organizerId, Pageable pageable);
}
