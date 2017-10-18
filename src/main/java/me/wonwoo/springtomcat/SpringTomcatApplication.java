package me.wonwoo.springtomcat;

import javax.servlet.ServletRegistration;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringTomcatApplication {

    public static void main(String[] args) throws Exception {
        TomcatRunner.run(servletContext -> {
            AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
            rootContext.register(RootConfig.class);
            servletContext.addListener(new ContextLoaderListener(rootContext));
            ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
            dispatcher.setLoadOnStartup(1);
            dispatcher.addMapping("/");
        });
    }
}


