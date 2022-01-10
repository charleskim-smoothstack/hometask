package com.charleskim.hometask.controller;

import javax.validation.Valid;

import com.charleskim.hometask.dto.ValidationRequest;
import com.charleskim.hometask.dto.ValidationResponse;
import com.charleskim.hometask.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("validate")
    public ResponseEntity<ValidationResponse> getAccountValidation(
            @Valid @RequestBody ValidationRequest validationRequest) {
        ValidationResponse accountValidationResponse = accountService.getAccountValidation(validationRequest);
        return ResponseEntity.ok(accountValidationResponse);
    };
}
