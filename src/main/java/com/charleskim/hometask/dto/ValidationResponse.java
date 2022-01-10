package com.charleskim.hometask.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse {
    private List<ValidationInfo> result = new ArrayList<>();

    public void addValidationDetails(String sourceUrl, Boolean isValid) {
        result.add(new ValidationInfo(sourceUrl, isValid));
    }
}
