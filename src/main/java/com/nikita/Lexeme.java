package com.nikita;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Lexeme{
    @ToString.Include
    private final LexemeType lexemeType;
    @ToString.Include
    private final LexemeCategory lexemeCategory;
    @ToString.Include
    private final String value;
    @ToString.Include
    private final int position;
    @ToString.Include
    private final int startPosition;
    @ToString.Include
    private final int endPosition;
}
