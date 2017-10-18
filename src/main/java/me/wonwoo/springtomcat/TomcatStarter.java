package me.wonwoo.springtomcat;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

class TomcatStarter implements ServletContainerInitializer {

    private final ServletContextInitializer[] initializers;

    private volatile Exception startUpException;

    TomcatStarter(ServletContextInitializer[] initializers) {
        this.initializers = initializers;
    }

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {
        try {
            for (ServletContextInitializer initializer : this.initializers) {
                initializer.onStartup(servletContext);
            }
        } catch (Exception ex) {
            this.startUpException = ex;

        }
    }

    public Exception getStartUpException() {
        return this.startUpException;
    }

}
