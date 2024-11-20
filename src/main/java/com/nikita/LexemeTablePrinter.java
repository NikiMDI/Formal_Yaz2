package com.nikita;

import com.jakewharton.fliptables.FlipTable;

import java.util.ArrayList;
import java.util.List;

public class LexemeTablePrinter {
    public static void printLexemeTable(List<Lexeme> lexemes) {
        List<String> headers = new ArrayList<>(List.of("Значение", "Тип лексемы", "Категория лексемы", "Начало лексемы", "Конец лексемы"));
        List<String[]> data = new ArrayList<>(lexemes.size());
        for (Lexeme lexeme : lexemes) {
            String[] row = new String[5];
            row[0] = lexeme.getValue();
            row[1] = lexeme.getLexemeType().toString();
            row[2] = lexeme.getLexemeCategory().toString();
            row[3] = String.valueOf(lexeme.getStartPosition());
            row[4] = String.valueOf(lexeme.getEndPosition());

            String.valueOf(data.add(row));
        }
        System.out.println(FlipTable.of(headers.toArray(new String[0]), data.toArray(new String[0][])));
    }
}
