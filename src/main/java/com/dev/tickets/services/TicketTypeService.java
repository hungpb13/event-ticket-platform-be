package com.dev.tickets.services;

import com.dev.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {

    Ticket purchaseTicket(UUID purchaserId, UUID ticketTypeId);
}
