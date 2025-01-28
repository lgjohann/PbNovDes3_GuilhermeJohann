package com.johann.msticketmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.johann.msticketmanager.clients.MsEventClient;
import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.exception.EventNotFound;
import com.johann.msticketmanager.exception.TicketDeleteException;
import com.johann.msticketmanager.exception.TicketNotFound;
import com.johann.msticketmanager.mqueue.NotificationTicket;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MsEventClient msEventClient;
    private final NotificationTicket notificationTicket;

    @Transactional
    public TicketResponseDto createTicket(Ticket ticket) {
        try {
            EventDto eventDto = EventDto.toEvent(msEventClient.findEventById(ticket.getEventId()));

            ticket.setEventId(eventDto.getEventId());
            ticket.setEventName(eventDto.getEventName());

            ticket.setStatus("sucesso");
            ticketRepository.save(ticket);
            notificationTicket.publishTicketCreated(ticket);

            TicketResponseDto responseDto = TicketMapper.toDto(ticket);
            responseDto.setEvent(eventDto);

            return responseDto;
        } catch (FeignException.FeignClientException.NotFound e) {
            throw new EventNotFound("Event not found");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

   @Transactional(readOnly = true)
    public List<TicketResponseDto> findTicketByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findAllByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new TicketNotFound(String.format("No tickets were found for this cpf: %s", cpf));
       }


        List<TicketResponseDto> dtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            EventDto eventDto = EventDto.toEvent(msEventClient.findEventById(ticket.getEventId()));
            dtoList.add(TicketMapper.toDto(ticket, eventDto));
        }

        return dtoList;
    }

    @Transactional
    public void deleteById(BigInteger id) {
        if(!ticketRepository.existsById(id)) {
            throw new TicketNotFound(String.format("Ticket with id %d not found", id));
        }
        if(!ticketRepository.findById(id).get().getStatus().equals("sucesso")) {
            throw new TicketDeleteException(String.format("Ticket with id %d is already deleted", id));
        }
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setStatus("cancelado");
        ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findAllByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new TicketNotFound(String.format("No tickets were found for this cpf: %s", cpf));
        }
        checkIfAllCanceled(cpf, tickets);

        for (Ticket ticket : tickets) {
            ticket.setStatus("cancelado");
            ticketRepository.save(ticket);
        }
    }

    private void checkIfAllCanceled(String cpf, List<Ticket> tickets) {
        boolean allCanceled = true;
        for (Ticket ticket : tickets) {
            if (!ticket.getStatus().equals("cancelado")) {
                allCanceled = false;
                break;
            }
        }
        if (allCanceled) {
            throw new TicketDeleteException(String.format("Tickets for the cpf %s is already all canceled", cpf));
        }
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDto> findTicketsByEventId(String eventId) {
        List<Ticket> tickets = ticketRepository.findAllByEventId(eventId);
        if (tickets.isEmpty()) {
            throw new TicketNotFound(String.format("No tickets were found for this event: %s", eventId));
        }

        EventDto eventDto = EventDto.toEvent(msEventClient.findEventById(tickets.get(0).getEventId()));

        List<TicketResponseDto> dtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            dtoList.add(TicketMapper.toDto(ticket, eventDto));
        }

        return dtoList;
    }
}
