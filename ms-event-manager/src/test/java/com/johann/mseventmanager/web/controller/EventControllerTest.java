package com.johann.mseventmanager.web.controller;

import static com.johann.mseventmanager.common.EventConstants.EVENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.johann.mseventmanager.common.EventConstants;
import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.exception.CepNotFoundException;
import com.johann.mseventmanager.exception.EventNotFoundException;
import com.johann.mseventmanager.service.EventService;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import com.johann.mseventmanager.web.dto.EventResponseDto;
import com.johann.mseventmanager.web.mapper.EventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @Test
    public void createEvent_WithValidData_ReturnsCreated() {
        EventCreateDto request = new EventCreateDto();
        request.setEventName("eventName");
        request.setDateTime(LocalDateTime.parse("2024-12-31T21:00:00"));
        request.setCep("01020-000");
        Event event = EventMapper.toEvent(request);

        when(eventService.save(any(Event.class))).thenReturn(EVENT);

        ResponseEntity<EventResponseDto> response = eventController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(EventMapper.toDto(EVENT));

        verify(eventService).save(event);
        assertThat(event.getEventName()).isEqualTo(request.getEventName());
    }

    @Test
    public void createEvent_WithInvalidCep_ReturnsNotFound() {
        EventCreateDto request = new EventCreateDto();
        request.setCep("invalid-cep");

        when(eventService.save(any(Event.class))).thenThrow(CepNotFoundException.class);

        assertThatThrownBy(() -> eventController.create(request)).isInstanceOf(CepNotFoundException.class);
    }

    @Test
    public void getEventById_WithValidId_ReturnsEvent() {
        when(eventService.findById(any())).thenReturn(EVENT);

        ResponseEntity<EventResponseDto> response = eventController.getById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(EventMapper.toDto(EVENT));
    }

    @Test
    public void getEventById_WithInvalidId_ThrowsNotFoundException() {
        String invalidId = "999";
        when(eventService.findById(invalidId)).thenThrow(EventNotFoundException.class);

        assertThatThrownBy(() -> eventController.getById(invalidId)).isInstanceOf(EventNotFoundException.class);
    }

    @Test
    public void getAllEvents_WithEvents_ReturnsEventsList() {
        List<Event> events = List.of(EVENT);
        when(eventService.findAll()).thenReturn(events);

        ResponseEntity<List<EventResponseDto>> response = eventController.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0)).isEqualTo(EventMapper.toDto(EVENT));
    }

    @Test
    public void getAllEvents_WithNoEvents_ThrowsNotFoundException() {
        when(eventService.findAll()).thenThrow(EventNotFoundException.class);

        assertThatThrownBy(() -> eventController.getAll()).isInstanceOf(EventNotFoundException.class);
    }

    @Test
    public void getAllSortedEvents_WithEvents_ReturnsSortedList() {
        List<Event> events = List.of(EVENT);
        when(eventService.findAllSorted()).thenReturn(events);

        ResponseEntity<List<EventResponseDto>> response = eventController.getAllSorted();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0)).isEqualTo(EventMapper.toDto(EVENT));
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEvent() {
        String eventId = "1";
        EventCreateDto request = new EventCreateDto();
        request.setEventName("UpdatedName");
        request.setDateTime(LocalDateTime.parse("2025-01-01T00:00:00"));
        request.setCep("02030-000");

        Event updatedEvent = EventConstants.EVENT;
        when(eventService.update(any(EventCreateDto.class), eq(eventId))).thenReturn(updatedEvent);

        ResponseEntity<EventResponseDto> response = eventController.update(request, eventId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(EventMapper.toDto(updatedEvent));
    }

    @Test
    public void deleteEvent_WithValidId_ReturnsNoContent() {
        doNothing().when(eventService).delete(any());

        ResponseEntity<Void> response = eventController.delete("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(eventService).delete("1");
    }

    @Test
    public void deleteEvent_WithInvalidId_ThrowsNotFoundException() {
        doThrow(new EventNotFoundException("Event not found")).when(eventService).delete("999");

        assertThatThrownBy(() -> eventController.delete("999")).isInstanceOf(EventNotFoundException.class);
    }
}
