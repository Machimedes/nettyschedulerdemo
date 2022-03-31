package pers.machi.urimapper;

import pers.machi.common.ClassAccessUtil;
import pers.machi.common.SingletonBeanUtil;
import pers.machi.urimapper.annotation.UriMapper;
import pers.machi.urimapper.annotation.UriMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;

import static pers.machi.common.MethodAccessUtil.getMethodsAnnotatedWith;

/**
 * While applicaton is stared, all UriMapper class should be injected into UriMapperManagement.
 * This class manage all UriMapper's singleton instance, methods and corresponding uri.
 */
public class UriMapperManagement {

    private static UriMapperManagement instance;
    public static Set<Class<?>> uriMappingClass;
    public HashMap<String, Method> uri2Method = new HashMap<>();

    static {
        try {
            instance = new UriMapperManagement();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private UriMapperManagement() throws InstantiationException, IllegalAccessException {
        Predicate<Class<?>> predicate = e -> e.isAnnotationPresent(UriMapper.class);
        uriMappingClass = ClassAccessUtil
                .accessingClassesInPackageWithFilter("pers.machi.urimapper", predicate);

        for (Class klass : uriMappingClass) {
            SingletonBeanUtil.getInstance().putBean(klass);
            for (Method method : getMethodsAnnotatedWith(klass, UriMethod.class)) {
                uri2Method.put(method.getAnnotation(UriMethod.class).uri(), method);
            }
        }
    }

    public static UriMapperManagement getInstance() {
        return instance;
    }
}


