package com.growup.pms.test.support;

import static org.mockito.Mockito.mock;

import com.growup.pms.test.annotation.AutoServiceMockBeans;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

@Slf4j
public class ServiceMockBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        String basePackage = getBasePackage(importingClassMetadata);
        log.debug("Scanning package: {}", basePackage);
        registerMockBeans(createScanner(), basePackage, registry);
    }

    private String getBasePackage(AnnotationMetadata importingClassMetadata) {
        return (String) Objects.requireNonNull(
                importingClassMetadata.getAnnotationAttributes(AutoServiceMockBeans.class.getName(), false)).get("value");
    }

    private ClassPathScanningCandidateComponentProvider createScanner() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        return scanner;
    }

    private void registerMockBeans(ClassPathScanningCandidateComponentProvider scanner, String basePackage, BeanDefinitionRegistry registry) {
        scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
            String className = beanDefinition.getBeanClassName();
            try {
                Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());
                log.debug("Mocking service: {}", clazz.getSimpleName());
                ((ConfigurableListableBeanFactory) registry).registerSingleton(clazz.getSimpleName(), mock(clazz));
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Failed to load class: " + className, ex);
            }
        });
    }
}
