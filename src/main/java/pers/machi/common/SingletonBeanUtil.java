package pers.machi.common;

import java.util.HashMap;

public class SingletonBeanUtil {
    private static HashMap<Class<?>, Object> SingletonBeans = new HashMap<Class<?>, Object>();

    private static SingletonBeanUtil instance = new SingletonBeanUtil();

    public synchronized boolean hasBean(Class<?> klass) {
        return SingletonBeans.containsKey(klass);
    }

    public synchronized void putBean(Class<?> klass) throws InstantiationException, IllegalAccessException {
        if (!hasBean(klass)) {
            SingletonBeans.put(klass, klass.newInstance());
        }
    }

    public synchronized Object getBean(Class<?> klass) {
       return  SingletonBeans.get(klass);
    }

    public static SingletonBeanUtil getInstance() {
        return instance;
    }
}
