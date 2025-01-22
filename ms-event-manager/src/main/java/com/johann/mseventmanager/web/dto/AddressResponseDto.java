package com.johann.mseventmanager.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressResponseDto {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

}
