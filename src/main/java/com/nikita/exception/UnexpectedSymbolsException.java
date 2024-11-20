package com.nikita.exception;

public class UnexpectedSymbolsException extends RuntimeException{
    public UnexpectedSymbolsException(int position){
        super("Найдены лишние символы на позиции: " + position);
    }
}
