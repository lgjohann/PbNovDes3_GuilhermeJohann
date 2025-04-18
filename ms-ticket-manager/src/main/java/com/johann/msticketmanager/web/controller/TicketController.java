package com.johann.msticketmanager.web.controller;

import com.johann.msticketmanager.service.TicketService;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import com.johann.msticketmanager.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

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

    @Operation(
            summary = "Recuperar um ingresso por id.",
            description = "Recurso para recuperar um ingresso através do id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "O ingresso com id informado não foi localizado",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @GetMapping(value = "/get-ticket/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable BigInteger id){
        TicketResponseDto dto = ticketService.findTicketById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Atualizar um ingresso.",
            description = "Recurso para atualizar informações de um ingresso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Possiveis causas desse erro: <br>"+
                            "- O ID do ingresso informado não foi encontrado na base de dados"+
                            "- O ID do evento informado na alteração não foi encontrado na base de dados",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Não foi possível atualizar o recurso, pois os dados de entrada são inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping(value = "/update-ticket/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@PathVariable BigInteger id, @RequestBody @Valid TicketCreateDto ticketUpdate) {
        TicketResponseDto dto = ticketService.updateTicket(id ,ticketUpdate);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Recuperar ingressos por cpf.",
            description = "Recurso para recuperar uma lista de ingressos através do cpf do cliente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema( schema = @Schema(implementation = TicketResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Não foram encontrados ingressos vinculados ao CPF informado",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @GetMapping(value = "/get-ticket-by-cpf/{cpf}")
    public ResponseEntity<List<TicketResponseDto>> getTicketByCpf(@PathVariable String cpf){
        List<TicketResponseDto> dto = ticketService.findTicketByCpf(cpf);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Cancelar um ingresso por id.",
            description = "Recurso para cancelar um ingresso através do id.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "O ingresso com id informado não foi localizado",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "O ingresso com esse id já está cancelado",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @DeleteMapping(value = "/cancel-ticket/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable BigInteger id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Cancelar ingressos por cpf.",
            description = "Recurso para cancelar todos os ingressos através do id.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Não foram encontrados ingressos vinculados ao CPF informado",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Todos os ingressos vinculados a esse cpf já estão cancelados",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @DeleteMapping(value = "/cancel-ticket-by-cpf/{cpf}")
    public ResponseEntity<Void> cancelTicket(@PathVariable String cpf) {
        ticketService.deleteByCpf(cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Recuperar ingressos por eventId.",
            description = "Recurso para recuperar uma lista de ingressos através do id do evento vinculado ao ingresso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema( schema = @Schema(implementation = TicketResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Não foram encontrados ingressos vinculados ao evento informado",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @GetMapping(value = "/check-tickets-by-event/{eventId}")
    public ResponseEntity<List<TicketResponseDto>> checkTicketsByEvent(@PathVariable String eventId) {
        List<TicketResponseDto> dtoList = ticketService.findTicketsByEventId(eventId);
        return ResponseEntity.ok(dtoList);
    }
}
