package com.johann.msticketmanager.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ErrorMessageTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private BindingResult bindingResult;

    @Test
    void errorMessageConstructor_WithValidRequestAndStatus(){
        String uri = "/api/tickets";
        String method = "POST";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Validation failed";

        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);

        ErrorMessage errorMessage = new ErrorMessage(request, status, message);

        assertThat(errorMessage.getPath()).isEqualTo(uri);
        assertThat(errorMessage.getMethod()).isEqualTo(method);
        assertThat(errorMessage.getStatus()).isEqualTo(status.value());
        assertThat(errorMessage.getStatusText()).isEqualTo(status.getReasonPhrase());
        assertThat(errorMessage.getMessage()).isEqualTo(message);
        assertThat(errorMessage.getErrors()).isNull();
    }

    @Test
    void errorMessageConstructor_WithBindingResult_ShouldPopulateErrorsMap() {
        String uri = "/api/tickets";
        String method = "POST";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        String message = "Invalid fields";
        
        FieldError fieldError1 = new FieldError("object", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("object", "field2", "must be positive");

        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ErrorMessage errorMessage = new ErrorMessage(request, status, message, bindingResult);

        Map<String, String> errors = errorMessage.getErrors();
        assertThat(errors).containsEntry("field2", "must be positive");
        assertThat(errors).containsEntry("field1", "must not be null");
        assertThat(errors).hasSize(2);
    }

    @Test
    void errorMessageConstructor_WithEmptyBindingResult_CreateEmptyErrorsMap() {
        when(request.getRequestURI()).thenReturn("/api/tickets");
        when(request.getMethod()).thenReturn("GET");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ErrorMessage errorMessage = new ErrorMessage(
            request, 
            HttpStatus.BAD_REQUEST, 
            "No errors but test case", 
            bindingResult
        );

        assertThat(errorMessage.getErrors()).isEmpty();
    }
}