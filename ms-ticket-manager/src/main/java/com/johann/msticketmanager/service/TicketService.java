package com.johann.msticketmanager.service;

import com.johann.msticketmanager.clients.MsEventClient;
import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.exception.EventNotFound;
import com.johann.msticketmanager.exception.TicketNotFound;
import com.johann.msticketmanager.repository.TicketRepository;
import com.johann.msticketmanager.web.dto.EventDto;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

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

    @Transactional(readOnly = true)
    public TicketResponseDto findTicketById(BigInteger id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFound(String.format("Ticket with id %d not found", id)));
        EventDto eventDto = EventDto.toEvent(msEventClient.findEventById(ticket.getEventId()));
        TicketResponseDto responseDto = TicketMapper.toDto(ticket);
        responseDto.setEvent(eventDto);
        return responseDto;
    }

    @Transactional
    public TicketResponseDto updateTicket(BigInteger id, TicketCreateDto ticketUpdate) {
        try {

            Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFound(String.format("Ticket with id %d not found", id)));

            EventDto eventDto = EventDto.toEvent(msEventClient.findEventById(ticketUpdate.getEventId()));
            ticket.setEventId(ticketUpdate.getEventId());
            ticket.setEventName(ticketUpdate.getEventName());

            ticket.setCustomerName(ticketUpdate.getCustomerName());
            ticket.setCpf(ticketUpdate.getCpf());
            ticket.setCustomerMail(ticketUpdate.getCustomerMail());
            ticket.setBrlAmount(ticketUpdate.getBrlAmount());
            ticket.setUsdAmount(ticketUpdate.getUsdAmount());
            ticketRepository.save(ticket);

            TicketResponseDto responseDto = TicketMapper.toDto(ticket);
            responseDto.setEvent(eventDto);
            return responseDto;
        } catch (FeignException.FeignClientException.NotFound e) {
            throw new EventNotFound("Event not found");
        }
   }
}
