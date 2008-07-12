package ru.yandex.utils.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

public class IfThenElseFactoryBean implements FactoryBean {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(IfThenElseFactoryBean.class);

    private boolean condition;
    private Object trueBean;
    private Object falseBean;

    public Object getObject() throws Exception {
        return condition ? trueBean : falseBean;
    }

    public Class getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return true; // XXX
    }

    @Required
    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    @Required
    public void setTrueBean(Object trueBean) {
        this.trueBean = trueBean;
    }

    @Required
    public void setFalseBean(Object falseBean) {
        this.falseBean = falseBean;
    }
} //~
