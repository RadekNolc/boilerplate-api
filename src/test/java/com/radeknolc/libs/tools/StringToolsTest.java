package com.radeknolc.libs.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StringToolsTest {

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    void isBlank(String text, boolean expected) {
        assertThat(StringTools.isBlank(text)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("", true),
                Arguments.of("   ", true),
                Arguments.of("abc", false)
        );
    }
}