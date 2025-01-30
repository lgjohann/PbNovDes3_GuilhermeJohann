package com.johann.mseventmanager.web.dto.clients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

}
