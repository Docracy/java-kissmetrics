package com.jeraff.kissmetrics.client.el;

import javax.el.*;

public class CompositeELResolverFactory {
    public static CompositeELResolver getCompositeELResolver(final boolean readOnly) {
        return new CompositeELResolver() {
            {
                add(new ArrayELResolver(readOnly));
                add(new ListELResolver(readOnly));
                add(new MapELResolver(readOnly));
                add(new ResourceBundleELResolver());
                add(new BeanELResolver(readOnly));
            }
        };
    }

    public static CompositeELResolver getCompositeELResolver() {
        return getCompositeELResolver(true);
    }
}
