package com.nikita;

public enum LexemeType {
    DELIMITER,
    AND,
    OR,
    RELATION,
    ARITHMETIC,
    ASSIGNMENT,
    DO,
    WHILE,
    LOOP,
    INPUT,
    OUTPUT,
    UNDEFINED;

    @Override
    public String toString() {
        return switch (this){
            case DELIMITER -> "delimiter";
            case AND -> "and";
            case OR -> "or";
            case RELATION -> "relation";
            case ARITHMETIC -> "arithmetic";
            case ASSIGNMENT -> "assignment";
            case DO -> "do";
            case WHILE -> "while";
            case LOOP -> "loop";
            case INPUT -> "input";
            case OUTPUT -> "output";
            case UNDEFINED -> "undefined";
        };
    }
}
