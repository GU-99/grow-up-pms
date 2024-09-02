package com.growup.pms.common.util;

import java.lang.annotation.Annotation;
import java.util.List;
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

    @SuppressWarnings("unchecked")
    public static <T> T findFirstAnnotatedParameterOfType(JoinPoint joinPoint, Class<? extends Annotation> annotationClass, Class<T> parameterType) {
        return filterAnnotatedParametersByType(joinPoint, annotationClass, parameterType).stream()
                .findFirst()
                .map(param -> (T) param.value)
                .orElseThrow(() -> new IllegalStateException("'%s' 애노테이션이 붙은 '%s' 타입의 파라미터를 찾을 수 없습니다."
                        .formatted(annotationClass.getSimpleName(), parameterType.getSimpleName())));
    }

    public record Parameter(String name, Object value, Class<?> type) { }
}
