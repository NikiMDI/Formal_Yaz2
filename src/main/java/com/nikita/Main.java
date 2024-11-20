package com.nikita;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        //String input = "do while x <>>>>> 10\n    1x = x + 1;\n   x = x * 5\n    output x\nloop";

        //LexicalAnalyzer analyzer = new LexicalAnalyzer();
        //analyzer.startAnalyze(input);

        //List<Lexeme> lexemes = analyzer.getLexemes();
        //LexemeTablePrinter.printLexemeTable(lexemes);
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class)){
            syntacticalAnalyzerLab(context, "src/main/resources/program1.txt");
        }
    }
    private static void syntacticalAnalyzerLab(AnnotationConfigApplicationContext context, String filepath) {
        String program;
        try{
            program = Files.readString(Path.of(filepath));
        }catch(IOException e){
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }
        SyntaxAnalyzer syntaxAnalyzer = context.getBean(SyntaxAnalyzer.class);
        syntaxAnalyzer.startAnalyze(program);
        System.out.println("Синтаксический анализ прошёл успешно");
    }
}