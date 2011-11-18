package com.jeraff.kissmetrics.toad.aop;

import com.jeraff.kissmetrics.client.KissMetricsException;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.jeraff.kissmetrics.toad.Toad;
import com.jeraff.kissmetrics.toad.ToadProvider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ToadInterceptor implements MethodInterceptor {
    private ToadProvider toadProvider;
    private Toad toad;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            toadProvider = (ToadProvider) invocation.getThis();
            toad = toadProvider.getToad();

            final Kissmetrics kissAnnotation = invocation.getMethod().getAnnotation(Kissmetrics.class);

            populateToad(kissAnnotation);

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

    private void populateToad(Kissmetrics annotation) throws KissMetricsException {
        // if the annotation has a default value, we assume it's a simple record
        if (annotation.value() != null) {
            final Record record = annotation.value();
            KissMetricsProperties props = (!record.props().isEmpty()) ?
                    (KissMetricsProperties) toad.resolve(record.props()) :
                    new KissMetricsProperties();
            toad.user((String)toad.resolve(record.id()))
                    .record((String)toad.resolve(record.event()), props);
        }

        // build up the aliasing
        for (Alias alias : annotation.alias()) {
            toad.user((String)toad.resolve(alias.id()))
                    .alias((String)toad.resolve(alias.to()));
        }

        for (Set set : annotation.set()) {
            if (set.props() != null) {
                toad.user((String)toad.resolve(set.id()))
                        .set((KissMetricsProperties) toad.resolve(set.props()));
            } else {
                toad.user((String)toad.resolve(set.id()))
                        .set((String)toad.resolve(set.key()), set.val());
            }
        }
    }

}
