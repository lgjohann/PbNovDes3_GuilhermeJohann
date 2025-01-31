package com.johann.mseventmanager.web.dto.clients;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TicketEventDto {

    private String eventId;
    private String eventName;
    private LocalDateTime eventDateTime;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;

}
