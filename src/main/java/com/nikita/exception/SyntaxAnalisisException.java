package com.nikita.exception;

public class SyntaxAnalisisException extends RuntimeException{
    public SyntaxAnalisisException(String msg){
        super(msg);
    }
    public SyntaxAnalisisException(String msg, Throwable cause){
        super(msg, cause);
    }
}
