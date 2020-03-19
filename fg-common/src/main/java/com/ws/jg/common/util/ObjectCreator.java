package com.ws.jg.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utiity class to create new object.
 * 
 * @author schanda
 */
public class ObjectCreator {
    
    /**
     * Create and return the instance of the class designated by this className.
     * It uses the no-argument constructor (default one) while instantiating
     * the object (provided the no-argument constructor is defined).
     * 
     * @param   <T>
     * @param   className           Class name
     * 
     * @return  T                   New Instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(String className) {
        try {
            Class<?> clazz = Class.forName(className.trim());
            return create(clazz);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Create and return the instance of the class designated by this className.
     * It uses the no-argument constructor (default one) while instantiating
     * the object (provided the no-argument constructor is defined).
     * 
     * @param   <T>
     * @param   clazz               Class instance
     * 
     * @return  T                   New Instance
     * @throws  RuntimeException    If any error occurs during creation
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Class clazz) throws RuntimeException {
        return create(clazz, new Class[] {}, new Object[] {});
    }
    
    /**
     * Create and return the instance of the class designated by this className.
     * It uses the specific parameterized constructor while instantiating the class
     * instance.
     * 
     * @param   <T>
     * @param   className           Class name
     * @param   paramTypes          Constructor Parameter types
     * @param   params              Constructor Parameter names
     * 
     * @return  T                   New Instance
     * @throws  RuntimeException    If any error occurs during creation
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(String className
                               , Class[] paramTypes
                               , Object[] params) throws RuntimeException {
        try {
            Class<?> clazz = Class.forName(className.trim());
            return create(clazz, paramTypes, params);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Create and return the instance of the class designated by this className.
     * It uses the specific parameterized constructor while instantiating the class
     * instance.
     * 
     * @param   <T>
     * @param   clazz               Class instance
     * @param   paramTypes          Constructor Parameter types
     * @param   params              Constructor Parameter names
     * 
     * @return  T                   New Instance
     * @throws  RuntimeException    If any error occurs during creation
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<?> clazz
                               , Class[] paramTypes
                               , Object[] params) throws RuntimeException {
        try {
            Constructor<T> constructor = (Constructor<T>)clazz.getDeclaredConstructor(paramTypes);
            T obj = constructor.newInstance(params);
            return obj;
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Create and return the instance of the class designated by this className.
     * It uses a static accessor or factory method for instantiating.
     * 
     * @param   <T>
     * @param   className           Class name
     * @param   factoryMethod       Factory/accessor method name (static)
     * @param   paramTypes          Parameter types
     * @param   params              Parameter values
     * 
     * @return  T                   New Instance
     * @throws  RuntimeException    If any error occurs during creation
     */
    @SuppressWarnings("unchecked")
    public static <T> T createStatic(String className, String factoryMethod
            , Class[] paramTypes, Object[] params) throws RuntimeException {
        
        try {
            Class<?> clazz = Class.forName(className.trim());
            Method method = clazz.getDeclaredMethod(factoryMethod, paramTypes);
            return (T)method.invoke(null, params);
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

