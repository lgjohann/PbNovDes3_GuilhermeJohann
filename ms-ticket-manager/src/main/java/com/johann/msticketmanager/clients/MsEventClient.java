package com.johann.msticketmanager.clients;

import com.johann.msticketmanager.web.dto.EventClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${feign-names.ms-event-manager}:8080/api/v1/events", name = "ms-event-manager")
public interface MsEventClient {

    @GetMapping(value = "/get-event/{id}")
    EventClientResponseDto findEventById(@PathVariable String id);

}
