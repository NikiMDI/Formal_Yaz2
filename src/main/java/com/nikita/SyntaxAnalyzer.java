package com.nikita;

import com.nikita.exception.*;
import lombok.RequiredArgsConstructor;

import java.util.ListIterator;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class SyntaxAnalyzer {
    private final LexicalAnalyzer lexicalAnalyzer;
    private ListIterator<Lexeme> lexemeIterator;
    private Lexeme currentLexeme;

    public boolean startAnalyze(String input) {
        try {
            lexicalAnalyzer.startAnalyze(input);
            LexemeTablePrinter.printLexemeTable(lexicalAnalyzer.getLexemes());
            checkLexemesAvailable();
            lexemeIterator = lexicalAnalyzer.getLexemes().listIterator();
            checkDOWhileStatement();
            return true;
        } catch (IllegalArgumentException ex) {
            throw new LexicalAnalysisException("Ошибка при лексическом анализе: ", ex);
        } catch (InvalidLexemeException | NoSuchElementException ex) {
            throw new SyntaxAnalisisException("Ошибка при синтиксическом анализе: ", ex);
        }
    }

    private void checkLexemesAvailable() {
        if (lexicalAnalyzer.getLexemes().isEmpty()){
            throw new NoLexemesFoundException("Список лексем пуст");
        }
    }

    private void checkDOWhileStatement() {
        if (lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            if (currentLexeme.getLexemeType() != LexemeType.DO) {
                throw new InvalidLexemeException(LexemeType.DO, currentLexeme.getPosition());
            }

            if (lexemeIterator.hasNext()) {
                currentLexeme = lexemeIterator.next();
                if (currentLexeme.getLexemeType() != LexemeType.WHILE) {
                    throw new InvalidLexemeException(LexemeType.WHILE, currentLexeme.getPosition());
                }

                // Проверяем условие
                checkCondition();

                // Проверяем наличие блока операторов
                checkStatement();

                if (lexemeIterator.hasNext()) {
                    currentLexeme = lexemeIterator.next();
                    if (currentLexeme.getLexemeType() != LexemeType.LOOP) {
                        throw new InvalidLexemeException(LexemeType.LOOP, currentLexeme.getPosition());
                    }
                } else {
                    throw new InvalidLexemeException(LexemeType.LOOP, currentLexeme.getPosition());
                }
            } else {
                throw new InvalidLexemeException(LexemeType.WHILE, currentLexeme.getPosition());
            }
        } else {
            throw new InvalidLexemeException(LexemeType.DO, currentLexeme.getPosition());
        }
    }


    private void checkLoop() {
        if(lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            if (currentLexeme.getLexemeType() == LexemeType.LOOP) {
                throw new UnexpectedSymbolsException(currentLexeme.getPosition());
            }
        }
    }

    private void checkStatement() {
        if(lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            // Проверяем, является ли текущий лексем оператором вывода
            if (currentLexeme.getLexemeType() == LexemeType.OUTPUT) {
                checkOutputStatement();
            } else {
                checkAssignmentStatement();
            }
        } else {
            throw new InvalidLexemeException(LexemeCategory.IDENTIFIER, currentLexeme.getPosition());
        }
    }

    // Новый метод для проверки оператора вывода
    private void checkOutputStatement() {
        if(lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            if (currentLexeme.getLexemeCategory() != LexemeCategory.IDENTIFIER){
                throw new InvalidLexemeException(LexemeCategory.IDENTIFIER, currentLexeme.getPosition());
            }
        } else {
            throw new InvalidLexemeException(LexemeCategory.IDENTIFIER, currentLexeme.getPosition());
        }
    }

    private void checkAssignmentStatement() {
        if (currentLexeme.getLexemeCategory() != LexemeCategory.IDENTIFIER ) {
            System.out.println("Ошибка на позиции " + currentLexeme.getPosition() + ": ожидалось identifier или constant, но получено " + currentLexeme.getLexemeCategory());
            throw new InvalidLexemeException(LexemeCategory.IDENTIFIER, currentLexeme.getPosition());
        }
        if (lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            if (currentLexeme.getLexemeType() != LexemeType.ASSIGNMENT ) {
                System.out.println("Ошибка на позиции " + currentLexeme.getPosition() + ": ожидалось identifier или constant, но получено " + currentLexeme.getLexemeCategory());
                throw new InvalidLexemeException(LexemeType.ASSIGNMENT, currentLexeme.getPosition());
            }
            checkArithmeticExpression();
        } else {
            System.out.println("Ошибка на позиции " + currentLexeme.getPosition() + ": ожидалось identifier или constant, но получено " + currentLexeme.getLexemeCategory());
            throw new InvalidLexemeException(LexemeType.ASSIGNMENT, currentLexeme.getPosition());
        }
    }

    private void checkArithmeticExpression() {
        checkOperand();
        if(lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            while (currentLexeme.getLexemeType() == LexemeType.ARITHMETIC){
                checkOperand();
                if (lexemeIterator.hasNext()) {
                    currentLexeme = lexemeIterator.next();
                } else {
                    throw new InvalidLexemeException(LexemeType.LOOP, currentLexeme.getPosition());
                }
            }
            currentLexeme = lexemeIterator.previous();
        } else {
            throw new InvalidLexemeException(LexemeType.LOOP, currentLexeme.getPosition());
        }
    }

    private void checkCondition(){
        checkLogicalExpression();
        if (lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            while (currentLexeme.getLexemeType() == LexemeType.OR){
                checkLogicalExpression();
                if (lexemeIterator.hasNext()) {
                    currentLexeme = lexemeIterator.next();
                }
            }
        }
    }

    private void checkLogicalExpression() {
        checkRelationExpression();
        if (lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            while (currentLexeme.getLexemeType() == LexemeType.AND){
                checkRelationExpression();
                if(lexemeIterator.hasNext()){
                    currentLexeme = lexemeIterator.next();
                }
            }
            currentLexeme = lexemeIterator.previous();
        }
    }

    private void checkRelationExpression() {
        checkOperand();
        if(lexemeIterator.hasNext()) {
            currentLexeme = lexemeIterator.next();
            if (currentLexeme.getLexemeType() == LexemeType.RELATION){
                checkOperand();
            } else {
                currentLexeme = lexemeIterator.previous();
            }
        }
    }

    private void checkOperand() {
        if (lexemeIterator.hasNext()){
            currentLexeme = lexemeIterator.next();
            if (currentLexeme.getLexemeCategory() != LexemeCategory.CONSTANT && currentLexeme.getLexemeCategory() != LexemeCategory.IDENTIFIER){
                System.out.println("Ошибка на позиции " + currentLexeme.getPosition() + ": ожидалось identifier или constant, но получено " + currentLexeme.getLexemeCategory());
                throw new InvalidLexemeException(LexemeCategory.IDENTIFIER, LexemeCategory.CONSTANT, currentLexeme.getPosition());
            }
        } else {

            throw new InvalidLexemeException(LexemeCategory.IDENTIFIER, LexemeCategory.CONSTANT, currentLexeme.getPosition());
        }
    }
}
