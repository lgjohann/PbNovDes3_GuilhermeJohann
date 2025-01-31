package com.johann.msticketmanager.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Email implements Serializable{
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;

}
