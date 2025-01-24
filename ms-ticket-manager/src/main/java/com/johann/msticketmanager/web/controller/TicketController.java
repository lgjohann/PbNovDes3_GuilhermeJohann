package com.johann.msticketmanager.web.controller;

import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.service.TicketService;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/tickets")
@Tag(name = "Ingressos", description = "Contém os recursos para gerenciamento de ingressos")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(value = "/create-ticket")
    public ResponseEntity<TicketResponseDto> create(@RequestBody @Valid TicketCreateDto ticketCreateDto){
        Ticket ticket = ticketService.createTicket(TicketMapper.toTicket(ticketCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketMapper.toDto(ticket));
    }

}
