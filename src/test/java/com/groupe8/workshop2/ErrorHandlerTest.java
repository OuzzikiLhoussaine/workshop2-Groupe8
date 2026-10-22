package com.groupe8.workshop2;

import com.groupe8.workshop2.web.ErrorHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void renderErrorPage_doesNotExposeExceptionDetails() {
        String rendered = errorHandler.renderErrorPage(
                new IllegalStateException("database password leaked"));

        assertTrue(rendered.contains("Une erreur interne est survenue."));
        assertFalse(rendered.contains("database password leaked"));
        assertFalse(rendered.contains("<pre>"));
    }

    @Test
    void saveData_handlesInvalidDataWithoutExposingAnException() {
        errorHandler.saveData(null);
    }
}
