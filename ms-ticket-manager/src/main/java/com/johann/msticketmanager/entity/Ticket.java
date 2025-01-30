package com.johann.msticketmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Document(collection = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Ticket {

    @Transient
    public static final String SEQUENCE_NAME = "tickets_sequence";

    @Id
    private BigInteger ticketId;
    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String eventName;
    private BigDecimal brlAmount;
    private BigDecimal usdAmount;
    private String status;

}
