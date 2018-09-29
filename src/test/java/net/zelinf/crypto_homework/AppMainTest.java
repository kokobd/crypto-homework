package net.zelinf.crypto_homework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ServiceLoader;

class AppMainTest {

    @Test
    void testExerciseFetch() {
        ServiceLoader<Exercise> loader = ServiceLoader.load(Exercise.class);
        assertTrue(loader.iterator().hasNext());
    }
}
