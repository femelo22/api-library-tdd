package br.com.lfmelo.resources.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String msg) {
        super(msg);
    }
}
