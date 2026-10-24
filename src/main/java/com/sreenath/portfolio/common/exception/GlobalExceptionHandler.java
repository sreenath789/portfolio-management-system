package com.sreenath.portfolio.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException invalidRequestException,
                                                                    HttpServletRequest httpServletRequest){
        return buildErrorResponse(invalidRequestException,HttpStatus.BAD_REQUEST,httpServletRequest);
    }
    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientQuantity(InsufficientQuantityException insufficientQuantityException,
                                                                  HttpServletRequest httpServletRequest){
        return buildErrorResponse(insufficientQuantityException,HttpStatus.BAD_REQUEST,httpServletRequest);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(DuplicateResourceException duplicateResourceException,
                                                                  HttpServletRequest httpServletRequest){
        return buildErrorResponse(duplicateResourceException,HttpStatus.CONFLICT,httpServletRequest);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                     HttpServletRequest httpServletRequest){
        return buildErrorResponse(resourceNotFoundException,HttpStatus.NOT_FOUND,httpServletRequest);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException tokenExpiredException,
                                                                     HttpServletRequest httpServletRequest){
        return buildErrorResponse(tokenExpiredException,HttpStatus.UNAUTHORIZED,httpServletRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidateExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error->errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    public ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex,
                                                            HttpStatus httpStatus,
                                                            HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(ex.getMessage())
                .path(httpServletRequest.getRequestURI())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
