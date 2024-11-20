package com.nikita;

public enum LexemeCategory{
    KEYWORD,
    IDENTIFIER,
    CONSTANT,
    SPECIAL_SYMBOL,
    OPERATION,
    UNDEFINED;

    @Override
    public String toString(){
        return switch (this){
            case KEYWORD -> "keyword";
            case IDENTIFIER -> "identifier";
            case CONSTANT -> "constant";
            case SPECIAL_SYMBOL -> "special_symbol";
            case OPERATION -> "operation";
            case UNDEFINED -> "undefined";
        };
    }
}
