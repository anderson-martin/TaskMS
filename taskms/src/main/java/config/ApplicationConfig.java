package config;

/**
 * Created by rohan on 2/8/17.
 */
import resources.HelloWorld;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * https://docs.oracle.com/javaee/7/tutorial/jaxrs002.htm
 * READ more about the purpose of this file there, section Configuring JAX-RS Applications
 */

/**
 * @author rohan
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(HelloWorld.class);
        // register Jackson JSON providers
    }
}
