package com.johann.mseventmanager.service;

import com.johann.mseventmanager.client.ViaCepClient;
import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.exception.CepNotFoundException;
import com.johann.mseventmanager.exception.EventNotFoundException;
import com.johann.mseventmanager.repository.EventRepository;
import com.johann.mseventmanager.web.dto.AddressResponseDto;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;

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
}
