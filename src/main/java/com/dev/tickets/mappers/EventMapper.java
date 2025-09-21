package com.dev.tickets.mappers;

import com.dev.tickets.domain.dtos.CreateEventRequestDto;
import com.dev.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.entities.TicketType;
import com.dev.tickets.domain.requests.CreateEventRequest;
import com.dev.tickets.domain.requests.CreateTicketTypeRequest;
import com.dev.tickets.domain.responses.CreateEventResponse;
import com.dev.tickets.domain.responses.ListEventResponse;
import com.dev.tickets.domain.responses.ListTicketTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest toCreateTicketTypeRequest(CreateTicketTypeRequestDto dto);

    CreateEventRequest toCreateEventRequest(CreateEventRequestDto dto);

    CreateEventResponse toCreateEventResponse(Event event);

    ListTicketTypeResponse toListTicketTypeResponse(TicketType ticketType);

    ListEventResponse toListEventResponse(Event event);
}
