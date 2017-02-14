package restResources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by rohan on 2/8/17.
 */
@Path("/sayHello")
public class HelloWorld {
    @GET
    @Produces("application/json")
    public String seyHello() {
        return "vittu";
    }
}
