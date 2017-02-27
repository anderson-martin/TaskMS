package config;

/**
 * Created by rohan on 2/8/17.
 */
import restResources.*;
import restResources.exceptionMapping.BadRequestExceptionMapper;
import restResources.exceptionMapping.ForbiddenExceptionMapper;
import restResources.exceptionMapping.NotAuthorizedExceptionMapper;
import restResources.exceptionMapping.StateConflictMapper;
import restResources.test.TestRest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * https://docs.oracle.com/javaee/7/tutorial/jaxrs002.htm
 * READ more about the purpose of this file there, section Configuring JAX-RS Applications
 */

@javax.ws.rs.ApplicationPath("rest")
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
        // exception:
        resources.add(StateConflictMapper.class);
        resources.add(ForbiddenExceptionMapper.class);
        resources.add(NotAuthorizedExceptionMapper.class);
        resources.add(BadRequestExceptionMapper.class);


        resources.add(TestRest.class);

        resources.add(Auth.class);
        resources.add(GroupResources.class);
        resources.add(UserResources.class);
        resources.add(TaskResources.class);
        resources.add(IssueResources.class);
        // register Jackson JSON providers
    }
}
