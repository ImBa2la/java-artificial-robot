package ru.yandex.ir.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.HashMap;

/**
 *
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
}
