package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by rohan on 2/8/17.
 */
@Path("/sayHello")
public class HelloWorld {
    @GET
    @Produces("text/plain")
    public String seyHello() {
        return "Hello world!";
    }
}
