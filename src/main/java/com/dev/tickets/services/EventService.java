package com.dev.tickets.services;

import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.requests.CreateEventRequest;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest request);
}
