package com.jeraff.kissmetrics.el;

import org.junit.Assert;
import org.junit.Test;

import javax.el.*;
import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * http://illegalargumentexception.blogspot.com/2008/04/java-using-el-outside-j2ee.html
 */
public class DemoEl {
    public class User {
        private String username;

        private User(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public class Balls {
        public User user;
        public HashMap<String, String> m = new HashMap<String, String>();
        public ArrayList<String> ar = new ArrayList<String>();

        private Balls(User user) {
            this.user = user;
            this.m.put("hello", "ryan");
            this.ar.add("wuddup");
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public ArrayList<String> getAr() {
            return ar;
        }

        public void setAr(ArrayList<String> ar) {
            this.ar = ar;
        }

        public HashMap<String, String> getM() {
            return m;
        }

        public void setM(HashMap<String, String> kk) {
            m = kk;
        }
    }

    @Test
    public void testELBasic() throws Exception {
        //load the expression factory
        ClassLoader cl = DemoEl.class.getClassLoader();
        Class<?> expressionFactoryClass = cl.loadClass("de.odysseus.el.ExpressionFactoryImpl");
        ExpressionFactory expressionFactory = (ExpressionFactory) expressionFactoryClass.newInstance();

        final String username = "phatduckk";
        final ELResolver demoELResolver = new DemoELResolver(new Balls(new User(username)));
        final CompositeELResolver compositeELResolver = new CompositeELResolver();
        compositeELResolver.add(demoELResolver);
        compositeELResolver.add(new ArrayELResolver());
        compositeELResolver.add(new ListELResolver());
        compositeELResolver.add(new BeanELResolver());
        compositeELResolver.add(new MapELResolver());

        final FunctionMapper functionMapper = new DemoFunctionMapper();
        final VariableMapper demoVariableMapper = new DemoVariableMapper();

        ELContext context = new ELContext() {
            @Override
            public ELResolver getELResolver() {
                return compositeELResolver;
            }

            @Override
            public FunctionMapper getFunctionMapper() {
                return functionMapper;
            }

            @Override
            public VariableMapper getVariableMapper() {
                return demoVariableMapper;
            }
        };

        String expression = null;
        ValueExpression ve = null;
        Object result = null;

        //create and resolve a value expression
        expression = "${user.username}";
        ve = expressionFactory.createValueExpression(context, expression, Object.class);
        result = ve.getValue(context);
        Assert.assertEquals(username, result);

        expression = "${m.hello}";
        ve = expressionFactory.createValueExpression(context, expression, Object.class);
        result = ve.getValue(context);
        Assert.assertEquals("ryan", result);

        expression = "${ar[0]}";
        ve = expressionFactory.createValueExpression(context, expression, Object.class);
        result = ve.getValue(context);
        Assert.assertEquals("wuddup", result);
    }

    private class DemoELResolver extends ELResolver {
        private ELResolver delegate = new CompositeELResolver() {
            {
                add(new ArrayELResolver(false));
                add(new ListELResolver(false));
                add(new MapELResolver(false));
                add(new ResourceBundleELResolver());
                add(new BeanELResolver(false));
            }
        };

        private Object base;

        public DemoELResolver(Object base) {
            this.base = base;
        }

        @Override
        public Object getValue(ELContext context, Object base, Object property) {
            if (base == null) {
                base = this.base;
            }
            return delegate.getValue(context, base, property);
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            if (base == null) {
                base = this.base;
            }
            return delegate.getCommonPropertyType(context, base);
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            if (base == null) {
                base = this.base;
            }
            return delegate.getFeatureDescriptors(context, base);
        }

        @Override
        public Class<?> getType(ELContext context, Object base, Object property) {
            if (base == null) {
                base = this.base;
            }
            return delegate.getType(context, base, property);
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base, Object property) {
            if (base == null) {
                base = this.base;
            }
            return delegate.isReadOnly(context, base, property);
        }

        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) {
            if (base == null) {
                base = this.base;
            }
            delegate.setValue(context, base, property, value);
        }
    }

    private class DemoFunctionMapper extends FunctionMapper {

        private Map<String, Method> functionMap = new HashMap<String, Method>();

        @Override
        public Method resolveFunction(String prefix, String localName) {
            String key = prefix + ":" + localName;
            return functionMap.get(key);
        }

        public void addFunction(String prefix, String localName, Method method) {
            if (prefix == null || localName == null || method == null) {
                throw new NullPointerException();
            }
            int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                throw new IllegalArgumentException("method not public");
            }
            if (!Modifier.isStatic(modifiers)) {
                throw new IllegalArgumentException("method not static");
            }
            Class<?> retType = method.getReturnType();
            if (retType == Void.TYPE) {
                throw new IllegalArgumentException("method returns void");
            }

            String key = prefix + ":" + localName;
            functionMap.put(key, method);
        }
    }

    private class DemoVariableMapper extends VariableMapper {

        private Map<String, ValueExpression> expressions = new HashMap<String, ValueExpression>();

        @Override
        public ValueExpression resolveVariable(String variable) {
            return expressions.get(variable);
        }

        @Override
        public ValueExpression setVariable(String variable, ValueExpression expression) {
            return expressions.put(variable, expression);
        }

    }
}


