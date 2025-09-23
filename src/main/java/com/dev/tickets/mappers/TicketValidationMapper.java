package com.dev.tickets.mappers;

import com.dev.tickets.domain.entities.TicketValidation;
import com.dev.tickets.domain.responses.TicketValidationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    TicketValidationResponse toTicketValidationResponse(TicketValidation ticketValidation);
}
