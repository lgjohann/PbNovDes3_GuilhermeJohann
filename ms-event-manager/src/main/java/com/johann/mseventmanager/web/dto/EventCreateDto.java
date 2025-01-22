package com.johann.mseventmanager.web.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventCreateDto {

    private String eventName;
    private LocalDateTime dateTime;
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$", message = "Invalid cep format")
    private String cep;
}
