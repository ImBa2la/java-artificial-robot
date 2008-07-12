package ru.yandex.utils.corba;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import ru.yandex.spring.corba.PropertiesProvider;

public class CorbaPropertiesProvider implements PropertiesProvider {
    private static final Logger logger = Logger.getLogger(CorbaPropertiesProvider.class);

    private String propertiesLocation = "corba.properties";

    public Properties getProperties() {
        Properties properties = new Properties(System.getProperties());
        Resource resource = new ClassPathResource(propertiesLocation);
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            logger.error("Could not load Corba properties", e);
            System.exit(-1);
        }
        return properties;
    }

    public void setPropertiesLocation(String propertiesLocation) {
        this.propertiesLocation = propertiesLocation;
    }
}
