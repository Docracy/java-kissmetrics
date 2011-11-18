package com.jeraff.kissmetrics.client.el;

import javax.el.*;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class JeraffELResolver extends ELResolver {
    private ExpressionFactory expressionFactory;
    private Object base;
    private ELResolver delegate;
    private ELContext elContext;

    public JeraffELResolver(ExpressionFactory expressionFactory, Object base) {
        this.expressionFactory = expressionFactory;
        this.delegate = CompositeELResolverFactory.getCompositeELResolver();
        this.base = base;

        final CompositeELResolver compositeELResolver = CompositeELResolverFactory
                .getCompositeELResolver();
        compositeELResolver.add(this);

        final FunctionMapper functionMapper = new JeraffFunctionMapper();
        final JeraffVariableMapper variableMapper = new JeraffVariableMapper();

        this.elContext = new ELContext() {
            @Override
            public ELResolver getELResolver() {
                return compositeELResolver;
            }

            @Override
            public FunctionMapper getFunctionMapper() {
                return functionMapper;
            }

            @Override
            public JeraffVariableMapper getVariableMapper() {
                return variableMapper;
            }
        };
    }

    public Object getValue(String expression) {
        final ValueExpression valueExpression = expressionFactory
                .createValueExpression(elContext, expression, Object.class);

        return valueExpression.getValue(elContext);
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
