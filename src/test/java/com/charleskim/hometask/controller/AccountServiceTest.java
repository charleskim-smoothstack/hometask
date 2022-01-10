package com.charleskim.hometask.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
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
    void getAccountValidation_validValidationRequestWithValidAccount_ValidValidationResponse() {
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
        expectedValidationResponse.addValidationDetails(source, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_validationRequestWithInvalidAccount_ValidValidationResponse() {
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
        expectedValidationResponse.addValidationDetails(source, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_validationRequestWithValidAccountMultipleSources_ValidValidationResponse() {
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
        expectedValidationResponse.addValidationDetails(source1, isValid);
        expectedValidationResponse.addValidationDetails(source2, isValid);
        assertThat(validationResponse, is(expectedValidationResponse));
    }

    @Test
    void getAccountValidation_validationRequestWithValidAccountNoSources_ValidValidationResponse() {
        fail("Test not implemented.");
    }
}
