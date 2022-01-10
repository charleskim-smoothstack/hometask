package com.charleskim.hometask.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.charleskim.hometask.dto.AccountInfo;
import com.charleskim.hometask.dto.AccountStatus;
import com.charleskim.hometask.dto.ValidationRequest;
import com.charleskim.hometask.dto.ValidationResponse;
import com.charleskim.hometask.service.AccountService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAccountValidation_validValidationRequest_ValidValidationResponse() {
        String source = "source-url";
        Long accountNumber = 1L;
        AccountInfo accountInfo = new AccountInfo(accountNumber);
        RequestEntity<AccountInfo> requestEntity = RequestEntity.post(source)
                .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
        Boolean isValid = Boolean.TRUE;
        AccountStatus accountStatus = new AccountStatus(isValid);
        ResponseEntity<AccountStatus> responseEntity = ResponseEntity.ok(accountStatus);
        when(restTemplate.exchange(requestEntity, AccountStatus.class)).thenReturn(responseEntity);

        ValidationRequest validationRequest = new ValidationRequest(accountNumber, Arrays.asList(source));
        ValidationResponse returnedValidationResponse = accountService.getAccountValidation(validationRequest);
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse.addValidationDetails(source, isValid);
        assertThat(returnedValidationResponse, is(validationResponse));
    }
}
