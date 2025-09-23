package com.dev.tickets.mappers;

import com.dev.tickets.domain.entities.Ticket;
import com.dev.tickets.domain.entities.TicketType;
import com.dev.tickets.domain.responses.ListTicketResponse;
import com.dev.tickets.domain.responses.ListTicketTypeForTicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTypeForTicketResponse toListTicketTypeForTicketResponse(TicketType ticketType);

    ListTicketResponse toListTicketResponse(Ticket ticket);
}
