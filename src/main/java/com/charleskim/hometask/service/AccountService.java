package com.charleskim.hometask.service;

import java.util.Arrays;
import java.util.List;

import com.charleskim.hometask.dto.AccountInfo;
import com.charleskim.hometask.dto.AccountStatus;
import com.charleskim.hometask.dto.ValidationRequest;
import com.charleskim.hometask.dto.ValidationResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final RestTemplate restTemplate;

    @Value("${data-source-url}")
    private String dataSourceUrl;

    private List<String> sources;

    public ValidationResponse getAccountValidation(ValidationRequest validationRequest) {
        if (!validationRequest.getSources().isEmpty()) {
            sources = validationRequest.getSources();
        } else {
            sources = Arrays.asList(dataSourceUrl);
        }
        ValidationResponse accountValidationResponse = new ValidationResponse();
        AccountInfo accountInfo = new AccountInfo(validationRequest.getAccountNumber());
        for (String source : sources) {
            RequestEntity<AccountInfo> requestEntity = RequestEntity.post(source)
                    .contentType(MediaType.APPLICATION_JSON).body(accountInfo);
            ResponseEntity<AccountStatus> responseEntity = restTemplate.exchange(requestEntity,
                    AccountStatus.class);
            AccountStatus accountValidation = responseEntity.getBody();
            accountValidationResponse.addValidationDetails(source, accountValidation.getIsValid());
        }
        return accountValidationResponse;
    }
}
