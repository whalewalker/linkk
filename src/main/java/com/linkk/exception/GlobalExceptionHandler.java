package com.linkk.exception;

import com.linkk.data.dto.Error;
import com.linkk.data.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Response<?>> handleDuplicateKeyException(DuplicateKeyException e) {
        return commonResponseForDuplicateError(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "Duplicate key error", e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return commonResponseForBadRequest("Data integrity violation", e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleValidationException(MethodArgumentNotValidException e) {
        Response<?> response = new Response<>();
        response.setResponseMessage("Bad request");
        BindingResult result = e.getBindingResult();
        List<FieldError> errorList = result.getFieldErrors();
        List<Error> errors = errorList.stream().map(fieldError -> new Error(fieldError.getField(), fieldError.getDefaultMessage())).toList();
        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleBindingException(BindException e) {
        return commonResponseForBadRequest("Invalid url parameters", e);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return commonResponseForBadRequest("Required input file is missing", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleException(Exception e) {
        return commonResponseForSystemError("Error occurred, please contact the administrator", e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return commonResponseForBadRequest("Invalid url", e);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Response<?>> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        return commonResponseForDuplicateError(e.getMessage(), e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleResourceNotFoundException(ResourceNotFoundException e) {
        Response<?> response = new Response<>();
        response.setResponseMessage(e.getMessage());
        log.error("Error information -> {}", e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Response<?>> handleIOException(IOException e) {
        Response<?> response = new Response<>();
        response.setResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return commonResponseForBadRequest(e.getMessage(), e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return commonResponseForBadRequest("Request not supported", e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return commonResponseForBadRequest("Json Parse Error", e);
    }

    private ResponseEntity<Response<?>> commonResponseForDuplicateError(String message, Exception e) {
        Response<?> response = new Response<>();
        response.setResponseMessage(message);
        log.error(message);
        log.error("Error information -> {}", e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Response<?>> commonResponseForBadRequest(String message, Exception e) {
        Response<?> response = new Response<>();
        response.setResponseMessage(message);
        log.error(message);
        log.error("Error information -> {}", e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response<?>> commonResponseForSystemError(String message, Exception e) {
        Response<?> response = new Response<>();
        response.setResponseMessage(message);
        log.error(message);
        log.error("Error information -> {}", e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}