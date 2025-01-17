package com.growup.pms.common.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AopUtil {

    public static List<Parameter> extractAnnotatedParameters(JoinPoint joinPoint, Class<? extends Annotation> clazz) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        java.lang.reflect.Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        return IntStream.range(0, parameters.length)
                .filter(i -> parameters[i].isAnnotationPresent(clazz))
                .mapToObj(i -> new Parameter(parameters[i].getName(), args[i], parameters[i].getType()))
                .toList();
    }

    public static List<Parameter> filterAnnotatedParametersByType(JoinPoint joinPoint, Class<? extends Annotation> annotationClass, Class<?> parameterType) {
        return extractAnnotatedParameters(joinPoint, annotationClass).stream()
                .filter(param -> parameterType.isAssignableFrom(param.type))
                .toList();
    }

    /**
     * Finds the first method parameter annotated with a specific annotation and of a given type.
     *
     * @param joinPoint The join point representing the method execution
     * @param annotationClass The annotation class to search for on parameters
     * @param parameterType The expected type of the parameter
     * @param <T> The generic type of the parameter to be returned
     * @return An Optional containing the first parameter value matching the annotation and type, or an empty Optional if no match is found
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> findFirstAnnotatedParameterOfType(JoinPoint joinPoint, Class<? extends Annotation> annotationClass, Class<T> parameterType) {
        return filterAnnotatedParametersByType(joinPoint, annotationClass, parameterType).stream()
                .findFirst()
                .map(param -> (T) param.value);
    }

    /**
     * Finds and returns the first method parameter annotated with the specified annotation and of the given type, throwing an exception if no such parameter exists.
     *
     * @param joinPoint The join point representing the method execution
     * @param annotationClass The annotation class to search for on method parameters
     * @param parameterType The expected type of the parameter
     * @return The first parameter value matching the annotation and type
     * @throws IllegalStateException If no parameter is found with the specified annotation and type
     * @param <T> The type of the parameter to be retrieved
     */
    public static <T> T findFirstAnnotatedParameterOfTypeOrThrow(JoinPoint joinPoint, Class<? extends Annotation> annotationClass, Class<T> parameterType) {
        return findFirstAnnotatedParameterOfType(joinPoint, annotationClass, parameterType)
                .orElseThrow(() -> new IllegalStateException("'%s' 애노테이션이 붙은 '%s' 타입의 파라미터를 찾을 수 없습니다."
                        .formatted(annotationClass.getSimpleName(), parameterType.getSimpleName())));
    }

    public record Parameter(String name, Object value, Class<?> type) { }
}
