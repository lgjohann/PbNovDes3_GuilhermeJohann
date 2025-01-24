package com.johann.msticketmanager.service;

import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus("sucesso"); // fazer isso aqui ser setado após o envio na fila do rabbit.
        return ticketRepository.save(ticket);
    }
}
