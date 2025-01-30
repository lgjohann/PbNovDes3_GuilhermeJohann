package com.johann.mseventmanager.common;

import com.johann.mseventmanager.entity.Event;
import com.johann.mseventmanager.web.dto.clients.AddressResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventConstants {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static final Event EVENT = new Event(
            "1",
            "eventName",
            LocalDateTime.parse("2024-12-31T21:00:00"),
            "01020-000",
            "logradouro",
            "bairro",
            "cidade",
            "uf"
    );

    public static final Event INVALID_EVENT = new Event(
            "2",
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );

    public static final AddressResponseDto ADDRESS_RESPONSE_DTO = new AddressResponseDto(
            "01020-000",
            "logradouro",
            "bairro",
            "cidade",
            "uf"
            );

}
