package me.wonwoo.springtomcat;

import javax.servlet.ServletContext;

public interface ServletContextInitializer {
    void onStartup(ServletContext servletContext);
}