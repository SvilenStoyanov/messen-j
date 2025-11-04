package com.svistoyanov.mj.docker.utils;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;

public record IntegrationTestValueDescriptor<AnnotationType extends Annotation, ValueType>(
        Class<AnnotationType> annotationType,
        Class<ValueType> valueType,
        BiFunction<? super IntegrationTestEnvironment, ? super AnnotationType, ? extends ValueType> valueGetter
) {
}
