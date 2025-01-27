package com.johann.msticketmanager.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventDto {

    private String eventId;

    private String eventName;

    private LocalDateTime eventDateTime;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String uf;

    public static EventDto toEvent(EventClientResponseDto dto){
        EventDto eventDto = new EventDto();
        eventDto.setEventId(dto.getId());
        eventDto.setEventName(dto.getEventName());
        eventDto.setEventDateTime(dto.getDateTime());
        eventDto.setLogradouro(dto.getLogradouro());
        eventDto.setBairro(dto.getBairro());
        eventDto.setCidade(dto.getCidade());
        eventDto.setUf(dto.getUf());
        return eventDto;
    }

}
