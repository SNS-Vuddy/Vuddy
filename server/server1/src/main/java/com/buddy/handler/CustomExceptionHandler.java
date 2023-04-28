package com.buddy.handler;

import com.buddy.model.dto.response.ErrorRes;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // ConstraintViolationException 커스텀 처리
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    protected ResponseEntity<ErrorRes> handleDuplicateNickname(ConstraintViolationException e) {

        String errorMessage = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        ErrorRes errorResponse = new ErrorRes(400, errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // MethodArgumentNotValidException 커스텀 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorRes errorResponse = new ErrorRes(400, errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // NullPointerException 예외 커스텀 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {

        String errorMessage = "NullPointerException 예외가 발생했습니다.";

        ErrorRes errorResponse = new ErrorRes(400, errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    // IllegalStateException 예외 커스텀 처리
    @ExceptionHandler(value = {IllegalStateException.class})
    @ResponseBody
    protected ResponseEntity<ErrorRes> handleDuplicateNickname(IllegalStateException e) {

        String errorMessage = e.getMessage();

        ErrorRes errorResponse = new ErrorRes(409, errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
