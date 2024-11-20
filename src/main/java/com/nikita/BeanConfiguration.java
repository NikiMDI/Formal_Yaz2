package com.nikita;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public SyntaxAnalyzer syntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        return new SyntaxAnalyzer(lexicalAnalyzer);
    }

    @Bean
    public LexicalAnalyzer lexicalAnalyzer() {
        return new LexicalAnalyzer();
    }
}
