package com.jeraff.kissmetrics.client.el;

import com.jeraff.kissmetrics.client.el.testobjects.Holder;
import com.jeraff.kissmetrics.client.el.testobjects.User;
import org.junit.Assert;
import org.junit.Test;

import javax.el.ExpressionFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class JeraffELResolverTest {
    private ExpressionFactory getExpressionFactory() throws Exception {
        ClassLoader cl = JeraffELResolverTest.class.getClassLoader();
        try {
            Class<?> expressionFactoryClass = cl.loadClass("de.odysseus.el.ExpressionFactoryImpl");
            return (ExpressionFactory) expressionFactoryClass.newInstance();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * toss all this stuff into the Holder object which is the bean we'll be running
     * the expressions on
     *
     * @param i
     * @return
     */
    private Holder getBean(Object i) {
        return new Holder(i);
    }

    @Test
    public void testArray() throws Exception {
        int[] i = {0, 1, 2};
        JeraffELResolver r = new JeraffELResolver(getExpressionFactory(), getBean(i));
        final Object value = r.getValue("${x[0]}");
        Assert.assertEquals(0, value);
    }

    @Test
    public void testList() throws Exception {
        final String name1 = "banjo";
        final String name2 = "buddy dog";

        ArrayList<String> str = new ArrayList<String>() {
            {
                add(name1);
                add(name2);
            }
        };

        JeraffELResolver r = new JeraffELResolver(getExpressionFactory(), getBean(str));
        Assert.assertEquals(name1, r.getValue("${x[0]}"));
        Assert.assertEquals(name2, r.getValue("${x[1]}"));
    }

    @Test
    public void testMap() throws Exception {
        final String v1 = "v1";
        final int v2 = 7;

        HashMap<String, Object> m = new HashMap<String, Object>(){
            {
                put("k1", v1);
                put("k2", v2);
            }
        };

        JeraffELResolver r = new JeraffELResolver(getExpressionFactory(), getBean(m));
        Assert.assertEquals(v1, r.getValue("${x.k1}"));
        Assert.assertEquals(v2, r.getValue("${x.k2}"));
    }

    @Test
    public void testBasicBean() throws Exception {
        final String username = "phatduckk";
        final User u = new User();
        final int age = 666;
        final HashMap<String,String> attrs = new HashMap<String, String>(){{
            put("username", username);
        }};

        final Collection<String> tags = new ArrayList<String>(){{
            add(username);
            add("tag1");
        }};

        u.setAge(age);
        u.setUsername(username);
        u.setAttrs(attrs);
        u.setTags(tags);

        JeraffELResolver r = new JeraffELResolver(getExpressionFactory(), getBean(u));
        Assert.assertEquals(username, r.getValue("${x.username}"));
        Assert.assertEquals(age, r.getValue("${x.age}"));
        Assert.assertEquals(username, r.getValue("${x.attrs.username}"));
        Assert.assertEquals(username, r.getValue("${x.tags[0]}"));
    }

    @Test
    public void listOfBeans() throws Exception {
        final String username = "phatduckk";
        final ArrayList list = new ArrayList(){{
            add(new User(username));
        }};

        JeraffELResolver r = new JeraffELResolver(getExpressionFactory(), getBean(list));
        Assert.assertEquals(username, r.getValue("${x[0].username}"));
    }
}
