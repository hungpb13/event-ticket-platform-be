package com.dev.tickets.mappers;

import com.dev.tickets.domain.entities.Ticket;
import com.dev.tickets.domain.entities.TicketType;
import com.dev.tickets.domain.responses.GetTicketResponse;
import com.dev.tickets.domain.responses.ListTicketResponse;
import com.dev.tickets.domain.responses.ListTicketTypeForTicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTypeForTicketResponse toListTicketTypeForTicketResponse(TicketType ticketType);

    ListTicketResponse toListTicketResponse(Ticket ticket);

    @Mapping(target = "price", source = "ticket.ticketType.price")
    @Mapping(target = "description", source = "ticket.ticketType.description")
    @Mapping(target = "eventName", source = "ticket.ticketType.event.name")
    @Mapping(target = "eventVenue", source = "ticket.ticketType.event.venue")
    @Mapping(target = "eventStart", source = "ticket.ticketType.event.start")
    @Mapping(target = "eventEnd", source = "ticket.ticketType.event.end")
    GetTicketResponse toGetTicketResponse(Ticket ticket);
}
