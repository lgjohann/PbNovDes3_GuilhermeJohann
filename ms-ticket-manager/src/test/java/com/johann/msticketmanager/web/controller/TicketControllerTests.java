package com.johann.msticketmanager.web.controller;

import com.johann.msticketmanager.exception.EventNotFound;
import com.johann.msticketmanager.service.TicketService;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTests {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @Test
    void create_WithValidInput_ReturnsCreated() {
        TicketCreateDto createDto = new TicketCreateDto();
        TicketResponseDto responseDto = new TicketResponseDto();
        when(ticketService.createTicket(any())).thenReturn(responseDto);

        ResponseEntity<TicketResponseDto> response = ticketController.create(createDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDto);
        verify(ticketService).createTicket(TicketMapper.toTicket(createDto));
    }

    @Test
    void getTicket_WithValidId_ReturnsOk() {
        BigInteger ticketId = BigInteger.valueOf(1);
        TicketResponseDto responseDto = new TicketResponseDto();
        when(ticketService.findTicketById(ticketId)).thenReturn(responseDto);

        ResponseEntity<TicketResponseDto> response = ticketController.getTicket(ticketId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
        verify(ticketService).findTicketById(ticketId);
    }

    @Test
    void updateTicket_WithValidInput_ReturnsOk() {
        BigInteger ticketId = BigInteger.valueOf(1);
        TicketCreateDto updateDto = new TicketCreateDto();
        TicketResponseDto responseDto = new TicketResponseDto();
        when(ticketService.updateTicket(eq(ticketId), any())).thenReturn(responseDto);

        ResponseEntity<TicketResponseDto> response = ticketController.updateTicket(ticketId, updateDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
        verify(ticketService).updateTicket(ticketId, updateDto);
    }

    @Test
    void getTicketByCpf_WithValidCpf_ReturnsOk() {
        String cpf = "12345678900";
        List<TicketResponseDto> responseDtos = Collections.singletonList(new TicketResponseDto());
        when(ticketService.findTicketByCpf(cpf)).thenReturn(responseDtos);

        ResponseEntity<List<TicketResponseDto>> response = ticketController.getTicketByCpf(cpf);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDtos);
        verify(ticketService).findTicketByCpf(cpf);
    }

    @Test
    void deleteTicket_WithValidId_ReturnsNoContent() {
        BigInteger ticketId = BigInteger.valueOf(1);

        ResponseEntity<Void> response = ticketController.deleteTicket(ticketId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(ticketService).deleteById(ticketId);
    }

    @Test
    void cancelTicket_WithValidCpf_ReturnsNoContent() {
        String cpf = "12345678900";

        ResponseEntity<Void> response = ticketController.cancelTicket(cpf);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(ticketService).deleteByCpf(cpf);
    }

    @Test
    void checkTicketsByEvent_WithValidEventId_ReturnsOk() {
        String eventId = "abc123";
        List<TicketResponseDto> responseDtos = Collections.singletonList(new TicketResponseDto());
        when(ticketService.findTicketsByEventId(eventId)).thenReturn(responseDtos);

        ResponseEntity<List<TicketResponseDto>> response = ticketController.checkTicketsByEvent(eventId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDtos);
        verify(ticketService).findTicketsByEventId(eventId);
    }

}
