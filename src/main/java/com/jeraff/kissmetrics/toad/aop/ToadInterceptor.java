package com.jeraff.kissmetrics.toad.aop;

import com.jeraff.kissmetrics.toad.ToadELHelper;
import com.jeraff.kissmetrics.toad.Toad;
import com.jeraff.kissmetrics.toad.ToadProvider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ToadInterceptor implements MethodInterceptor {
    private ToadProvider toadProvider;
    private Toad toad;
    private ToadELHelper elHelper;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            toadProvider = (ToadProvider) invocation.getThis();
            toad = toadProvider.getToad();
            elHelper = new ToadELHelper(toadProvider);

            final Kissmetrics kissAnnotation = invocation.getMethod().getAnnotation(Kissmetrics.class);

            elHelper.populateToad(kissAnnotation);

            if (kissAnnotation.runBefore()) {
                toad.run();
                return invocation.proceed();
            }

            final Object result = invocation.proceed();
            toad.run();
            return result;
        } catch(Exception e) {
            return invocation.proceed();
        }
    }
}
