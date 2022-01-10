package com.charleskim.hometask.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import com.charleskim.hometask.dto.ValidationRequest;
import com.charleskim.hometask.dto.ValidationResponse;
import com.charleskim.hometask.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAccountValidation_ValidAccountOneSource_IsValidTrue()
            throws JsonProcessingException, Exception {
        Long accountNumber = 1L;
        String source = "data-source-url";
        ValidationRequest validationRequest = new ValidationRequest(accountNumber, Arrays.asList(source));
        ValidationResponse validationResponse = new ValidationResponse();
        Boolean isValid = Boolean.TRUE;
        validationResponse.addValidationResult(source, isValid);
        when(accountService.getAccountValidation(validationRequest)).thenReturn(validationResponse);

        String apiPath = "/account/validate";
        mockMvc.perform(post(apiPath).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationRequest))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    void getAccountValidation_InvalidAccountOneSource_IsValidFalse()
            throws JsonProcessingException, Exception {
        Long accountNumber = 1L;
        String source = "data-source-url";
        ValidationRequest validationRequest = new ValidationRequest(accountNumber, Arrays.asList(source));
        ValidationResponse validationResponse = new ValidationResponse();
        Boolean isValid = Boolean.FALSE;
        validationResponse.addValidationResult(source, isValid);
        when(accountService.getAccountValidation(validationRequest)).thenReturn(validationResponse);

        String apiPath = "/account/validate";
        mockMvc.perform(post(apiPath).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationRequest))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    void getAccountValidation_ValidAccountMultipleSources_IsValidTrueForBothSources()
            throws JsonProcessingException, Exception {
        Long accountNumber = 1L;
        String source1 = "data-source1-url";
        String source2 = "data-source2-url";
        ValidationRequest validationRequest = new ValidationRequest(accountNumber, Arrays.asList(source1, source2));
        ValidationResponse validationResponse = new ValidationResponse();
        Boolean isValid = Boolean.TRUE;
        validationResponse.addValidationResult(source1, isValid);
        validationResponse.addValidationResult(source2, isValid);
        when(accountService.getAccountValidation(validationRequest)).thenReturn(validationResponse);

        String apiPath = "/account/validate";
        mockMvc.perform(post(apiPath).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationRequest))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    void getAccountValidation_ValidAccountNoSources_IsValidTrueForDefaultSource()
            throws JsonProcessingException, Exception {
        Long accountNumber = 1L;
        String source = "data-source-url";
        ValidationRequest validationRequest = new ValidationRequest();
        validationRequest.setAccountNumber(accountNumber);
        ValidationResponse validationResponse = new ValidationResponse();
        Boolean isValid = Boolean.TRUE;
        validationResponse.addValidationResult(source, isValid);
        when(accountService.getAccountValidation(validationRequest)).thenReturn(validationResponse);

        String apiPath = "/account/validate";
        mockMvc.perform(post(apiPath).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationRequest))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    void getAccountValidation_BadSource_InternalServerError()
            throws JsonProcessingException, Exception {
        Long accountNumber = 1L;
        String source = "data-source-url";
        ValidationRequest validationRequest = new ValidationRequest();
        validationRequest.setAccountNumber(accountNumber);
        ValidationResponse validationResponse = new ValidationResponse();
        Boolean isValid = Boolean.TRUE;
        validationResponse.addValidationResult(source, isValid);
        when(accountService.getAccountValidation(validationRequest))
                .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Account validation failed."));

        String apiPath = "/account/validate";
        mockMvc.perform(post(apiPath).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validationRequest))).andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
