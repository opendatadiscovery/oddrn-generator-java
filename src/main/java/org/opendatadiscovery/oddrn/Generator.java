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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.opendatadiscovery.oddrn.annotation.PathField;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.exception.PathDoesntExistException;
import org.opendatadiscovery.oddrn.model.AirflowPath;
import org.opendatadiscovery.oddrn.model.DynamodbPath;
import org.opendatadiscovery.oddrn.model.GrpcServicePath;
import org.opendatadiscovery.oddrn.model.HivePath;
import org.opendatadiscovery.oddrn.model.KafkaConnectorPath;
import org.opendatadiscovery.oddrn.model.KafkaPath;
import org.opendatadiscovery.oddrn.model.MysqlPath;
import org.opendatadiscovery.oddrn.model.ODDPlatformDataSourcePath;
import org.opendatadiscovery.oddrn.model.OddrnPath;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;
import org.opendatadiscovery.oddrn.model.SnowflakePath;
import org.opendatadiscovery.oddrn.model.SparkPath;
import org.opendatadiscovery.oddrn.util.GeneratorUtil;

import static java.util.Locale.ENGLISH;
import static java.util.function.Function.identity;

public class Generator {
    static final String GET_PREFIX = "get";

    private static final Map<Class<?>, Function<String, ?>> RETURN_TYPE_MAPPING = Map.of(
        String.class, identity(),
        Integer.class, Integer::parseInt,
        Long.class, Long::parseLong,
        Double.class, Double::parseDouble,
        Float.class, Float::parseFloat
    );

    private final Map<Class<? extends OddrnPath>, ModelDescription> cache =
        Stream.of(
            AirflowPath.class,
            DynamodbPath.class,
            GrpcServicePath.class,
            HivePath.class,
            KafkaConnectorPath.class,
            KafkaPath.class,
            MysqlPath.class,
            PostgreSqlPath.class,
            SnowflakePath.class,
            SparkPath.class,
            ODDPlatformDataSourcePath.class
        ).collect(
            Collectors.toMap(
                c -> c,
                this::generateModel
            )
        );

    public static String capitalize(final String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    public Optional<OddrnPath> parse(final String oddrn)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Optional<OddrnPath> result = Optional.empty();

        for (final ModelDescription description : this.cache.values()) {
            if (oddrn.startsWith(description.prefix+"/")) {
                final String withoutPrefix = oddrn.substring(description.prefix.length());
                final Object builder = description.builderMethod.invoke(null);
                int nextFieldPos = 0;

                do {
                    final int fieldNamePos = withoutPrefix.indexOf("/", nextFieldPos);
                    final int valuePos = withoutPrefix.indexOf("/", fieldNamePos + 1);
                    nextFieldPos = withoutPrefix.indexOf("/", valuePos + 1);

                    if (fieldNamePos >= 0 && valuePos >= 0) {
                        final String fieldName = withoutPrefix.substring(fieldNamePos + 1, valuePos);
                        final String stringValue;
                        if (nextFieldPos > 0) {
                            stringValue = withoutPrefix.substring(valuePos + 1, nextFieldPos);
                        } else {
                            stringValue = withoutPrefix.substring(valuePos + 1);
                        }
                        final ModelField modelField = description.prefixes.get(fieldName);

                        if (modelField != null) {
                            final Class<?> returnType = modelField.getReadMethod().getReturnType();

                            final Function<String, ?> mapper = RETURN_TYPE_MAPPING.get(returnType);

                            if (mapper == null) {
                                throw new IllegalArgumentException(
                                    String.format("Field path of type %s is not supported", returnType));
                            }

                            modelField.setMethod.invoke(builder, mapper.apply(GeneratorUtil.unescape(stringValue)));
                        }
                    }
                } while (nextFieldPos >= 0);

                return Optional.ofNullable(
                    (OddrnPath) builder.getClass().getMethod("build").invoke(builder)
                );
            }
        }

        return result;
    }

    public String generate(final OddrnPath model, final String field)
            throws PathDoesntExistException, IllegalAccessException,
            InvocationTargetException, EmptyPathValueException {

        validatePath(model, field);

        final ModelDescription modelDescription = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );

        final Map<String, ModelField> fields = modelDescription.fields;

        final Optional<ModelField> first = Optional.ofNullable(fields.get(field));

