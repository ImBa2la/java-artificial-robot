package ru.yandex.utils.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import ru.yandex.utils.LogSignalHandler;

/**
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class SpringUtils {
    private static Map<String[], ApplicationContext> contexts = new HashMap<String[], ApplicationContext>();

    public static ApplicationContext getApplicationContext(String... contextLocations) {
        ApplicationContext context = contexts.get(contextLocations);
        if (context == null) {
            context = new ClassPathXmlApplicationContext(contextLocations);
            contexts.put(contextLocations, context);
        }
        return context;
    }

    public static ApplicationContext getApplicationContext(String contextLocation) {
        return getApplicationContext(new String[]{contextLocation});
    }
    
	public static void initLogger(String propertyFileName, String parameterFileName, String log4jConfig) {
		Properties properties = loadProperties(propertyFileName);		
		String logFile = properties.getProperty(parameterFileName);
		LogSignalHandler.startLog(logFile, log4jConfig);
	}
	
	public static Properties loadProperties(String propertyName) {
        try {
            Properties properties = new Properties();
            properties.load(new ClassPathResource(System.getProperty(propertyName)).getInputStream());
            return properties;
        } catch (IOException e) {
            System.err.println("Could not load SuperController properties: " + e.getMessage());
            System.exit(-1);
            return null;
        }
    }
    
}
