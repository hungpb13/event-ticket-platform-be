package com.dev.tickets.services.impl;

import com.dev.tickets.domain.entities.QrCode;
import com.dev.tickets.domain.entities.Ticket;
import com.dev.tickets.domain.entities.TicketValidation;
import com.dev.tickets.domain.enums.QrCodeStatusEnum;
import com.dev.tickets.domain.enums.TicketValidationMethodEnum;
import com.dev.tickets.domain.enums.TicketValidationStatusEnum;
import com.dev.tickets.exceptions.QrCodeNotFoundException;
import com.dev.tickets.exceptions.TicketNotFoundException;
import com.dev.tickets.repositories.QrCodeRepository;
import com.dev.tickets.repositories.TicketRepository;
import com.dev.tickets.repositories.TicketValidationRepository;
import com.dev.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final TicketValidationRepository ticketValidationRepository;
    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        "QR Code not found with id: " + qrCodeId
                ));

        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket, TicketValidationMethodEnum.QR_SCAN);
    }


    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(
                        "Ticket not found with ID: " + ticketId
                ));

        return validateTicket(ticket, TicketValidationMethodEnum.MANUAL);
    }

    private TicketValidation validateTicket(Ticket ticket, TicketValidationMethodEnum method) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(method);

        TicketValidationStatusEnum status = ticket.getValidations()
                .stream()
                .filter(validation ->
                        validation.getStatus().equals(TicketValidationStatusEnum.VALID)
                ).findFirst()
                .map(validation -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(status);

        return ticketValidationRepository.save(ticketValidation);
    }
}
