package com.charleskim.hometask.controller;

import javax.validation.Valid;

import com.charleskim.hometask.dto.ValidationRequest;
import com.charleskim.hometask.dto.ValidationResponse;
import com.charleskim.hometask.service.AccountService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping(path = "validate",
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ValidationResponse> getAccountValidation(
            @Valid @RequestBody ValidationRequest validationRequest) {
        ValidationResponse accountValidationResponse = accountService.getAccountValidation(validationRequest);
        return ResponseEntity.ok(accountValidationResponse);
    };
}
