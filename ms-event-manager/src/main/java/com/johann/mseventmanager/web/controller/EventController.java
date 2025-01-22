package com.johann.mseventmanager.web.controller;

import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.service.EventService;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import com.johann.mseventmanager.web.dto.EventResponseDto;
import com.johann.mseventmanager.web.exception.ErrorMessage;
import com.johann.mseventmanager.web.mapper.EventMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/events")
@Tag(name = "Eventos", description = "Contém os recursos para gerenciamento de eventos")
public class EventController {

    private final EventService eventService;

    @Operation(
            summary = "Criar um novo evento.",
            description = "Recurso para criar um novo evento.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "O cep informado para criação do evento não foi encontrado na base de dados",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Não foi possível criar o recurso, pois os dados de entrada são inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<EventResponseDto> create(@RequestBody @Valid EventCreateDto eventCreateDto){
        Event event = eventService.save(EventMapper.toEvent(eventCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.toDto(event));
    }

    @Operation(
            summary = "Recuperar um evento por id.",
            description = "Recurso para recuperaer um evento através do id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "O evento com id informado não foi localizado",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<EventResponseDto> getById(@PathVariable String id){
        Event event = eventService.findById(id);
        return ResponseEntity.ok(EventMapper.toDto(event));
    }
}

