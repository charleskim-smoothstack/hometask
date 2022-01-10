package com.charleskim.hometask.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationInfo {
    @NotEmpty
    private String source;

    @NotNull
    private Boolean isValid;
}
