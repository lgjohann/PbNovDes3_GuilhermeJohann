package com.johann.msticketmanager.common;

import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.web.dto.EventClientResponseDto;
import com.johann.msticketmanager.web.dto.EventDto;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketConstants {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static final Ticket TICKET = new Ticket(
            new BigInteger("1"),
            "customer",
            "12345678900",
            "customermail@mail.com",
            "abc10",
            "event",
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(5),
            "sucesso");

    public static final Ticket INVALID_TICKET = new Ticket(
            new BigInteger("2"),
            "",
            "",
            "",
            "",
            "",
            null,
            null,
            null
    );

    public static final EventClientResponseDto EVENT_CLIENT_RESPONSE_DTO  = new EventClientResponseDto(
            "abc10",
            "event",
            LocalDateTime.parse("2024-12-31T21:00:00"),
            "logradouro",
            "bairro",
            "cidade",
            "uf"
    );

    public static final Ticket CANCELED_TICKET = new Ticket(
            new BigInteger("1"),
            "customer",
            "12345678900",
            "customermail@mail.com",
            "abc10",
            "event",
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(5),
            "cancelado");
}
