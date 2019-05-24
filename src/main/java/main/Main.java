package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {
    private static Server server = null;

    private static void SetUp() throws Exception {
        server = new Server(80);
        WebAppContext ctx = new WebAppContext();
        ctx.setResourceBase("src/main/webapp");
        ctx.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*jstl.jar$");
        org.eclipse.jetty.webapp.Configuration.ClassList classList = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
        classList.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classList.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
        server.setHandler(ctx);
        server.start();
    }

    private static void TearDown() throws Exception {
        server.join();
    }

    public static void main(String[] args) throws Exception {
        SetUp();
        TearDown();

    }
}
