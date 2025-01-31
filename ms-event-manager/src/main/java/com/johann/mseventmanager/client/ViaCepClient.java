package com.johann.mseventmanager.client;

import com.johann.mseventmanager.web.dto.clients.AddressResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://viacep.com.br/ws", name = "viacep")
public interface ViaCepClient {

    @GetMapping(value = "{cep}/json", params = "cep")
    AddressResponseDto getAddress(@PathVariable("cep") String cep);
}
