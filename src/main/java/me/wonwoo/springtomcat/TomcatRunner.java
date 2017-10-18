package me.wonwoo.springtomcat;

public class TomcatRunner {

    public static void run() {
        run(8080);
    }

    public static void run(int port) {
        run(port, new ServletContextInitializer[] {});
    }

    public static void run(ServletContextInitializer... servletContextInitializers) {
        run(8080, servletContextInitializers);
    }

    public static void run(int port, ServletContextInitializer... servletContextInitializers) {
        TomcatEmbedded tomcatEmbedded = new TomcatEmbedded();
        tomcatEmbedded.setPort(port);
        tomcatEmbedded.start(servletContextInitializers);
    }
}
