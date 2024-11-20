package com.nikita;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class LexicalAnalyzer {
    private List<Lexeme> lexemes;

    public void startAnalyze(String input) {
        // Добавляем незначащий символ в конец входа
        input = input + '\n';
        lexemes = new ArrayList<>();
        State currentState = State.START;
        State previousState;
        boolean canAdd;
        StringBuilder currentLexeme = new StringBuilder();
        StringBuilder nextLexeme = new StringBuilder();
        int index = 0;
        int position = 0;
        int startPosition = 0;

        while (currentState != State.ERROR){
            previousState = currentState;
            canAdd = true;
            if (index == input.length()) {
                currentState = State.FINAL;
                break;
            }
            char currentSymbol = input.charAt(index);

            switch (currentState){
                case START -> {
                    if (Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (Character.isDigit(currentSymbol)) {
                        currentState = State.CONSTANT;
                    } else if (Character.isLetter(currentSymbol)) {
                        currentState = State.IDENTIFIER;
                    } else if (currentSymbol == '>' || currentSymbol == '<') {
                        currentState = State.COMPARISON;
                    } else if (currentSymbol == '+' || currentSymbol == '-' || currentSymbol == '*' || currentSymbol == '/') {
                        currentState = State.ARITHMETIC;
                    } else if (currentSymbol == '=') {
                        currentState = State.ASSIGNMENT;
                    } else if (currentSymbol == ';') {
                        currentState = State.DELIMITER;
                    }
                    else currentState = State.ERROR;
                    canAdd = false;
                    if (!Character.isWhitespace(currentSymbol)) {
                        currentLexeme.append(currentSymbol);
                        startPosition = index;
                    }
                }
                case COMPARISON -> {
                    if (Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (currentSymbol == '>') {
                        {
                            currentLexeme.append(currentSymbol);
                            currentState = State.COMPARISON;
                            //nextLexeme.append(currentSymbol);
                        }
                    }
                    else if (Character.isLetter(currentSymbol)) {
                        currentState = State.IDENTIFIER;
                        nextLexeme.append(currentSymbol);
                    } else if (Character.isDigit(currentSymbol)) {
                        currentState = State.CONSTANT;
                        nextLexeme.append(currentSymbol);
                    }else if (currentSymbol == ';') {
                        currentState = State.DELIMITER;
                        nextLexeme.append(currentSymbol);
                    }else {
                        currentState = State.ERROR;
                        if (currentSymbol == '=') {
                            nextLexeme.append(currentSymbol);
                        }
                        canAdd = false;
                    }
                }
                case ASSIGNMENT -> {
                    if (currentSymbol == '=') {
                        currentState = State.COMPARISON;
                        currentLexeme.append(currentSymbol);
                    } else if (Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (Character.isLetter(currentSymbol)) {
                        currentState = State.IDENTIFIER;
                        nextLexeme.append(currentSymbol);
                    } else if (Character.isDigit(currentSymbol)) {
                        currentState = State.CONSTANT;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == ';') {
                        currentState = State.DELIMITER;
                        nextLexeme.append(currentSymbol);
                    }
                    else {
                        currentState = State.ERROR;
                        canAdd = false;
                    }
                }
                case CONSTANT -> {
                    if (Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (Character.isDigit(currentSymbol)) {
                        canAdd = false;
                        currentState = State.CONSTANT;
                        currentLexeme.append(currentSymbol);
                    } else if (currentSymbol == '<' || currentSymbol == '>') {
                        currentState = State.COMPARISON;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '=') {
                        currentState = State.ASSIGNMENT;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '+' || currentSymbol == '-' || currentSymbol == '*' || currentSymbol == '/') {
                        currentState = State.ARITHMETIC;
                        nextLexeme.append(currentSymbol);
                    } else if(currentSymbol == ';') {
                        currentState = State.DELIMITER;
                        nextLexeme.append(currentSymbol);
                    }
                    else {
                        currentState = State.ERROR;
                        canAdd = false;
                    }
                }
                case IDENTIFIER -> {
                    if (Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (Character.isDigit(currentSymbol) || Character.isLetter(currentSymbol)) {
                        currentState = State.IDENTIFIER;
                        canAdd = false;
                        currentLexeme.append(currentSymbol);

                    } else if (currentSymbol == '>' || currentSymbol == '<') {
                        currentState = State.COMPARISON;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '=') {
                        currentState = State.ASSIGNMENT;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '+' || currentSymbol == '-' || currentSymbol == '*' || currentSymbol == '/') {
                        currentState = State.ARITHMETIC;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == ';') {
                        currentState = State.DELIMITER;
                        nextLexeme.append(currentSymbol);
                    }
                    else {
                        currentState = State.ERROR;
                        canAdd = false;
                    }
                }
                case ARITHMETIC -> {
                    if (Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (Character.isLetter(currentSymbol)) {
                        currentState = State.IDENTIFIER;
                        nextLexeme.append(currentSymbol);
                    } else if (Character.isDigit(currentSymbol)) {
                        currentState = State.CONSTANT;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '-' || currentSymbol == '+' || currentSymbol == '*' || currentSymbol == '/') {
                        currentState = State.ARITHMETIC;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == ';') {
                        currentState = State.DELIMITER;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '>' || currentSymbol == '<') {
                        currentState = State.COMPARISON;
                        nextLexeme.append(currentSymbol);
                    }
                    else {
                        currentState = State.ERROR;
                        canAdd = false;
                    }
                }
                case DELIMITER -> {
                    if(Character.isWhitespace(currentSymbol)) {
                        currentState = State.START;
                    } else if (Character.isLetter(currentSymbol)) {
                        currentState = State.IDENTIFIER;
                        nextLexeme.append(currentSymbol);
                    } else if (Character.isDigit(currentSymbol)) {
                        currentState = State.CONSTANT;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == ';') {
                        currentState = State.DELIMITER;
                        currentLexeme.append(currentSymbol);
                    } else if (currentSymbol == '=') {
                        currentState = State.COMPARISON;
                        nextLexeme.append(currentSymbol);
                    } else if (currentSymbol == '-' || currentSymbol == '+' || currentSymbol == '*' || currentSymbol == '/') {
                        currentState = State.ARITHMETIC;
                        nextLexeme.append(currentSymbol);
                    } else {
                        currentState = State.ERROR;
                        canAdd = false;
                    }
                }

            }
            if (currentState == State.ERROR) {
                throw new IllegalArgumentException("Ошибка при обработке лексемы: " + currentLexeme + " " + "Позиция начала ошибки "  + " " + index);
            }
            position = index;
            if (canAdd) {
                addLexeme(previousState, currentLexeme.toString(), position, startPosition, index - 1);
                currentLexeme = new StringBuilder(nextLexeme.toString());
                nextLexeme = new StringBuilder();
                startPosition = index;
            }
            index++;
        }
    }
    private void addLexeme(State prevState, String value, int position, int startPosition, int endPosition) {
        LexemeType lexemeType = LexemeType.UNDEFINED;
        LexemeCategory lexemeCategory = LexemeCategory.UNDEFINED;

        if (prevState == State.ARITHMETIC) {
            lexemeType = LexemeType.ARITHMETIC;
            lexemeCategory = LexemeCategory.SPECIAL_SYMBOL;
        } else if (prevState == State.ASSIGNMENT) {
            lexemeCategory = LexemeCategory.SPECIAL_SYMBOL;
            if (Objects.equals(value, "==")) {
                lexemeType = LexemeType.RELATION;
            } else {
                lexemeType = LexemeType.ASSIGNMENT;
            }
        } else if (prevState == State.CONSTANT) {
            lexemeType = LexemeType.UNDEFINED;
            lexemeCategory = LexemeCategory.CONSTANT;
        } else if (prevState == State.COMPARISON) {
            lexemeType = LexemeType.RELATION;
            lexemeCategory = LexemeCategory.SPECIAL_SYMBOL;
        } else if (prevState == State.IDENTIFIER) {
            boolean isKeyword = true;

            //value = value.toLowerCase();

            if (!value.equals(value.toLowerCase()) ) {
                throw new IllegalArgumentException("Keyword and IDENTIFIER must be in lowercase: " + value);
            }

            if (value.equals("and")) {
                lexemeType = LexemeType.AND;
            } else if (value.equals("or")) {
                lexemeType = LexemeType.OR;
            } else if (value.equals("do")) {
                lexemeType = LexemeType.DO;
            } else if (value.equals("while")) {
                lexemeType = LexemeType.WHILE;
            } else if (value.equals("loop")) {
                lexemeType = LexemeType.LOOP;
            } else if (value.equals("input")) {
                lexemeType = LexemeType.INPUT;
            } else if (value.equals("output")) {
                lexemeType = LexemeType.OUTPUT;
            } else {
                lexemeType = LexemeType.UNDEFINED;
                isKeyword = false;
            }
            if (isKeyword) {
                lexemeCategory = LexemeCategory.KEYWORD;
            } else {
                lexemeCategory = LexemeCategory.IDENTIFIER;
                lexemeType = LexemeType.UNDEFINED;
            }
        } else if (prevState == State.DELIMITER) {
            lexemeType = LexemeType.DELIMITER;
            lexemeCategory = LexemeCategory.SPECIAL_SYMBOL;
        }

        if (lexemeCategory == LexemeCategory.CONSTANT) {
            lexemeType = LexemeType.UNDEFINED;
        } else if (lexemeCategory == LexemeCategory.IDENTIFIER) {
            lexemeType = LexemeType.UNDEFINED;
        }

        Lexeme lexeme = new Lexeme(lexemeType, lexemeCategory, value, position, startPosition, endPosition);
        if (!lexeme.getValue().isEmpty()) {
            lexemes.add(lexeme);
        }
    }
}
