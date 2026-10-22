package com.groupe8.workshop2;

import com.groupe8.workshop2.web.CommandRunner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandRunnerTest {

    private final CommandRunner commandRunner = new CommandRunner();

    @Test
    void pingHost_rejectsCommandInjection() {
        assertThrows(IllegalArgumentException.class,
                () -> commandRunner.pingHost("127.0.0.1; whoami"));
    }

    @Test
    void pingHost_rejectsNullHost() {
        assertThrows(IllegalArgumentException.class,
                () -> commandRunner.pingHost(null));
    }
}
