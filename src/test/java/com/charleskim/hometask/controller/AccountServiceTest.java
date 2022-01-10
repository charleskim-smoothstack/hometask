package com.charleskim.hometask.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.charleskim.hometask.dto.AccountInfo;
import com.charleskim.hometask.dto.AccountStatus;
import com.charleskim.hometask.dto.ValidationRequest;
import com.charleskim.hometask.dto.ValidationResponse;
import com.charleskim.hometask.service.AccountService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${data-source-url}")
    private String dataSourceUrl;

    @Test
    void getAccountValidation_ValidAccountOneSource_IsValidTrue() {
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
        ValidationResponse validationResponse = accountService.getAccountValidation(validationRequest);
        ValidationResponse expectedValidationResponse = new ValidationResponse();
        expectedValidationResponse.addValidationResult(source, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_InvalidAccountOneSource_IsValidFalse() {
        String source = "source-url";
        Long accountNumber = 1L;
        AccountInfo accountInfo = new AccountInfo(accountNumber);
        RequestEntity<AccountInfo> requestEntity = RequestEntity.post(source)
                .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
        Boolean isValid = Boolean.FALSE;
        AccountStatus accountStatus = new AccountStatus(isValid);
        ResponseEntity<AccountStatus> responseEntity = ResponseEntity.ok(accountStatus);
        when(restTemplate.exchange(requestEntity, AccountStatus.class)).thenReturn(responseEntity);

        ValidationRequest validationRequest = new ValidationRequest(accountNumber, Arrays.asList(source));
        ValidationResponse validationResponse = accountService.getAccountValidation(validationRequest);
        ValidationResponse expectedValidationResponse = new ValidationResponse();
        expectedValidationResponse.addValidationResult(source, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_ValidAccountMultipleSources_IsValidTrueForBothSources() {
        String source1 = "source1-url";
        String source2 = "source2-url";
        Long accountNumber = 1L;
        AccountInfo accountInfo = new AccountInfo(accountNumber);
        RequestEntity<AccountInfo> requestEntity1 = RequestEntity.post(source1)
                .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
        Boolean isValid = Boolean.TRUE;
        AccountStatus accountStatus = new AccountStatus(isValid);
        ResponseEntity<AccountStatus> responseEntity = ResponseEntity.ok(accountStatus);
        when(restTemplate.exchange(requestEntity1, AccountStatus.class)).thenReturn(responseEntity);
        RequestEntity<AccountInfo> requestEntity2 = RequestEntity.post(source2)
                .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
        when(restTemplate.exchange(requestEntity2, AccountStatus.class)).thenReturn(responseEntity);

        ValidationRequest validationRequest = new ValidationRequest(accountNumber, Arrays.asList(source1, source2));
        ValidationResponse validationResponse = accountService.getAccountValidation(validationRequest);
        ValidationResponse expectedValidationResponse = new ValidationResponse();
        expectedValidationResponse.addValidationResult(source1, isValid);
        expectedValidationResponse.addValidationResult(source2, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_ValidAccountNoSources_IsValidTrueForDefaultSource() {
        Long accountNumber = 1L;
        AccountInfo accountInfo = new AccountInfo(accountNumber);
        RequestEntity<AccountInfo> requestEntity = RequestEntity.post(dataSourceUrl)
                .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
        Boolean isValid = Boolean.TRUE;
        AccountStatus accountStatus = new AccountStatus(isValid);
        ResponseEntity<AccountStatus> responseEntity = ResponseEntity.ok(accountStatus);
        when(restTemplate.exchange(requestEntity, AccountStatus.class)).thenReturn(responseEntity);

        ValidationRequest validationRequest = new ValidationRequest();
        validationRequest.setAccountNumber(accountNumber);
        ValidationResponse validationResponse = accountService.getAccountValidation(validationRequest);
        ValidationResponse expectedValidationResponse = new ValidationResponse();
        expectedValidationResponse.addValidationResult(dataSourceUrl, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_BadSource_ResponseStatusException() {
        Long accountNumber = 1L;
        AccountInfo accountInfo = new AccountInfo(accountNumber);
        RequestEntity<AccountInfo> requestEntity = RequestEntity.post(dataSourceUrl)
                .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
        when(restTemplate.exchange(requestEntity, AccountStatus.class))
                .thenThrow(new RestClientException("Server failed."));

        ValidationRequest validationRequest = new ValidationRequest();
        validationRequest.setAccountNumber(accountNumber);
        assertThrows(ResponseStatusException.class, () -> {
            accountService.getAccountValidation(validationRequest);
        });
    }
}
