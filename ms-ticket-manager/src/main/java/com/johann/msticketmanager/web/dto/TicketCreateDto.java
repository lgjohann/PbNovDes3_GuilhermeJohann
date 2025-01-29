package com.johann.msticketmanager.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateDto {

    @NotBlank
    private String customerName;

    @CPF
    private String cpf;

    @NotBlank
    @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",message = "Invalid e-mail format")
    private String customerMail;

    @NotBlank
    private String eventId;

    @NotBlank
    private String eventName;

    @DecimalMin("0.0")
    private BigDecimal brlAmount;

    @DecimalMin("0.0")
    private BigDecimal usdAmount;

}
