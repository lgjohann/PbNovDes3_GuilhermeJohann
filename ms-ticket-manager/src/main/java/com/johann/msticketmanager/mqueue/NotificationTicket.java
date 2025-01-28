package com.johann.msticketmanager.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johann.msticketmanager.entity.Email;
import com.johann.msticketmanager.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationTicket {

    private final RabbitTemplate rabbitTemplate;
    private final Queue sendEmailQueue;

    public void publishTicketCreated(Ticket ticket) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Email email = new Email();
        email.setFrom("guilherme4695@gmail.com");
        email.setTo("useremail_imjustmakeingsure_it_doesnt_exists@email.com");
        email.setSubject("Ticket created successfully!");

        StringBuilder mailBody = new StringBuilder();
        mailBody.append("Hello, " + ticket.getCustomerName());
        mailBody.append("!\nThank you for buying a ticket for the event " + ticket.getEventName());
        mailBody.append(", i'm sure you will have a great time! but if you don't, it's a skill issue. hehe. We only sell tickets for the best events...");
        mailBody.append("\nAnyway, here are the full information of your ticket in case you need it:\n\n");
        mailBody.append("ID: " + ticket.getTicketId());
        mailBody.append("\n");
        mailBody.append("Customer: " + ticket.getCustomerName());
        mailBody.append("\n");
        mailBody.append("Event: " + ticket.getEventName());
        mailBody.append("\n");
        mailBody.append("Ticket price in BRL: R$" + ticket.getBrlAmount());
        mailBody.append("\n");
        mailBody.append("Ticket price in USD: $" + ticket.getUsdAmount());
        email.setBody(mailBody.toString());
        String json = mapper.writeValueAsString(email);
        rabbitTemplate.convertAndSend(sendEmailQueue.getName(), json);
    }

}
