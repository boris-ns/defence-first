package rs.ac.uns.ftn.pkiservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rs.ac.uns.ftn.pkiservice.dto.response.ErrorMessageDTO;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ApiRequestException;
import rs.ac.uns.ftn.pkiservice.exception.exceptions.ResourceNotFoundException;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<ErrorMessageDTO> handleApiRequestException(ApiRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(e.getMessage(), badRequest, ZonedDateTime.now());
        return new ResponseEntity<>(errorMessageDTO, badRequest);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessageDTO> handleNotFoundException(ResourceNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(e.getMessage(), notFound, ZonedDateTime.now());
        return new ResponseEntity<>(errorMessageDTO, notFound);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessageDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(message, badRequest, ZonedDateTime.now());
        return new ResponseEntity<>(errorMessage, badRequest);
    }
}
