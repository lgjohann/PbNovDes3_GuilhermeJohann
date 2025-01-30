package com.johann.mseventmanager.service;


import static com.johann.mseventmanager.common.EventConstants.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.johann.mseventmanager.client.TicketsClient;
import com.johann.mseventmanager.client.ViaCepClient;
import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.exception.CepNotFoundException;
import com.johann.mseventmanager.exception.EventDeleteException;
import com.johann.mseventmanager.exception.EventNotFoundException;
import com.johann.mseventmanager.repository.EventRepository;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import com.johann.mseventmanager.web.dto.clients.AddressResponseDto;
import com.johann.mseventmanager.web.dto.clients.TicketResponseDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private TicketsClient ticketsClient;

    @Test
    public void createEvent_WithValidData_ReturnsEvent() {
        when(eventRepository.save(EVENT)).thenReturn(EVENT);
        when(viaCepClient.getAddress(any())).thenReturn(ADDRESS_RESPONSE_DTO);

        Event sut = eventService.save(EVENT);

        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(EVENT.getId());
        assertThat(sut).isEqualTo(EVENT);
        verify(eventRepository).save(EVENT);
    }

    @Test
    public void createEvent_WithViaCepReturn_ReturnsError() {
        AddressResponseDto dto = new AddressResponseDto();
        dto.setCep(null);

        when(viaCepClient.getAddress(any())).thenReturn(dto);

        assertThatThrownBy( () -> eventService.save(EVENT) ).isInstanceOf(CepNotFoundException.class);
    }

    @Test
    public void findEventById_WithValidId_ReturnsEvent() {
        when(eventRepository.findById(any())).thenReturn(Optional.of(EVENT));

        Event sut = eventService.findById(EVENT.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(EVENT.getId());
        assertThat(sut).isEqualTo(EVENT);
        verify(eventRepository).findById(EVENT.getId());
    }

    @Test
    public void findEventById_WithUnexistingId_ReturnsEventNotFoundException() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy( () -> eventService.findById(EVENT.getId())).isInstanceOf(EventNotFoundException.class);
    }

    @Test
    public void findAll_WithEvents_ReturnsEventsList() {
        when(eventRepository.findAll()).thenReturn(List.of(EVENT));

        List<Event> sut = eventService.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).contains(EVENT);
        verify(eventRepository).findAll();
    }

    @Test
    public void findAll_WithNoEvents_ThrowsEventNotFoundException() {
        when(eventRepository.findAll()).thenReturn(emptyList());

        assertThatThrownBy(() -> eventService.findAll()).isInstanceOf(EventNotFoundException.class);
        verify(eventRepository).findAll();
    }

    @Test
    public void findAllSorted_WithEvents_ReturnsSortedList() {
        when(eventRepository.findAllByOrderByEventNameAsc()).thenReturn(List.of(EVENT));

        List<Event> sut = eventService.findAllSorted();

        assertThat(sut).isNotEmpty();
        assertThat(sut).contains(EVENT);
        verify(eventRepository).findAllByOrderByEventNameAsc();
    }

    @Test
    public void findAllSorted_WithNoEvents_ThrowsEventNotFoundException() {
        when(eventRepository.findAllByOrderByEventNameAsc()).thenReturn(emptyList());

        assertThatThrownBy(() -> eventService.findAllSorted()).isInstanceOf(EventNotFoundException.class);
        verify(eventRepository).findAllByOrderByEventNameAsc();
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEvent() {
        EventCreateDto updatedDto = new EventCreateDto();
        updatedDto.setEventName("UpdatedName");
        updatedDto.setDateTime(LocalDateTime.parse("2025-01-01T00:00:00"));
        updatedDto.setCep("02030-000");

        when(eventRepository.findById(EVENT.getId())).thenReturn(Optional.of(EVENT));
        when(viaCepClient.getAddress("02030000")).thenReturn(ADDRESS_RESPONSE_DTO);
        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        Event sut = eventService.update(updatedDto, EVENT.getId());

        assertThat(sut.getEventName()).isEqualTo(updatedDto.getEventName());
        assertThat(sut.getDateTime()).isEqualTo(updatedDto.getDateTime());
        assertThat(sut.getCep()).isEqualTo(ADDRESS_RESPONSE_DTO.getCep());
        verify(viaCepClient).getAddress("02030000");
        verify(eventRepository).save(EVENT);
    }

    @Test
    public void deleteEvent_WithNoExistingTickets_DeletesEvent() {
        when(eventRepository.existsById(EVENT.getId())).thenReturn(true);
        when(ticketsClient.checkTicketsByEventId(EVENT.getId())).thenReturn(ResponseEntity.of(Optional.ofNullable(any())));

        eventService.delete(EVENT.getId());

        verify(eventRepository).deleteById(EVENT.getId());
    }

    @Test
    public void deleteEvent_WithExistingTickets_ThrowsEventDeleteException() {
        when(eventRepository.existsById(EVENT.getId())).thenReturn(true);

        List<TicketResponseDto> ticketList = List.of(new TicketResponseDto());

        doReturn(ResponseEntity.ok(ticketList)).when(ticketsClient).checkTicketsByEventId(EVENT.getId());

        assertThatThrownBy(() -> eventService.delete(EVENT.getId())).isInstanceOf(EventDeleteException.class);

        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    public void deleteEvent_WhenEventDoesNotExist_ThrowsEventNotFoundException() {
        when(eventRepository.existsById(EVENT.getId())).thenReturn(false);

        assertThatThrownBy(() -> eventService.delete(EVENT.getId())).isInstanceOf(EventNotFoundException.class);
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    public void deleteEvent_WhenTicketsClientThrowsNotFound_DeletesEvent() throws FeignException {
        when(eventRepository.existsById(EVENT.getId())).thenReturn(true);
        when(ticketsClient.checkTicketsByEventId(EVENT.getId())).thenThrow(FeignException.NotFound.class);

        eventService.delete(EVENT.getId());

        verify(eventRepository).deleteById(EVENT.getId());
    }

}
