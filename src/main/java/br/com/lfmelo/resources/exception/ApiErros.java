package br.com.lfmelo.resources.exception;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErros {

    private List<String> errors;

    public ApiErros(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> {
            System.out.println(error);
            this.errors.add(error.getDefaultMessage());
        });
    }

    public List<String> getErrors() {
        return errors;
    }
}