        if (first.isPresent()) {
            final List<ModelField> pathFields = new ArrayList<>();

            ModelField currentField = first.get();
            pathFields.add(currentField);

            while (currentField.pathField.dependency().length > 0
                && !currentField.pathField.dependency()[0].isEmpty()
            ) {
                boolean allFailed = false;

                for (final String dependency : currentField.pathField.dependency()) {
                    if (!dependency.isEmpty()) {
                        final Optional<ModelField> find = Optional.ofNullable(fields.get(dependency));

                        if (find.isPresent()) {
                            currentField = find.get();
                            pathFields.add(currentField);
                            allFailed = false;
                            break;
                        } else {
                            allFailed = true;
                        }
                    }
                }

                if (allFailed) {
                    throw new PathDoesntExistException(
                        String.format("Path %s doesn't exist in generator",
                            String.join(" ,", currentField.pathField.dependency())
                        )
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
                builder.append(GeneratorUtil.escape(modelField.readMethod.invoke(model).toString()));
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
        final ModelDescription modelDescription = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );

        for (final ModelField field : modelDescription.fields.values()) {
            this.validatePath(model, field.field.getName());
        }
    }

    public void validatePath(final OddrnPath model, final String field)
            throws IllegalAccessException, PathDoesntExistException,
            InvocationTargetException, EmptyPathValueException {
        final ModelDescription description = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );

        final Optional<ModelField> first = Optional.ofNullable(description.fields.get(field));

        if (first.isPresent()) {
            final ModelField modelField = first.get();
            final String fieldName = modelField.field.getName();

            if (modelField.pathField.dependency().length > 0) {
                boolean allFailed = false;

                PathDoesntExistException lastPathException = null;
                EmptyPathValueException lastEmptyException = null;

                for (final String dependency : modelField.pathField.dependency()) {
                    if (!dependency.isEmpty()) {
                        try {
                            validatePath(model, dependency);
                            allFailed = false;
                            break;
                        } catch (PathDoesntExistException e) {
                            allFailed = true;
                            lastPathException = e;
                        } catch (EmptyPathValueException e) {
                            allFailed = true;
                            lastEmptyException = e;
                        }
                    }
                }

                if (allFailed) {
                    if (lastPathException != null) {
                        throw lastPathException;
                    }
                    if (lastEmptyException != null) {
                        throw lastEmptyException;
                    }
                }
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
    private ModelDescription generateModel(final Class<? extends OddrnPath> clazz) {
        final ModelDescription.ModelDescriptionBuilder descriptionBuilder = ModelDescription.builder();

        final Map<String, ModelField> fields = new HashMap<>();
        final Map<String, ModelField> prefixes = new HashMap<>();

        Class<?> currentClazz = clazz;
        final Class<?> builderClazz = clazz.getMethod("builder").getReturnType();
        final Method builderMethod = clazz.getMethod("builder");
        final Object builder = builderMethod.invoke(null);
        final Object build = builder.getClass().getMethod("build").invoke(builder);
        final String prefix = build.getClass().getMethod("prefix").invoke(build).toString();

        descriptionBuilder.prefix(prefix);
        descriptionBuilder.builderMethod(clazz.getMethod("builder"));

        while (OddrnPath.class.isAssignableFrom(currentClazz)) {
            for (final Field field : currentClazz.getDeclaredFields()) {
                final PathField[] pathFields = field.getAnnotationsByType(PathField.class);
                final Method getMethod = clazz.getMethod(GET_PREFIX + capitalize(field.getName()));
                final Method setMethod = builderClazz.getMethod(field.getName(), getMethod.getReturnType());

                final ModelField model = ModelField.builder()
                    .field(field)
                    .pathField(pathFields[0])
                    .readMethod(getMethod)
                    .setMethod(setMethod)
                    .build();

                if (pathFields.length > 0) {
                    fields.put(field.getName(), model);
                    prefixes.put(
                        pathFields[0].prefix().isEmpty() ? field.getName() : pathFields[0].prefix(),
                        model
                    );
                }
            }

            currentClazz = currentClazz.getSuperclass();
        }

        descriptionBuilder.prefixes(prefixes);
        descriptionBuilder.fields(fields);
        return descriptionBuilder.build();
    }

    @Data
    @Builder
    private static class ModelDescription {
        private final Map<String, ModelField> fields;
        private final Map<String, ModelField> prefixes;
        private final String prefix;
        private final Method builderMethod;
    }

    @Data
    @Builder
    private static class ModelField {
        private final Field field;
        private final Method readMethod;
        private final Method setMethod;
        private final PathField pathField;
    }
}
