package me.wonwoo.springtomcat;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;

public class TomcatEmbedded {

    public static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private static final AtomicInteger containerCounter = new AtomicInteger(-1);
    private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();

    private int port;
    private String contextPath;
    private String displayName;

    void start(ServletContextInitializer... servletContextInitializers) {
        Tomcat tomcat = new Tomcat();
        File baseDir = createTempDir("tomcat");
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        Connector connector = new Connector(DEFAULT_PROTOCOL);
        tomcat.getService().addConnector(connector);
        connector.setPort(getPort());
        tomcat.setConnector(connector);
        tomcat.getHost().setAutoDeploy(false);
        prepareContext(tomcat.getHost(), servletContextInitializers);
        startDaemonAwaitThread(tomcat);
    }

    private void prepareContext(Host host, ServletContextInitializer... initializers) {
        File docBase = createTempDir("tomcat-docbase");
        final StandardContext context = new StandardContext();
        context.setName(getContextPath());
        context.setDisplayName(getDisplayName());
        context.setPath(getContextPath());
        context.setDocBase(docBase.getAbsolutePath());
        context.addLifecycleListener(new Tomcat.FixContextListener());
        WebappLoader loader = new WebappLoader(context.getParentClassLoader());
        loader.setLoaderClass(WebappClassLoader.class.getName());
        loader.setDelegate(true);
        context.setLoader(loader);
        addDefaultServlet(context);
        configureContext(context, initializers);
        host.addChild(context);
    }

    private void addDefaultServlet(Context context) {
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMappingDecoded("/", "default");
    }

    private void configureContext(Context context,
            ServletContextInitializer[] initializers) {
        TomcatStarter starter = new TomcatStarter(initializers);
        context.addServletContainerInitializer(starter, NO_CLASSES);
    }

    private File createTempDir(String prefix) {
        try {
            File tempDir = File.createTempFile(prefix + ".", "." + getPort());
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    private void startDaemonAwaitThread(Tomcat tomcat) {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException();
        }
        Thread awaitThread = new Thread("container-" + (containerCounter.get())) {

            @Override
            public void run() {
                tomcat.getServer().await();
            }

        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
