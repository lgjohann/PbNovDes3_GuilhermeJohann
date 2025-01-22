package com.johann.mseventmanager.client;

import com.johann.mseventmanager.web.dto.AddressResponseDto;
import org.bson.json.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://viacep.com.br/ws", name = "viacep")
public interface ViaCepClient {

    @GetMapping(value = "{cep}/json", params = "cep")
    AddressResponseDto getAddress(@RequestParam("cep") String cep);
}
