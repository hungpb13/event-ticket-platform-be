package com.dev.tickets.services.impl;

import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.entities.TicketType;
import com.dev.tickets.domain.entities.User;
import com.dev.tickets.domain.requests.CreateEventRequest;
import com.dev.tickets.exceptions.UserNotFoundException;
import com.dev.tickets.repositories.EventRepository;
import com.dev.tickets.repositories.UserRepository;
import com.dev.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
