package com.johann.msticketmanager.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventClientResponseDto {

    private String id;

    private String eventName;

    private LocalDateTime dateTime;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String uf;

}
