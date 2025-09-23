package com.dev.tickets.services;

import com.dev.tickets.domain.entities.QrCode;
import com.dev.tickets.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
}
