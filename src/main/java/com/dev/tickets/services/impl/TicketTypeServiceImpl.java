package com.dev.tickets.services.impl;

import com.dev.tickets.domain.entities.Ticket;
import com.dev.tickets.domain.entities.TicketType;
import com.dev.tickets.domain.entities.User;
import com.dev.tickets.domain.enums.TicketStatusEnum;
import com.dev.tickets.exceptions.TicketTypeNotFoundException;
import com.dev.tickets.exceptions.TicketsSoldOutException;
import com.dev.tickets.exceptions.UserNotFoundException;
import com.dev.tickets.repositories.TicketRepository;
import com.dev.tickets.repositories.TicketTypeRepository;
import com.dev.tickets.repositories.UserRepository;
import com.dev.tickets.services.QrCodeService;
import com.dev.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Transactional
    @Override
    public Ticket purchaseTicket(UUID purchaserId, UUID ticketTypeId) {
        User purchaser = userRepository.findById(purchaserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + purchaserId));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException("Ticket type not found with ID: " + ticketTypeId));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketTypeId);
        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchasedTickets + 1 > totalAvailable) {
            throw new TicketsSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(purchaser);

        Ticket savedTicket = ticketRepository.save(ticket);

        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
