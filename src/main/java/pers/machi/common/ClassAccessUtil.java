package pers.machi.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// 从指定的package中加载类 并按照一定规则过滤
public class ClassAccessUtil {
    private final static Logger logger = LogManager.getLogger(ClassAccessUtil.class);

    public static Set<Class<?>> accessingClassesInPackageWithFilter(String packageName, Predicate<Class<?>> predicate) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClassInPackage(line, packageName))
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> accessingClassesInPackage(String packageName) {
        return accessingClassesInPackageWithFilter(packageName, e -> true);
    }


    private static Class<?> getClassInPackage(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        return null;
    }
}
