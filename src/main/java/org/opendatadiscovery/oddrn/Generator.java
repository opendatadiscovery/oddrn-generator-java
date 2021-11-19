package org.opendatadiscovery.oddrn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.opendatadiscovery.oddrn.annotation.PathField;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.exception.PathDoesntExistException;
import org.opendatadiscovery.oddrn.model.OddrnPath;

import static java.util.Locale.ENGLISH;

public class Generator {

    static final String GET_PREFIX = "get";

    private final Map<Class<? extends OddrnPath>, Map<String, ModelField>> cache =
        new ConcurrentHashMap<>();

    public static String capitalize(final String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    public String generate(final OddrnPath model, final String field)
            throws PathDoesntExistException, IllegalAccessException,
            InvocationTargetException, EmptyPathValueException {

        validatePath(model, field);

        final Map<String, ModelField> fields = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );

        final Optional<ModelField> first = Optional.ofNullable(fields.get(field));

        if (first.isPresent()) {
            final List<ModelField> pathFields = new ArrayList<>();

            ModelField currentField = first.get();
            pathFields.add(currentField);

            while (!currentField.pathField.dependency().isEmpty()) {
                final String dependence = currentField.pathField.dependency();
                final Optional<ModelField> find = Optional.ofNullable(fields.get(dependence));

                if (find.isPresent()) {
                    currentField = find.get();
                    pathFields.add(currentField);
                } else {
                    throw new PathDoesntExistException(
                        String.format("Path %s doesn't exist in generator", dependence)
                    );
                }
            }

            Collections.reverse(pathFields);
            final StringBuilder builder = new StringBuilder();
            builder.append(model.prefix());
            for (final ModelField modelField : pathFields) {
                final String prefix = modelField.pathField.prefix().isEmpty()
                    ? modelField.getField().getName()
                    : modelField.pathField.prefix();

                builder.append("/");
                builder.append(prefix);
                builder.append("/");
                builder.append(modelField.readMethod.invoke(model));
            }

            return builder.toString();
        } else {
            throw new PathDoesntExistException(
                String.format("Path %s doesn't exist in generator", field)
            );
        }
    }

    public void validateAllPaths(final OddrnPath model)
            throws PathDoesntExistException, IllegalAccessException,
            InvocationTargetException, EmptyPathValueException {
        final Map<String, ModelField> fields = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );
        for (final ModelField field : fields.values()) {
            this.validatePath(model, field.field.getName());
        }
    }

    public void validatePath(final OddrnPath model, final String field)
            throws IllegalAccessException, PathDoesntExistException,
            InvocationTargetException, EmptyPathValueException {
        final Map<String, ModelField> fields = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );

        final Optional<ModelField> first = Optional.ofNullable(fields.get(field));

        if (first.isPresent()) {
            final ModelField modelField = first.get();
            final String fieldName = modelField.field.getName();

            if (!modelField.pathField.dependency().isEmpty()) {
                validatePath(model, modelField.pathField.dependency());
            }

            if (!modelField.pathField.nullable()
                && (
                modelField.getReadMethod().invoke(model) == null
                    || modelField.getReadMethod().invoke(model).toString().isEmpty()
                )
            ) {
                throw new EmptyPathValueException(
                    String.format("'Attribute %s' is empty",
                        fieldName
                    )
                );
            }
        } else {
            throw new PathDoesntExistException(
                String.format("Path %s doesn't exist in generator", field)
            );
        }
    }

    @SneakyThrows
    private Map<String, ModelField> generateModel(final Class<? extends OddrnPath> clazz) {
        final Map<String, ModelField> fields = new HashMap<>();

        Class<?> currentClazz = clazz;

        while (OddrnPath.class.isAssignableFrom(currentClazz)) {
            for (final Field field : currentClazz.getDeclaredFields()) {
                final PathField[] pathFields = field.getAnnotationsByType(PathField.class);
                final Method getMethod = clazz.getMethod(GET_PREFIX + capitalize(field.getName()));
                if (pathFields.length > 0) {
                    fields.put(
                        field.getName(),
                        ModelField.builder()
                            .field(field)
                            .pathField(pathFields[0])
                            .readMethod(getMethod)
                            .build()
                    );
                }
            }
            currentClazz = currentClazz.getSuperclass();
        }

        return fields;
    }

    @Data
    @Builder
    private static class ModelField {
        private final Field field;
        private final Method readMethod;
        private final PathField pathField;
    }
}
