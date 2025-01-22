package com.johann.mseventmanager.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventCreateDto {

    @NotBlank
    private String eventName;
    @NotBlank
    private LocalDateTime dateTime;
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$", message = "Invalid cep format")
    private String cep;
}
