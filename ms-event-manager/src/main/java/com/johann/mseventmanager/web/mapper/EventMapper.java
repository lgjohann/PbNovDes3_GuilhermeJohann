package com.johann.mseventmanager.web.mapper;

import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.web.dto.EventCreateDto;
import com.johann.mseventmanager.web.dto.EventResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEvent(EventCreateDto dto) {
        return new ModelMapper().map(dto, Event.class);
    }

    public static EventResponseDto toDto(Event event) {
        return new ModelMapper().map(event, EventResponseDto.class);
    }
}
