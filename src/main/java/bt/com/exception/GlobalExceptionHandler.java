package bt.com.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import bt.com.dto.constants.Literals;
import bt.com.dto.constants.Messages;
import bt.com.dto.projection.ErrorView;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorView> handlePatientNotFound(
            PatientNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                Messages.PATIENT_NOT_FOUND_LOG,
                request.getRequestURI(),
                ex.getMessage()
        );

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                Literals.ERROR_PATIENT_NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorView> handleDoctorNotFound(
            DoctorNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                Messages.DOCTOR_NOT_FOUND_LOG,
                request.getRequestURI(),
                ex.getMessage()
        );

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                Literals.ERROR_DOCTOR_NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorView>
    handleAppointmentNotFound(
            AppointmentNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                Messages.APPOINTMENT_NOT_FOUND_LOG,
                request.getRequestURI(),
                ex.getMessage()
        );

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                Literals.ERROR_APPOINTMENT_NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorView> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse(Messages.INVALID_REQUEST);

        log.warn(
                Messages.INVALID_REQUEST_LOG,
                request.getRequestURI(),
                message
        );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                Literals.ERROR_INVALID_REQUEST,
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorView>
    handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn(
                Messages.INVALID_REQUEST_LOG,
                request.getRequestURI(),
                ex.getMessage()
        );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                Literals.ERROR_INVALID_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorView>
    handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.warn(
                Messages.INVALID_STATE_LOG,
                request.getRequestURI(),
                ex.getMessage()
        );

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                Literals.ERROR_INVALID_STATE,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorView>
    handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        log.error(
                Messages.UNEXPECTED_ERROR_LOG,
                request.getRequestURI(),
                ex
        );

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                Literals.ERROR_INTERNAL_SERVER,
                Messages.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorView> buildErrorResponse(
            HttpStatus status,
            String error,
            String message,
            String path) {

        ErrorView response =
                ErrorView.builder()
                        .timestamp(Instant.now())
                        .status(status.value())
                        .error(error)
                        .message(message)
                        .path(path)
                        .build();

        return ResponseEntity
                .status(status)
                .body(response);
    }
}