package ru.yandex.utils.spring;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class PropertiesLocationFactoryBean extends AbstractFactoryBean {
	
    private String propertyName;
    private String defaultLocation;

    protected Object createInstance() throws Exception {
        String location = System.getProperty(propertyName);
        if (location == null) {
            location = defaultLocation;
        }
        return location;
    }

    public Class getObjectType() {
        return String.class;
    }

    @Required
    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }
    
    @Required
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
