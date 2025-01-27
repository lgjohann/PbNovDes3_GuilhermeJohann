package com.johann.msticketmanager.service;

import com.johann.msticketmanager.clients.MsEventClient;
import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.exception.EventNotFound;
import com.johann.msticketmanager.repository.TicketRepository;
import com.johann.msticketmanager.web.dto.EventDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MsEventClient msEventClient;

    @Transactional
    public TicketResponseDto createTicket(Ticket ticket) {
        try {
            EventDto eventDto = EventDto.toEvent(msEventClient.findEventById(ticket.getEventId()));

            ticket.setEventId(eventDto.getEventId());
            ticket.setEventName(eventDto.getEventName());
            ticket.setStatus("sucesso"); // fazer isso aqui ser setado após o envio na fila do rabbit.

            ticketRepository.save(ticket);

            TicketResponseDto responseDto = TicketMapper.toDto(ticket);
            responseDto.setEvent(eventDto);

            return responseDto;
        } catch (FeignException.FeignClientException.NotFound e) {
            throw new EventNotFound("Event not found");
        }
    }
}
