package com.svistoyanov.mj.docker.utils;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractIntegrationTestExtension implements BeforeEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE                 = ExtensionContext.Namespace.create(AbstractIntegrationTestExtension.class);
    private static final String                     ENVIRONMENT_KEY           = "environment";
    private static final String                     ENVIRONMENT_EXCEPTION_KEY = "environmentException";

    private final Function<EnvironmentConfig, IntegrationTestEnvironment> environmentSupplier;

    protected AbstractIntegrationTestExtension(Function<EnvironmentConfig, IntegrationTestEnvironment> environmentSupplier) {
        this.environmentSupplier = environmentSupplier;
    }

    protected static IntegrationTestEnvironment getOrCreateTestEnvironment(
            ExtensionContext ctx,
            Function<EnvironmentConfig, IntegrationTestEnvironment> environmentSupplier
    ) {
        final ExtensionContext.Store store = getRootStore(ctx);
        final TestEnvironmentStoreValue envStoreValue = store.get(ENVIRONMENT_KEY, TestEnvironmentStoreValue.class);
        if (envStoreValue != null) {
            return envStoreValue.environment();
        }

        Throwable environmentException = store.get(ENVIRONMENT_EXCEPTION_KEY, Throwable.class);
        if (environmentException != null) {
            throw new RuntimeException(environmentException);
        }

        IntegrationTestEnvironment environment = null;
        try {
            environment = environmentSupplier.apply(new EnvConfigProviderImpl(ctx));
            environment.bootstrap();
            store.put(ENVIRONMENT_KEY, new TestEnvironmentStoreValue(environment));
            return environment;
        } catch (Throwable th) {
            if (environment != null) {
                environment.shutdown();
            }
            store.put(ENVIRONMENT_EXCEPTION_KEY, th);
            throw th;
        }
    }

    protected static ExtensionContext.Store getRootStore(ExtensionContext ctx) {
        return getStore(ctx.getRoot());
    }

    protected static ExtensionContext.Store getStore(ExtensionContext ctx) {
        return ctx.getStore(NAMESPACE);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        final Optional<TestInstances> testInstances = context.getTestInstances();
        if (testInstances.isEmpty()) {
            return;
        }

        final IntegrationTestEnvironment environment = getOrCreateTestEnvironment(context, environmentSupplier);
        environment.resetDatabaseData();

        for (Object testInstance : testInstances.get().getAllInstances()) {
            postProcessTestInstance(testInstance, environment);
        }
    }

    public void postProcessTestInstance(Object testInstance, IntegrationTestEnvironment environment) {
        List<Field> fields = ReflectionUtils.findFields(
                testInstance.getClass(),
                f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()),
                ReflectionUtils.HierarchyTraversalMode.BOTTOM_UP
        );

        for (Field field : fields) {
            for (IntegrationTestValueDescriptor<?, ?> descriptor : getTestValueDescriptors()) {
                processField(field, testInstance, environment, descriptor);
            }
        }
    }

    private <A extends Annotation, V> void processField(
            Field field,
            Object instance,
            IntegrationTestEnvironment testEnvironment,
            IntegrationTestValueDescriptor<A, V> descriptor) {
        A annotation = field.getAnnotation(descriptor.annotationType());
        if (annotation == null) {
            return;
        }

        Class<?> fieldType = field.getType();
        Class<?> valueType = descriptor.valueType();

        if (!fieldType.isAssignableFrom(valueType)) {
            String message = String
                    .format("Could not inject value at %s#%s . Expected value of type [%s] but was [%s]",
                            field.getDeclaringClass().getName(),
                            field.getName(),
                            fieldType.getName(),
                            valueType.getName());
            throw new RuntimeException(message);
        }

        boolean accessible = field.canAccess(instance);
        if (!accessible && !field.trySetAccessible()) {
            String message = String.format("Could not inject value at %s#%s . Field is not accessible!",
                    instance.getClass().getName(),
                    field.getName());
            throw new RuntimeException(message);
        }

        try {
            Object value = descriptor.valueGetter().apply(testEnvironment, annotation);
            field.set(instance, value);
        } catch (IllegalArgumentException e) {
            // Already checked for type safety. Will not be thrown!
        } catch (IllegalAccessException e) {
            // Already checked for access. Will not be thrown!
        } finally {
            if (!accessible) {
                field.setAccessible(accessible);
            }
        }
    }

    public abstract List<IntegrationTestValueDescriptor<?, ?>> getTestValueDescriptors();
}
