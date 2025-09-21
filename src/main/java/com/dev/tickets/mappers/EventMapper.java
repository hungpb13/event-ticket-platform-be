package com.dev.tickets.mappers;

import com.dev.tickets.domain.dtos.CreateEventRequestDto;
import com.dev.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.dev.tickets.domain.entities.Event;
import com.dev.tickets.domain.requests.CreateEventRequest;
import com.dev.tickets.domain.requests.CreateTicketTypeRequest;
import com.dev.tickets.domain.responses.CreateEventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest toCreateTicketTypeRequest(CreateTicketTypeRequestDto dto);

    CreateEventRequest toCreateEventRequest(CreateEventRequestDto dto);

    CreateEventResponse toCreateEventResponse(Event event);
}
