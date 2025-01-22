package com.johann.mseventmanager.web.controller;

import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.service.EventService;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import com.johann.mseventmanager.web.dto.EventResponseDto;
import com.johann.mseventmanager.web.mapper.EventMapper;
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
@RequestMapping(value = "api/v1/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> create(@RequestBody @Valid EventCreateDto eventCreateDto){
        Event event = eventService.save(EventMapper.toEvent(eventCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.toDto(event));
    }
}

