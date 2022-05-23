package io.github.mrdaios.templates;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TemplateProviderManager {

    private final static ConcurrentMap<String, TemplateProvider> driverConcurrentMap = new ConcurrentHashMap<>();

    static {
        loadInitialDrivers();
    }

    public static TemplateProvider getDriver(String driverType) {
        if (null == driverType) {
            throw new RuntimeException("driverType is null.");
        }
        TemplateProvider templateProvider = driverConcurrentMap.get(driverType);
        if (null == templateProvider) {
            throw new RuntimeException(String.format("not support driverType:%s.", driverType));
        }
        return templateProvider;
    }

    private static void loadInitialDrivers() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                ServiceLoader<TemplateProvider> loaderDrivers = ServiceLoader.load(TemplateProvider.class);
                for (TemplateProvider loaderDriver : loaderDrivers) {
                    String templateType = loaderDriver.getTemplateType();
                    if (templateType != null && templateType.length() > 0) {
                        driverConcurrentMap.put(templateType, loaderDriver);
                    }
                }
                return null;
            }
        });
    }
}
