package com.github.wu.common.spi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultNameGeneratorTest {

    @Test
    void generate() {
        NameGenerator nameGenerator = new DefaultNameGenerator();
        String generate = nameGenerator.generate(DefaultNameGeneratorTest.class);
        assertEquals("defaultNameGeneratorTest", generate);
    }
}