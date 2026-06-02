package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class PasswordInput {

    @NotBlank
    private String actualPassword;

    @NotBlank
    private String newPassword;
}
