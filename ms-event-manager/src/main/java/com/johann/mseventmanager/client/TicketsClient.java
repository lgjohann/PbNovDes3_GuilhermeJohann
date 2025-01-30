package com.johann.mseventmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(url = "localhost:8081/api/v1/tickets", name = "ms-ticket-manager")
public interface TicketsClient {

    @GetMapping(value = "/check-tickets-by-event/{eventId}")
    ResponseEntity<?> checkTicketsByEventId(@PathVariable String eventId);
}
