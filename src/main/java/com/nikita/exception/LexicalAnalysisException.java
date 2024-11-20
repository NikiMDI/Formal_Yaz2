package com.nikita.exception;

public class LexicalAnalysisException extends RuntimeException{
    public LexicalAnalysisException(String msg){
        super(msg);
    }
    public LexicalAnalysisException(String msg, Throwable cause){
        super(msg, cause);
    }
}
