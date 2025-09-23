package com.dev.tickets.controllers;

import com.dev.tickets.domain.dtos.ErrorDto;
import com.dev.tickets.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("Caught unexpected exception: ", e);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Oops! Something went wrong!")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Caught ConstraintViolationException: ", e);

        String message = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation ->
                        violation.getPropertyPath() + ": " + violation.getMessage()
                ).orElse("Constraint violation occurred");

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Caught MethodArgumentNotValidException: ", e);

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream().findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error occurred");

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        log.error("Caught UserNotFoundException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("User not found")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException e) {
        log.error("Caught EventNotFoundException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Event not found")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException e) {
        log.error("Caught TicketTypeNotFoundException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Ticket type not found")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException e) {
        log.error("Caught EventUpdateException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Unable to update event")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ErrorDto> handleQrCodeGenerationException(QrCodeGenerationException e) {
        log.error("Caught QrCodeGenerationException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unable to generate QR Code")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleQrCodeNotFoundException(QrCodeNotFoundException e) {
        log.error("Caught QrCodeNotFoundException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("QR Code not found")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(TicketsSoldOutException.class)
    public ResponseEntity<ErrorDto> handleTicketsSoldOutException(TicketsSoldOutException e) {
        log.error("Caught TicketsSoldOutException: ", e);

        ErrorDto error = ErrorDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Tickets are sold out for this ticket type")
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }
}
