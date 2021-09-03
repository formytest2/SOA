package com.tranboot.client.utils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.springframework.aop.AopInvocationException;
import org.springframework.aop.RawTargetAccess;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

public class CustomAopProxy {

    public static Class<?> getTargetClass(Object object) {
        return ClassUtils.isCglibProxy(object) ? object.getClass().getSuperclass() : object.getClass();
    }

    public static <T> T proxy(Class<T> clazz, Callback interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        CallbackFilter filter = new CustomAopProxy.ConcreteClassCallbackFilter();
        enhancer.setCallbackFilter(filter);
        Callback[] callbacks = new Callback[]{interceptor};
        enhancer.setCallbackFilter(new CustomAopProxy.ConcreteClassCallbackFilter());
        enhancer.setCallbacks(callbacks);
        return (T)enhancer.create();
    }

    private static Object processReturnType(Object proxy, Object target, Method method, Object retVal) {
        if (retVal != null && retVal == target && !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
            retVal = proxy;
        }

        Class<?> returnType = method.getReturnType();
        if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
        } else {
            return retVal;
        }
    }

    public static Object[] adaptArgumentsIfNecessary(Method method, Object... arguments) {
        if (method.isVarArgs() && !ObjectUtils.isEmpty(arguments)) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == arguments.length) {
                int varargIndex = paramTypes.length - 1;
                Class<?> varargType = paramTypes[varargIndex];
                if (varargType.isArray()) {
                    Object varargArray = arguments[varargIndex];
                    if (varargArray instanceof Object[] && !varargType.isInstance(varargArray)) {
                        Object[] newArguments = new Object[arguments.length];
                        System.arraycopy(arguments, 0, newArguments, 0, varargIndex);
                        Class<?> targetElementType = varargType.getComponentType();
                        int varargLength = Array.getLength(varargArray);
                        Object newVarargArray = Array.newInstance(targetElementType, varargLength);
                        System.arraycopy(varargArray, 0, newVarargArray, 0, varargLength);
                        newArguments[varargIndex] = newVarargArray;
                        return newArguments;
                    }
                }
            }
        }

        return arguments;
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {
        private final MethodProxy methodProxy;
        private final boolean publicMethod;

        public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
            this.methodProxy = methodProxy;
            this.publicMethod = Modifier.isPublic(method.getModifiers());
        }

        protected Object invokeJoinpoint() throws Throwable {
            return this.publicMethod ? this.methodProxy.invoke(this.target, this.arguments) : super.invokeJoinpoint();
        }
    }

    public static class StaticUnadvisedInterceptor implements MethodInterceptor, Serializable {
        private static final long serialVersionUID = 1770180708216539010L;
        private final Object target;

        public StaticUnadvisedInterceptor(Object target) {
            this.target = target;
        }

        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object retVal = methodProxy.invoke(this.target, args);
            return CustomAopProxy.processReturnType(proxy, this.target, method, retVal);
        }
    }

    public static class ConcreteClassCallbackFilter implements CallbackFilter {
        public ConcreteClassCallbackFilter() {
        }

        public int accept(Method method) {
            return 0;
        }
    }
}

