package com.baoge.Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;


public class BeanUtil extends BeanUtils {

    public static void copyProperties(Object source, Object target, List<String> ignores) {
        if (source == null) {
            target = null;
            return;
        }

        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if ((!ignores.contains(targetPd.getName())) &&
                    (targetPd.getWriteMethod() != null)) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();

                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source, new Object[0]);

                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, new Object[]{value});
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

    public static void copyProperties(Object source, Object target, String... ignores) {
        if (source == null) {
            target = null;
            return;
        }

        List<String> _ignores = Arrays.asList(ignores);

        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if ((!_ignores.contains(targetPd.getName())) &&
                    (targetPd.getWriteMethod() != null)) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();

                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source, new Object[0]);

                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, new Object[]{value});
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

    public static Object copyPropertie(Object source, Object target, List<String> ignores) {
        if (source == null) {
            target = null;
        } else {
            Assert.notNull(target, "Target must not be null");
            Class<?> actualEditable = target.getClass();
            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

            for (PropertyDescriptor targetPd : targetPds) {
                if ((!ignores.contains(targetPd.getName())) &&
                        (targetPd.getWriteMethod() != null)) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
                        try {
                            Method readMethod = sourcePd.getReadMethod();

                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source, new Object[0]);

                            if (value != null) {
                                Method writeMethod = targetPd.getWriteMethod();
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, new Object[]{value});
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException("Could not copy properties from source to target", ex);
                        }
                    }
                }
            }
        }

        return target;
    }

    public static Object copyPropertie(Object source, Object target, String... ignores) {
        if (source == null) {
            target = null;
        } else {
            List<String> _ignores = Arrays.asList(ignores);

            Assert.notNull(target, "Target must not be null");
            Class<?> actualEditable = target.getClass();
            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

            for (PropertyDescriptor targetPd : targetPds) {
                if ((!_ignores.contains(targetPd.getName())) &&
                        (targetPd.getWriteMethod() != null)) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
                        try {
                            Method readMethod = sourcePd.getReadMethod();

                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source, new Object[0]);

                            if (value != null) {
                                Method writeMethod = targetPd.getWriteMethod();
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, new Object[]{value});
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException("Could not copy properties from source to target", ex);
                        }
                    }
                }
            }
        }

        return target;
    }

    public static void copyProperties(Object source, Object target) throws BeansException {
        if (source == null) {
            target = null;
            return;
        }

        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());

                if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();

                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source, new Object[0]);

                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, new Object[]{value});
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

    public static Object copyPropertie(Object source, Object target) {
        if (source == null) {
            target = null;
        } else {
            Assert.notNull(target, "Target must not be null");
            Class<?> actualEditable = target.getClass();
            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

            for (PropertyDescriptor targetPd : targetPds) {
                if (targetPd.getWriteMethod() != null) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());

                    if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
                        try {
                            Method readMethod = sourcePd.getReadMethod();

                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source, new Object[0]);

                            if (value != null) {
                                Method writeMethod = targetPd.getWriteMethod();
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, new Object[]{value});
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException("Could not copy properties from source to target", ex);
                        }
                    }
                }
            }
        }

        return target;
    }
}
