package com.charleskim.hometask.dto;

import java.util.List;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequest {
    @Positive
    private Long accountNumber;

    private List<String> sources;
}
