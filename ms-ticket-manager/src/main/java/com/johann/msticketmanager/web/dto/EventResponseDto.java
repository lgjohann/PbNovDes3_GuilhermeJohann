package com.johann.msticketmanager.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"eventId", "eventName", "eventDateTime", "logradouro", "bairro", "cidade", "uf"})
public class EventResponseDto {

    @JsonProperty(value = "eventId")
    private String id;

    private String eventName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "eventDateTime")
    private LocalDateTime dateTime;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String uf;
}
