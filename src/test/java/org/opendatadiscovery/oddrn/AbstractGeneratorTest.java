package org.opendatadiscovery.oddrn;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.exception.PathDoesntExistException;
import org.opendatadiscovery.oddrn.exception.WrongPathOrderException;
import org.opendatadiscovery.oddrn.model.OddrnPath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbstractGeneratorTest {

    private final Generator generator = new Generator();

    public void shouldGeneratePath(final OddrnPath path, final String field, final String expected)
            throws PathDoesntExistException, WrongPathOrderException, IllegalAccessException,
            InvocationTargetException, EmptyPathValueException, NoSuchMethodException {
        final String oddrn = generator.generate(path, field);
        assertEquals(expected, oddrn);
        final Optional<OddrnPath> parse = generator.parse(oddrn);
        assertNotNull(parse);
        assertEquals(path, parse.get());
    }

    public void shouldFail(final OddrnPath path, final String field, final Class<? extends Exception> exception) {
        final Exception thrown = assertThrows(exception, () ->
            generator.generate(path, field)
        );
    }
}
