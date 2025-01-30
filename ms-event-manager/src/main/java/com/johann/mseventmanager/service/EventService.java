package com.johann.mseventmanager.service;

import com.johann.mseventmanager.client.TicketsClient;
import com.johann.mseventmanager.client.ViaCepClient;
import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.exception.CepNotFoundException;
import com.johann.mseventmanager.exception.EventDeleteException;
import com.johann.mseventmanager.exception.EventNotFoundException;
import com.johann.mseventmanager.repository.EventRepository;
import com.johann.mseventmanager.web.dto.clients.AddressResponseDto;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import com.johann.mseventmanager.web.dto.clients.TicketResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;
    private final TicketsClient ticketsClient;

    @Transactional
    public Event save(Event event) {

        AddressResponseDto address = viaCepClient.getAddress(event.getCep().replace("-",""));
        if (address.getCep() == null) {
            throw new CepNotFoundException("cep not found in the database");
        }

        event.setCep(address.getCep());
        event.setLogradouro(address.getLogradouro());
        event.setBairro(address.getBairro());
        event.setCidade(address.getLocalidade());
        event.setUf(address.getUf());

        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Event findById(String id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException("event not found in the database")
        );
    }

    @Transactional(readOnly = true)
    public List<Event> findAll() {
        List<Event> list = eventRepository.findAll();
        if (list.isEmpty()) {
            throw new EventNotFoundException("No events were found in the database");
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<Event> findAllSorted() {
        List<Event> list = eventRepository.findAllByOrderByEventNameAsc();
        if (list.isEmpty()) {
            throw new EventNotFoundException("No events were found in the database");
        }
        return list;
    }

    @Transactional
    public Event update(EventCreateDto updatedEvent, String id) {
        Event event = findById(id);

        event.setEventName(updatedEvent.getEventName());
        event.setDateTime(updatedEvent.getDateTime());
        event.setCep(updatedEvent.getCep());

        return save(event);
    }

    @Transactional
    public void delete(String id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException("event not found in the database");
        }

        try {
            ResponseEntity<?> response = ticketsClient.checkTicketsByEventId(id);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<TicketResponseDto> tickets = (List<TicketResponseDto>) response.getBody();
                if (!tickets.isEmpty()) {
                    throw new EventDeleteException("Tickets have already been sold for this event");
                }
            }

        } catch (FeignException.NotFound e) {
            System.out.println("No tickets found.");
        }
        eventRepository.deleteById(id);
    }
}
