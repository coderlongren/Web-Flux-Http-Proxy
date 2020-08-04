package com.coderlong.webflux.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;

import com.coderlong.webflux.annotation.ApiServer;
import com.coderlong.webflux.annotation.ApiServerScan;
import com.coderlong.webflux.utils.ProxyUtil;

/**
 * @author sailongren
 */
@Configuration
public class WebFluxHttpAutoConfiguration
        implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebFluxHttpAutoConfiguration.class);
    /**
     * beanFactory
     */
    private BeanFactory beanFactory;

    /**
     * 资源加载
     */
    private ResourceLoader resourceLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        LOGGER.info("start scan self package bean......");
        ClassPathBeanDefinitionScanner scanner = new ClassPathApiServerScanner(registry);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ApiServer.class));
        Set<BeanDefinition> beanDefinitions = scanPackages(importingClassMetadata, scanner);
        ProxyUtil.registerBeans(beanFactory, beanDefinitions);
    }

    /**
     * 包扫描
     */
    private Set<BeanDefinition> scanPackages(AnnotationMetadata importingClassMetadata,
            ClassPathBeanDefinitionScanner scanner) {
        List<String> packages = new ArrayList<>();
        Map<String, Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(ApiServerScan.class.getCanonicalName());
        if (annotationAttributes != null) {
            String[] basePackages = (String[]) annotationAttributes.get("packages");
            if (basePackages.length > 0) {
                packages.addAll(Arrays.asList(basePackages));
            }
        }
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        if (CollectionUtils.isEmpty(packages)) {
            return beanDefinitions;
        }
        packages.forEach(pack -> beanDefinitions.addAll(scanner.findCandidateComponents(pack)));
        return beanDefinitions;
    }

    protected static class ClassPathApiServerScanner extends ClassPathBeanDefinitionScanner {

        ClassPathApiServerScanner(BeanDefinitionRegistry registry) {
            super(registry, false);
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
        }

    }

}
