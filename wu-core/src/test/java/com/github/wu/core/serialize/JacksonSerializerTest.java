package com.github.wu.core.serialize;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JacksonSerializerTest {

    Serializer jacksonSerializer = new JacksonSerializer();


    @Test
    void serialize() throws IOException {
        User user = new User();
        user.setId(1);
        user.setName("wu");
        byte[] serialize = jacksonSerializer.serialize(user);

    }

    @Test
    void deserialize() throws IOException {
        User user = new User();
        user.setId(1);
        user.setName("wu");
        byte[] serialize = jacksonSerializer.serialize(user);

        User deserialize = jacksonSerializer.deserialize(serialize, User.class);
        assertEquals(user, deserialize);
    }

    private static class User {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return id == user.id &&
                    Objects.equals(name, user.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, id);
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}