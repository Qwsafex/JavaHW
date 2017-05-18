package xunit;

import annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class XUnit {
    @NotNull
    private final PrintWriter logWriter;

    public XUnit(@NotNull PrintWriter logWriter) {
        this.logWriter = logWriter;
    }
    public void test(@NotNull String className) throws IllegalTestClassException {
        logWriter.println("Testing class " + className + "...");
        Class<?> testedClass = getTestedClass(className);
        List<Method> beforeClass = getAnnotatedMethods(testedClass, XBeforeClass.class);
        assertStaticParameterless(beforeClass);
        List<Method> afterClass = getAnnotatedMethods(testedClass, XAfterClass.class);
        assertStaticParameterless(afterClass);
        List<Method> beforeMethods = getAnnotatedMethods(testedClass, XBefore.class);
        assertInstanceParameterless(beforeMethods);
        List<Method> afterMethods = getAnnotatedMethods(testedClass, XAfter.class);
        assertInstanceParameterless(afterMethods);
        List<Method> testMethods = getAnnotatedMethods(testedClass, XTest.class);
        assertInstanceParameterless(testMethods);

        Object testedClassInstance = getTestedInstance(testedClass);
        test(testedClassInstance, testMethods, beforeClass, afterClass, beforeMethods, afterMethods);
    }

    private void assertInstanceParameterless(@NotNull List<Method> methods) throws IllegalTestClassException {
        if (methods.stream().anyMatch(method -> Modifier.isStatic(method.getModifiers()))) {
            throw new IllegalTestClassException("XBefore/XAfter methods should not be static!");
        }
        assertParameterless(methods);
    }

    private void assertStaticParameterless(@NotNull List<Method> methods) throws IllegalTestClassException {
        if (methods.stream().anyMatch(method -> !Modifier.isStatic(method.getModifiers()))) {
            throw new IllegalTestClassException("XBefore/XAfter class methods should be static!");
        }
        assertParameterless(methods);
    }

    private void assertParameterless(@NotNull List<Method> methods) throws IllegalTestClassException {
        if (methods.stream().anyMatch(method -> method.getParameterCount() != 0)) {
            throw new IllegalTestClassException("XBefore/XAfter methods should not have parameters!");
        }
    }

    private void test(@NotNull Object testObject, @NotNull List<Method> testMethods,
                      @NotNull List<Method> beforeClass, @NotNull List<Method> afterClass,
                      @NotNull List<Method> beforeMethods, @NotNull List<Method> afterMethods) {
        logWriter.println("Running XBeforeClass methods...");
        executeStaticMethods(beforeClass);
        for (Method testMethod : testMethods) {
            logWriter.println("Running test " + testMethod.getName());
            final String ignoreReason = testMethod.getAnnotation(XTest.class).ignore();
            if (!ignoreReason.isEmpty()) {
                printIgnored(ignoreReason);
                continue;
            }
            if (!executeMethods(testObject, beforeMethods)) {
                logWriter.println("Failed to execute @XBefore methods");
                continue;
            }
            executeTestMethod(testObject, testMethod);
            if (!executeMethods(testObject, afterMethods)) {
                logWriter.println("Failed to execute @XAfter methods");
            }
        }
        logWriter.println("Running XAfterClass methods...");
        executeStaticMethods(afterClass);
    }


    private void executeTestMethod(@NotNull Object testObject, @NotNull Method testMethod) {
        Throwable caughtException = null;
        long startTime, finishTime;
        startTime = System.currentTimeMillis();
        try {
            testMethod.invoke(testObject);
        } catch (IllegalAccessException e) {
            logWriter.println("Method is private!");
            return;
        } catch (InvocationTargetException e) {
            caughtException = e.getCause();
        }
        finishTime = System.currentTimeMillis();
        double executionTime = (finishTime - startTime) / 1000;
        Class<? extends Throwable> expectedException = testMethod.getAnnotation(XTest.class).expected();
        if (expectedException.equals(XTest.NoException.class)) {
            if (caughtException == null) {
                printSuccess(executionTime);
            }
            else {
                printUnexpectedException(caughtException);
            }
        }
        else {
            if (caughtException == null) {
                printExpectedButNotThrown(expectedException);
            }
            else {
                if (expectedException.isAssignableFrom(caughtException.getClass())) {
                    printSuccess(executionTime);
                }
                else {
                    printExpectedButOtherThrown(expectedException, caughtException.getClass());
                }
            }
        }
    }

    void printIgnored(String ignoreReason) {
        logWriter.println("Ignored. Reason: " + ignoreReason);
    }
    void printExpectedButOtherThrown(@NotNull Class<? extends Throwable> expectedException,
                                             @NotNull Class<? extends Throwable> caughtException) {
        logWriter.println("XTest failed: expected " + expectedException +
                " but got " + caughtException);
    }

    void printExpectedButNotThrown(@NotNull Class<? extends Throwable> expectedException) {
        logWriter.println("XTest failed: expected " + expectedException);
    }

    void printUnexpectedException(@NotNull Throwable caughtException) {
        logWriter.println("XTest failed:");
        caughtException.printStackTrace(logWriter);
    }

    void printSuccess(double executionTime) {
        logWriter.println("XTest passed in " + executionTime + "s");
    }

    private boolean executeMethods(@Nullable Object testObject, @NotNull List<Method> methods) {
        for (Method method : methods) {
            try {
                method.invoke(testObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return false;
            }
        }
        return true;
    }

    private boolean executeStaticMethods(@NotNull List<Method> staticMethods) {
        return executeMethods(null, staticMethods);
    }

    private static <T extends Annotation> List<Method> getAnnotatedMethods(@NotNull Class<?> testedClass, @NotNull Class<T> annotation) {
        return Arrays.stream(testedClass.getMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    private Object getTestedInstance(@NotNull Class<?> testedClass) throws IllegalTestClassException {
        Constructor<?> constructor;
        try {
            constructor = testedClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalTestClassException("Class should have constructor with no parameters!");
        }
        try {
            return constructor.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalTestClassException("Class should not be abstract!");
        } catch (IllegalAccessException e) {
            throw new IllegalTestClassException("Class should have public constructor!");
        } catch (InvocationTargetException e) {
            throw new IllegalTestClassException("Constructor has thrown an exception: " + e.getMessage(), e);
        }
    }

    private Class<?> getTestedClass(@NotNull String className) throws IllegalTestClassException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalTestClassException();
        }
    }
}
