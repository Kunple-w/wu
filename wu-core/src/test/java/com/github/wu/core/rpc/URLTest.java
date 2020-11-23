package com.github.wu.core.rpc;

import com.github.wu.common.URL;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class URLTest {

    private String wu = "wu://localhost:18080/com.github.wu.executor?limit=10&timeout=100s";
    private String seng2 = "wu://admin:Hello1234@localhost:18080/com.github.wu.executor?limit=10&timeout=100s";

    @Test
    void getURL() {
        URL of = URL.of(wu);
        Map<String, String> param = new HashMap<>();
        URL url = new URL("wu", "localhost", 18080, "com.github.wu.executor");
        assertEquals(of, url);
    }

    @Test
    void testOf2() {
        URL of = URL.of(seng2);
        URL url = new URL("wu", "localhost", 18080, "admin", "Hello1234", "com.github.wu.executor");
        assertEquals("admin", of.getUsername());
        assertEquals("Hello1234", of.getPassword());
        assertEquals(of, url);
    }

    @Test
    void testOf3() {
        String str = "zookeeper://localhost:2181";
        URL of = URL.of(str);
        URL url = new URL("zookeeper", "localhost", 2181, "/");
        assertEquals(of, url);
    }
}