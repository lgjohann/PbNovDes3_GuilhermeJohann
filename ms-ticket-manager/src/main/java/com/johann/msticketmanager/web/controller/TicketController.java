package com.johann.msticketmanager.web.controller;

import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.service.TicketService;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import com.johann.msticketmanager.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Criar um novo ingresso.",
            description = "Recurso para criar um novo ingresso.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "O id do evento informado para criação do ingresso não foi encontrado na base de dados",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Não foi possível criar o recurso, pois os dados de entrada são inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping(value = "/create-ticket")
    public ResponseEntity<TicketResponseDto> create(@RequestBody @Valid TicketCreateDto ticketCreateDto){
        TicketResponseDto ticket = ticketService.createTicket(TicketMapper.toTicket(ticketCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

}
