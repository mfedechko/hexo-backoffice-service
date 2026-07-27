package com.gpn.crm.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleUpstreamError(RestClientResponseException ex) {
        HttpStatusCode upstreamStatus = ex.getStatusCode();
        // Client errors (e.g. 404 for an unknown product id) are meaningful to our own callers,
        // so pass them through as-is; only genuine upstream failures collapse to 502.
        HttpStatus responseStatus = upstreamStatus.is4xxClientError()
                ? HttpStatus.valueOf(upstreamStatus.value())
                : HttpStatus.BAD_GATEWAY;

        return ResponseEntity.status(responseStatus).body(Map.of(
                "error", "keycrm_upstream_error",
                "upstreamStatus", upstreamStatus.value(),
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleUpstreamUnreachable(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(Map.of(
                "error", "keycrm_unreachable",
                "message", ex.getMessage()
        ));
    }
}
