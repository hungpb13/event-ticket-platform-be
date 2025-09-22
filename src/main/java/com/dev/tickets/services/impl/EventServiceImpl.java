package com.dev.tickets.services.impl;

import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.entities.TicketType;
import com.dev.tickets.domain.entities.User;
import com.dev.tickets.domain.enums.EventStatusEnum;
import com.dev.tickets.domain.requests.CreateEventRequest;
import com.dev.tickets.domain.requests.UpdateEventRequest;
import com.dev.tickets.domain.requests.UpdateTicketTypeRequest;
import com.dev.tickets.exceptions.EventNotFoundException;
import com.dev.tickets.exceptions.EventUpdateException;
import com.dev.tickets.exceptions.TicketTypeNotFoundException;
import com.dev.tickets.exceptions.UserNotFoundException;
import com.dev.tickets.repositories.EventRepository;
import com.dev.tickets.repositories.UserRepository;
import com.dev.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest request) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + organizerId));

        Event eventToCreate = new Event();

        List<TicketType> ticketTypesToCreate = request.getTicketTypes().stream()
                .map(ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }).toList();

        eventToCreate.setName(request.getName());
        eventToCreate.setStart(request.getStart());
        eventToCreate.setEnd(request.getEnd());
        eventToCreate.setVenue(request.getVenue());
        eventToCreate.setSalesStart(request.getSalesStart());
        eventToCreate.setSalesEnd(request.getSalesEnd());
        eventToCreate.setStatus(request.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> getEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventDetailsForOrganizer(UUID id, UUID organizerId) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Transactional
    @Override
    public Event updateEventForOrganizer(UUID id, UUID organizerId, UpdateEventRequest request) {
        if (null == request.getId()) {
            throw new EventUpdateException("Event ID cannot be null");
        }

        if (!id.equals(request.getId())) {
            throw new EventUpdateException("Cannot update ID of an event");
        }

        Event existingEvent = getEventDetailsForOrganizer(id, organizerId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));

        existingEvent.setName(request.getName());
        existingEvent.setStart(request.getStart());
        existingEvent.setEnd(request.getEnd());
        existingEvent.setVenue(request.getVenue());
        existingEvent.setSalesStart(request.getSalesStart());
        existingEvent.setSalesEnd(request.getSalesEnd());
        existingEvent.setStatus(request.getStatus());

        Set<UUID> requestTicketTypeIds = request.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes()
                .removeIf(existingTicketType ->
                        !requestTicketTypeIds.contains(existingTicketType.getId()));

        Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest ticketType : request.getTicketTypes()) {
            UUID ticketTypeId = ticketType.getId();
            if (null == ticketTypeId) {
                // CREATE
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);

                existingEvent.getTicketTypes().add(ticketTypeToCreate);
            } else if (existingTicketTypesIndex.containsKey(ticketTypeId)) {
                // UPDATE
                TicketType existingTicketType = existingTicketTypesIndex.get(ticketTypeId);
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setDescription(ticketType.getDescription());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
            } else {
                throw new TicketTypeNotFoundException("Ticket type not found with id: " + ticketTypeId);
            }
        }

        return eventRepository.save(existingEvent);
    }

    @Transactional
    @Override
    public void deleteEventForOrganizer(UUID id, UUID organizerId) {
        getEventDetailsForOrganizer(id, organizerId)
                .ifPresent(eventRepository::delete);
    }

    @Override
    public Page<Event> getPublishedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        return eventRepository.searchEvents(query, pageable);
    }
}
