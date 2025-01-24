package com.johann.msticketmanager.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
public class TicketResponseDto {

    private BigInteger ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private EventResponseDto event;
    private BigDecimal brlTotalAmount;
    private BigDecimal usdTotalAmount;
    private String status;

}
