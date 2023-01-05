package br.com.lfmelo.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErros> objectNotFoundException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        BindingResult bindingResult = ex.getBindingResult();

        ApiErros apiErros = new ApiErros(bindingResult);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErros);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErros> handleBusinessException(BusinessException ex) {

        ApiErros apiErros = new ApiErros(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErros);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErros> handleBusinessException(NotFoundException ex) {

        ApiErros apiErros = new ApiErros(ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErros);
    }
}
