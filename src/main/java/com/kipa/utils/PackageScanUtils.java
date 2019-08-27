package com.kipa.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.common.KipaProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.SystemPropertyUtils;
import org.testng.collections.Sets;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Qinyadong
 * @date: 2019/4/19 17:38
 * 通过spring 包扫描实现自己的包扫描的工具类
 */
@Slf4j
public class PackageScanUtils {

    private PackageScanUtils() {}

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 结合spring的类扫描方式
     * 根据需要扫描的包路径及相应的注解，获取最终测method集合
     * 仅返回public方法，如果方法是非public类型的，不会被返回
     * 可以扫描工程下的class文件及jar中的class文件
     *
     * @param scanPackages
     * @param annotation
     * @return
     */
    public static Set<Method> findClassAnnotationMethods(String scanPackages, Class<? extends Annotation> annotation) {
        //获取所有的类
        Set<String> clazzSet = findPackageClass(scanPackages);
        Set<Method> methods = Sets.newHashSet();
        //遍历类，查询相应的annotation方法
        for (String clazz : clazzSet) {
            try {
                Set<Method> ms = findAnnotationMethods(clazz, annotation);
                if (ms != null) {
                    methods.addAll(ms);
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return methods;
    }

    /**
     * 根据扫描包的,查询下面的所有类
     *
     * @param scanPackages 扫描的package路径
     * @return
     */
    public static Set<String> findPackageClass(String scanPackages) {
        if (StringUtils.isBlank(scanPackages)) {
            return Collections.EMPTY_SET;
        }
        //验证及排重包路径,避免父子路径多次扫描
        Set<String> packages = checkPackage(scanPackages);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Set<String> clazzSet = Sets.newHashSet();
        for (String basePackage : packages) {
            if (StringUtils.isBlank(basePackage)) {
                continue;
            }
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    org.springframework.util.ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + "/" + DEFAULT_RESOURCE_PATTERN;
            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    //检查resource，这里的resource都是class
                    String clazz = loadClassName(metadataReaderFactory, resource);
                    clazzSet.add(clazz);
                }
            } catch (Exception e) {
                log.error("获取包下面的类信息失败,package:" + basePackage, e);
            }

        }
        return clazzSet;
    }

    /**
     * 排重、检测package父子关系，避免多次扫描
     *
     * @param scanPackages
     * @return 返回检查后有效的路径集合
     */
    private static Set<String> checkPackage(String scanPackages) {
        if (StringUtils.isBlank(scanPackages)) {
            return Collections.EMPTY_SET;
        }
        Set<String> packages = Sets.newHashSet();
        //排重路径
        Collections.addAll(packages, scanPackages.split(","));
        for (String pInArr : packages.toArray(new String[packages.size()])) {
            if (StringUtils.isBlank(pInArr) || pInArr.equals(".") || pInArr.startsWith(".")) {
                continue;
            }
            if (pInArr.endsWith(".")) {
                pInArr = pInArr.substring(0, pInArr.length() - 1);
            }
            Iterator<String> packageIte = packages.iterator();
            boolean needAdd = true;
            while (packageIte.hasNext()) {
                String pack = packageIte.next();
                if (pInArr.startsWith(pack + ".")) {
                    //如果待加入的路径是已经加入的pack的子集，不加入
                    needAdd = false;
                } else if (pack.startsWith(pInArr + ".")) {
                    //如果待加入的路径是已经加入的pack的父集，删除已加入的pack
                    packageIte.remove();
                }
            }
            if (needAdd) {
                packages.add(pInArr);
            }
        }
        return packages;
    }


    /**
     * 加载资源，根据resource获取className
     *
     * @param metadataReaderFactory spring中用来读取resource为class的工具
     * @param resource              这里的资源就是一个Class
     * @throws IOException
     */
    private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource) throws IOException {
        try {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                return metadataReader.getClassMetadata().getClassName();
            }
        } catch (Exception e) {
            log.error("根据resource获取类名称失败", e);
        }
        return null;
    }

    /**
     * 把action下面的所有method遍历一次，标记他们是否需要进行敏感词验证
     * 如果需要，放入cache中
     *
     * @param fullClassName
     */
    public static Set<Method> findAnnotationMethods(String fullClassName, Class<? extends Annotation> anno) throws ClassNotFoundException {
        Set<Method> methodSet = Sets.newHashSet();
        Class<?> clz = Class.forName(fullClassName);
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            Annotation annotation = method.getAnnotation(anno);
            if (annotation != null) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }


    /**
     * 扫描单个注解
     * @param basePackage
     * @param annotationType
     * @return
     */
    public static Set<BeanDefinition> getBeanWithAnnotation(String basePackage, Class<? extends Annotation> annotationType) {
        // 不使用默认的TypeFilter
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        return provider.findCandidateComponents(basePackage);
    }

    /**
     * 扫描多个注解
     * @param basePackage
     * @param annotationTypeList
     * @return
     */
    public static Set<BeanDefinition> getBeanWithAnnotationSet(String basePackage, List<Class<? extends Annotation>> annotationTypeList) {
        // 不使用默认的TypeFilter
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        if (CollectionUtils.isNotEmpty(annotationTypeList)) {
            annotationTypeList.forEach(annotationType -> provider.addIncludeFilter(new AnnotationTypeFilter(annotationType)));
        }
        return provider.findCandidateComponents(basePackage);
    }

    public static Set<Method> getMethodWithAnnotation(String packageName, Class<? extends Annotation> annotationType) {
        PreCheckUtils.checkEmpty(packageName, "目录不能为空");
        Reflections reflections = new Reflections(packageName, new MethodAnnotationsScanner());
        return reflections.getMethodsAnnotatedWith(annotationType);
    }

    public static <T extends Annotation> Map<Method, T > getMethodWithAnnotationMap(Class<?> clazz, Class<T> annotationType) {
        Map<Method,T> methodMap = Maps.newHashMap();
        Method[] methods = clazz.getMethods();
        if (ArrayUtils.isNotEmpty(methods)) {
            Arrays.stream(methods).forEach(method -> {
                T annotation = method.getAnnotation(annotationType);
                if (annotation != null) {
                    methodMap.put(method, annotation);
                }
            });
        }
        return methodMap;
    }

    public static Set<Class<?>> getClassWithAnnotationSet(String packageName, Class<? extends Annotation> annotationType) {
        PreCheckUtils.checkEmpty(packageName, "目录不能为空");
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner(),new SubTypesScanner());
        return reflections.getTypesAnnotatedWith(annotationType);
    }



    /**
     * 扫描单一的注解
     * @param basePackage
     * @param annotationType
     * @return
     */
    private static Map<String, Object> getAnnotationAttributes(String basePackage, Class<? extends Annotation> annotationType) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(basePackage)) {
            Set<BeanDefinition> beanWithAnnotation = getBeanWithAnnotation(basePackage, annotationType);
            if (CollectionUtils.isNotEmpty(beanWithAnnotation)) {
                beanWithAnnotation.forEach(beanDefinition -> {
                    if (beanDefinition instanceof ScannedGenericBeanDefinition) {
                        ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
                        AnnotationMetadata metadata = scannedGenericBeanDefinition.getMetadata();
                        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(annotationType.getName());
                        if (annotationAttributes != null) {
                            map.putAll(annotationAttributes);
                        }
                    }
                });
            }
        }
        return map;
    }


    /**
     * 包扫描子类信息 2.2.0
     * @param basePackage
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Set<Class<? extends T>> getClassOfType(String basePackage, Class<T> clazz) {
        PreCheckUtils.checkEmpty(basePackage, "目录不能为空");
        Reflections reflections = new Reflections(basePackage, new SubTypesScanner());
        return reflections.getSubTypesOf(clazz);
    }




    public static List<String> getAllPackageName(String selectPackage) {
        List<String> packageNameList = Lists.newArrayList();

        PreCheckUtils.checkEmpty(selectPackage, "扫描的包路径不能为空");
        String selectPackagePath = selectPackage.replace(".", "/");
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = contextClassLoader.getResources(selectPackagePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url != null) {
                    String path = url.getPath();
                    File file = new File(path);
                    List<String> packageName = getPackageName(file, file.getPath(), selectPackage);
                    packageNameList.addAll(packageName);
                }
            }
        } catch (IOException e) {
            throw new KipaProcessException("没扫描到信息");
        }
        return packageNameList;
    }

    private static List<String> getPackageName(File file, String separator, String selectPackage) {
        List<String> list = Lists.newArrayList();
        if (!file.exists()) {
            return list;
        }
        Collection<File> files = FileUtils.listFilesAndDirs(file, FileFilterUtils.directoryFileFilter(), DirectoryFileFilter.INSTANCE);
        if (CollectionUtils.isEmpty(files)) {
            return list;
        }

        return files.stream().map(fileTarget -> {
            String path = fileTarget.getPath();
            String packagePath = StringUtils.substringAfter(path, separator);
            String replace = packagePath.replace(File.separator, ".");
            return selectPackage.concat(replace);
        }).collect(Collectors.toList());
    }
}
